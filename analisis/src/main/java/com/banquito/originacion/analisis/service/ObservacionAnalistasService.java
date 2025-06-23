package com.banquito.originacion.analisis.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.originacion.analisis.exception.ObservacionAnalistasNotFoundException;
import com.banquito.originacion.analisis.model.ObservacionAnalistas;
import com.banquito.originacion.analisis.repository.ObservacionAnalistasRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class ObservacionAnalistasService {
    
    private static final Logger log = LoggerFactory.getLogger(ObservacionAnalistasService.class);

    private final ObservacionAnalistasRepository observacionAnalistasRepository;
    
    public ObservacionAnalistasService(ObservacionAnalistasRepository observacionAnalistasRepository) {
        this.observacionAnalistasRepository = observacionAnalistasRepository;
    }
    
    public List<ObservacionAnalistas> findAll() {
        log.info("Finding all ObservacionAnalistas.");
        return observacionAnalistasRepository.findAll();
    }
    
    public Page<ObservacionAnalistas> findAllPaginated(Pageable pageable) {
        log.info("Finding all ObservacionAnalistas with pagination: {}", pageable);
        return observacionAnalistasRepository.findAll(pageable);
    }
    
    public ObservacionAnalistas findById(Integer idObservacionAnalista) {
        log.info("Attempting to find ObservacionAnalistas with id: {}", idObservacionAnalista);
        return observacionAnalistasRepository.findById(idObservacionAnalista)
            .orElseThrow(() -> {
                log.warn("ObservacionAnalistas with id: {} not found.", idObservacionAnalista);
                return new ObservacionAnalistasNotFoundException(
                    idObservacionAnalista.toString(), "ID de observación");
            });
    }
    
    public List<ObservacionAnalistas> findByIdSolicitud(Integer idSolicitud) {
        log.info("Finding ObservacionAnalistas by idSolicitud: {}", idSolicitud);
        return observacionAnalistasRepository.findByIdSolicitud(idSolicitud);
    }
    
    public List<ObservacionAnalistas> findByUsuario(String usuario) {
        log.info("Finding ObservacionAnalistas by usuario: {}", usuario);
        return observacionAnalistasRepository.findByUsuario(usuario);
    }
    
    public List<ObservacionAnalistas> findByIdSolicitudOrderByFechaHoraDesc(Integer idSolicitud) {
        log.info("Finding ObservacionAnalistas by idSolicitud ordered by date: {}", idSolicitud);
        return observacionAnalistasRepository.findByIdSolicitudOrderByFechaHoraDesc(idSolicitud);
    }
    
    public List<ObservacionAnalistas> findByUsuarioOrderByFechaHoraDesc(String usuario) {
        log.info("Finding ObservacionAnalistas by usuario ordered by date: {}", usuario);
        return observacionAnalistasRepository.findByUsuarioOrderByFechaHoraDesc(usuario);
    }
    
    public ObservacionAnalistas save(ObservacionAnalistas observacionAnalistas) {
        log.info("Attempting to save new ObservacionAnalistas for solicitud: {}", observacionAnalistas.getIdSolicitud());
        log.debug("New observacion details: {}", observacionAnalistas);
        if (observacionAnalistas.getFechaHora() == null) {
            observacionAnalistas.setFechaHora(LocalDateTime.now());
        }
        if (observacionAnalistas.getVersion() == null) {
            observacionAnalistas.setVersion(BigDecimal.ONE);
        }
        ObservacionAnalistas savedObservacion = observacionAnalistasRepository.save(observacionAnalistas);
        log.info("Successfully saved new ObservacionAnalistas with id {} for solicitud {}", savedObservacion.getIdObservacionAnalista(), savedObservacion.getIdSolicitud());
        return savedObservacion;
    }
    
    public ObservacionAnalistas update(Integer idObservacionAnalista, ObservacionAnalistas observacionAnalistas) {
        log.info("Attempting to fully update ObservacionAnalistas with id: {}", idObservacionAnalista);
        log.debug("Update data: {}", observacionAnalistas);
        ObservacionAnalistas existingObservacion = findById(idObservacionAnalista);
        
        existingObservacion.setIdSolicitud(observacionAnalistas.getIdSolicitud());
        existingObservacion.setUsuario(observacionAnalistas.getUsuario());
        existingObservacion.setFechaHora(observacionAnalistas.getFechaHora());
        existingObservacion.setRazonIntervencion(observacionAnalistas.getRazonIntervencion());
        existingObservacion.setVersion(existingObservacion.getVersion().add(BigDecimal.ONE));
        
        ObservacionAnalistas updatedObservacion = observacionAnalistasRepository.save(existingObservacion);
        log.info("Successfully updated ObservacionAnalistas with id: {}", updatedObservacion.getIdObservacionAnalista());
        return updatedObservacion;
    }
    
    public ObservacionAnalistas partialUpdate(Integer idObservacionAnalista, ObservacionAnalistas observacionAnalistas) {
        log.info("Attempting to partially update ObservacionAnalistas with id: {}", idObservacionAnalista);
        log.debug("Partial update data: {}", observacionAnalistas);
        ObservacionAnalistas existingObservacion = findById(idObservacionAnalista);
        
        if (observacionAnalistas.getIdSolicitud() != null) {
            existingObservacion.setIdSolicitud(observacionAnalistas.getIdSolicitud());
        }
        if (observacionAnalistas.getUsuario() != null) {
            existingObservacion.setUsuario(observacionAnalistas.getUsuario());
        }
        if (observacionAnalistas.getFechaHora() != null) {
            existingObservacion.setFechaHora(observacionAnalistas.getFechaHora());
        }
        if (observacionAnalistas.getRazonIntervencion() != null) {
            existingObservacion.setRazonIntervencion(observacionAnalistas.getRazonIntervencion());
        }
        
        existingObservacion.setVersion(existingObservacion.getVersion().add(BigDecimal.ONE));
        
        ObservacionAnalistas updatedObservacion = observacionAnalistasRepository.save(existingObservacion);
        log.info("Successfully partially updated ObservacionAnalistas with id: {}", updatedObservacion.getIdObservacionAnalista());
        return updatedObservacion;
    }
    
    public void deleteById(Integer idObservacionAnalista) {
        log.info("Attempting to delete ObservacionAnalistas with id: {}", idObservacionAnalista);
        if (!observacionAnalistasRepository.existsById(idObservacionAnalista)) {
            log.warn("Delete failed. ObservacionAnalistas with id: {} not found.", idObservacionAnalista);
            throw new ObservacionAnalistasNotFoundException(
                idObservacionAnalista.toString(), "ID de observación");
        }
        observacionAnalistasRepository.deleteById(idObservacionAnalista);
        log.info("Successfully deleted ObservacionAnalistas with id: {}", idObservacionAnalista);
    }
    
    public boolean existsById(Integer idObservacionAnalista) {
        return observacionAnalistasRepository.existsById(idObservacionAnalista);
    }
} 