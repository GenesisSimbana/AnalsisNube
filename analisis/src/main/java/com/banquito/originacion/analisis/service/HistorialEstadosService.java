package com.banquito.originacion.analisis.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.originacion.analisis.exception.HistorialEstadosNotFoundException;
import com.banquito.originacion.analisis.enums.EstadoHistorialEnum;
import com.banquito.originacion.analisis.model.HistorialEstados;
import com.banquito.originacion.analisis.repository.HistorialEstadosRepository;
import com.banquito.originacion.analisis.exception.InvalidTransitionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class HistorialEstadosService {
    
    private static final Logger log = LoggerFactory.getLogger(HistorialEstadosService.class);
    
    private final HistorialEstadosRepository historialEstadosRepository;
    
    public HistorialEstadosService(HistorialEstadosRepository historialEstadosRepository) {
        this.historialEstadosRepository = historialEstadosRepository;
    }
    
    public List<HistorialEstados> findAll() {
        return historialEstadosRepository.findAll();
    }
    
    public Page<HistorialEstados> findAllPaginated(Pageable pageable) {
        return historialEstadosRepository.findAll(pageable);
    }
    
    public HistorialEstados findById(Integer idHistorial) {
        log.info("Attempting to find HistorialEstados with id: {}", idHistorial);
        Optional<HistorialEstados> historial = historialEstadosRepository.findById(idHistorial);
        if (historial.isEmpty()) {
            log.warn("HistorialEstados with id: {} not found.", idHistorial);
            throw new HistorialEstadosNotFoundException(idHistorial.toString(), "ID de historial");
        }
        log.info("Successfully found HistorialEstados with id: {}", idHistorial);
        return historial.get();
    }
    
    public List<HistorialEstados> findByIdSolicitud(Integer idSolicitud) {
        return historialEstadosRepository.findByIdSolicitud(idSolicitud);
    }
    
    public List<HistorialEstados> findByEstado(EstadoHistorialEnum estado) {
        return historialEstadosRepository.findByEstado(estado);
    }
    
    public List<HistorialEstados> findByIdSolicitudAndEstado(Integer idSolicitud, EstadoHistorialEnum estado) {
        return historialEstadosRepository.findByIdSolicitudAndEstado(idSolicitud, estado);
    }
    
    public List<HistorialEstados> findByUsuario(String usuario) {
        return historialEstadosRepository.findByUsuario(usuario);
    }
    
    public Optional<HistorialEstados> findLatestByIdSolicitud(Integer idSolicitud) {
        return historialEstadosRepository.findFirstByIdSolicitudOrderByFechaHoraDesc(idSolicitud);
    }
    
    public List<HistorialEstados> findByIdSolicitudOrderByFechaHoraDesc(Integer idSolicitud) {
        return historialEstadosRepository.findByIdSolicitudOrderByFechaHoraDesc(idSolicitud);
    }
    
    public HistorialEstados save(HistorialEstados historialEstados) {
        log.info("Attempting to save new HistorialEstados for solicitud: {}", historialEstados.getIdSolicitud());
        log.debug("New state details: {}", historialEstados);

        // Regla de negocio: Validar que el estado sea válido
        if (historialEstados.getEstado() == null) {
            log.error("Save attempt failed: State is null.");
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        
        // Regla de negocio: Validar que el usuario no esté vacío
        if (historialEstados.getUsuario() == null || historialEstados.getUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario no puede estar vacío");
        }
        
        // Regla de negocio: Validar que el motivo no esté vacío
        if (historialEstados.getMotivo() == null || historialEstados.getMotivo().trim().isEmpty()) {
            log.error("Save attempt failed: Motivo is null or empty for solicitud: {}.", historialEstados.getIdSolicitud());
            throw new IllegalArgumentException("El motivo no puede estar vacío");
        }

        Integer idSolicitud = historialEstados.getIdSolicitud();
        EstadoHistorialEnum nuevoEstado = historialEstados.getEstado();

        Optional<HistorialEstados> ultimoHistorialOpt = this.findLatestByIdSolicitud(idSolicitud);

        if (ultimoHistorialOpt.isPresent()) {
            // La solicitud ya tiene un historial, se debe validar la transición.
            HistorialEstados ultimoHistorial = ultimoHistorialOpt.get();
            EstadoHistorialEnum estadoActual = ultimoHistorial.getEstado();
            log.debug("Existing solicitud {}. Current state is {}. Attempting to transition to {}.", idSolicitud, estadoActual, nuevoEstado);

            // Regla 2: Evitar estados duplicados consecutivos.
            if (estadoActual == nuevoEstado) {
                log.warn("Invalid transition for solicitud {}: Attempted to transition to the same state {}.", idSolicitud, nuevoEstado);
                throw new InvalidTransitionException("La solicitud " + idSolicitud + " ya se encuentra en el estado " + nuevoEstado + ".");
            }

            // Regla 1: Validar si la transición de estado es permitida.
            if (!this.validarTransicionEstado(estadoActual, nuevoEstado)) {
                log.error("Invalid transition for solicitud {}: Cannot transition from {} to {}.", idSolicitud, estadoActual, nuevoEstado);
                throw new InvalidTransitionException(idSolicitud, estadoActual, nuevoEstado);
            }
            
            // Regla de negocio: Incrementar la versión basada en el último registro.
            historialEstados.setVersion(ultimoHistorial.getVersion().add(BigDecimal.ONE));
        } else {
            // Es el primer estado para esta solicitud.
            // Regla: El primer estado de una solicitud siempre debe ser 'Borrador'.
            log.debug("This is the first state for solicitud {}. Validating initial state is 'Borrador'.", idSolicitud);
            if (nuevoEstado != EstadoHistorialEnum.Borrador) {
                log.error("Invalid initial state for new solicitud {}: Must be 'Borrador', but was '{}'.", idSolicitud, nuevoEstado);
                throw new InvalidTransitionException("El estado inicial para una nueva solicitud debe ser 'Borrador', no '" + nuevoEstado + "'.");
            }
            
            // Regla de negocio: La versión inicial es 1.
            historialEstados.setVersion(BigDecimal.ONE);
        }
        
        // Regla de negocio: Fecha automática si no se proporciona
        if (historialEstados.getFechaHora() == null) {
            historialEstados.setFechaHora(LocalDateTime.now());
        }
        
        HistorialEstados savedHistorial = historialEstadosRepository.save(historialEstados);
        log.info("Successfully saved new HistorialEstados with id {} for solicitud {}", savedHistorial.getIdHistorial(), savedHistorial.getIdSolicitud());
        return savedHistorial;
    }

    public HistorialEstados update(Integer idHistorial, HistorialEstados historialEstados) {
        log.info("Attempting to fully update HistorialEstados with id: {}", idHistorial);
        log.debug("Update data: {}", historialEstados);
        HistorialEstados existingHistorial = findById(idHistorial);
        
        if (historialEstados.getEstado() == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        
        if (historialEstados.getUsuario() == null || historialEstados.getUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario no puede estar vacío");
        }
        
        if (historialEstados.getMotivo() == null || historialEstados.getMotivo().trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo no puede estar vacío");
        }
        
        BigDecimal nuevaVersion = existingHistorial.getVersion().add(BigDecimal.ONE);
        
        existingHistorial.setIdSolicitud(historialEstados.getIdSolicitud());
        existingHistorial.setEstado(historialEstados.getEstado());
        existingHistorial.setFechaHora(historialEstados.getFechaHora());
        existingHistorial.setUsuario(historialEstados.getUsuario());
        existingHistorial.setMotivo(historialEstados.getMotivo());
        existingHistorial.setVersion(nuevaVersion);
        
        HistorialEstados updatedHistorial = historialEstadosRepository.save(existingHistorial);
        log.info("Successfully updated HistorialEstados with id: {}", updatedHistorial.getIdHistorial());
        return updatedHistorial;
    }

    public HistorialEstados partialUpdate(Integer idHistorial, HistorialEstados historialEstados) {
        log.info("Attempting to partially update HistorialEstados with id: {}", idHistorial);
        log.debug("Partial update data: {}", historialEstados);
        HistorialEstados existingHistorial = findById(idHistorial);
        
        if (historialEstados.getIdSolicitud() != null) {
            existingHistorial.setIdSolicitud(historialEstados.getIdSolicitud());
        }
        if (historialEstados.getEstado() != null) {
            existingHistorial.setEstado(historialEstados.getEstado());
        }
        if (historialEstados.getFechaHora() != null) {
            existingHistorial.setFechaHora(historialEstados.getFechaHora());
        }
        if (historialEstados.getUsuario() != null && !historialEstados.getUsuario().trim().isEmpty()) {
            existingHistorial.setUsuario(historialEstados.getUsuario());
        }
        if (historialEstados.getMotivo() != null && !historialEstados.getMotivo().trim().isEmpty()) {
            existingHistorial.setMotivo(historialEstados.getMotivo());
        }
        
        existingHistorial.setVersion(existingHistorial.getVersion().add(BigDecimal.ONE));
        
        HistorialEstados updatedHistorial = historialEstadosRepository.save(existingHistorial);
        log.info("Successfully partially updated HistorialEstados with id: {}", updatedHistorial.getIdHistorial());
        return updatedHistorial;
    }

    public void deleteById(Integer idHistorial) {
        log.info("Attempting to delete HistorialEstados with id: {}", idHistorial);
        if (!historialEstadosRepository.existsById(idHistorial)) {
            log.warn("Delete failed. HistorialEstados with id: {} not found.", idHistorial);
            throw new HistorialEstadosNotFoundException(idHistorial.toString(), "ID de historial");
        }
        historialEstadosRepository.deleteById(idHistorial);
        log.info("Successfully deleted HistorialEstados with id: {}", idHistorial);
    }
    
    public boolean existsById(Integer idHistorial) {
        return historialEstadosRepository.existsById(idHistorial);
    }

    public EstadoHistorialEnum getEstadoActualSolicitud(Integer idSolicitud) {
        log.info("Fetching current state for solicitud id: {}", idSolicitud);
        Optional<HistorialEstados> ultimoHistorial = findLatestByIdSolicitud(idSolicitud);
        if (ultimoHistorial.isPresent()) {
            EstadoHistorialEnum estado = ultimoHistorial.get().getEstado();
            log.info("Current state for solicitud {} is {}.", idSolicitud, estado);
            return estado;
        }
        log.warn("No history found for solicitud id: {}. Cannot determine current state.", idSolicitud);
        return null; // No hay historial para esta solicitud
    }

    public boolean validarTransicionEstado(EstadoHistorialEnum estadoActual, EstadoHistorialEnum nuevoEstado) {
        log.debug("Validating transition from {} to {}", estadoActual, nuevoEstado);
        // Regla de negocio: Flujo de estados permitidos
        switch (estadoActual) {
            case Borrador:
                return nuevoEstado == EstadoHistorialEnum.EnRevision || 
                       nuevoEstado == EstadoHistorialEnum.Cancelada;
            case EnRevision:
                return nuevoEstado == EstadoHistorialEnum.Aprobada || 
                       nuevoEstado == EstadoHistorialEnum.Rechazada || 
                       nuevoEstado == EstadoHistorialEnum.Borrador;
            case Aprobada:
            case Rechazada:
            case Cancelada:
                log.debug("Transition from final state {} is not allowed.", estadoActual);
                return false; // Estados finales, no se pueden cambiar
            default:
                log.warn("Attempted transition from an unknown or unhandled state: {}", estadoActual);
                return false;
        }
    }
} 