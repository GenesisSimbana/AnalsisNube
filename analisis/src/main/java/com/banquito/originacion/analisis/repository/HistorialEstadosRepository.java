package com.banquito.originacion.analisis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.HistorialEstados;
import com.banquito.originacion.analisis.enums.EstadoHistorialEnum;

@Repository
public interface HistorialEstadosRepository extends JpaRepository<HistorialEstados, Integer> {
    
    List<HistorialEstados> findByIdSolicitud(Integer idSolicitud);
    
    List<HistorialEstados> findByEstado(EstadoHistorialEnum estado);

    List<HistorialEstados> findByUsuario(String usuario);
    
    List<HistorialEstados> findByIdSolicitudAndEstado(Integer idSolicitud, EstadoHistorialEnum estado);
        
    Optional<HistorialEstados> findFirstByIdSolicitudOrderByFechaHoraDesc(Integer idSolicitud);
    
    List<HistorialEstados> findByIdSolicitudOrderByFechaHoraDesc(Integer idSolicitud);
} 