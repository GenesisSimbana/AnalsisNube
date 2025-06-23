package com.banquito.originacion.analisis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.originacion.analisis.model.ObservacionAnalistas;

@Repository
public interface ObservacionAnalistasRepository extends JpaRepository<ObservacionAnalistas, Integer> {
    
    List<ObservacionAnalistas> findByIdSolicitud(Integer idSolicitud);
    
    List<ObservacionAnalistas> findByUsuario(String usuario);

    List<ObservacionAnalistas> findByIdSolicitudAndUsuario(Integer idSolicitud, String usuario);

    List<ObservacionAnalistas> findByIdSolicitudOrderByFechaHoraDesc(Integer idSolicitud);
    
    List<ObservacionAnalistas> findByUsuarioOrderByFechaHoraDesc(String usuario);
} 