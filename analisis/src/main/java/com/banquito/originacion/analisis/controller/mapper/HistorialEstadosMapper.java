package com.banquito.originacion.analisis.controller.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.banquito.originacion.analisis.controller.dto.HistorialEstadosDTO;
import com.banquito.originacion.analisis.model.HistorialEstados;

@Component
public class HistorialEstadosMapper {
    
    public HistorialEstadosDTO toDTO(HistorialEstados historialEstados) {
        if (historialEstados == null) {
            return null;
        }
        
        HistorialEstadosDTO dto = new HistorialEstadosDTO();
        dto.setIdHistorial(historialEstados.getIdHistorial());
        dto.setIdSolicitud(historialEstados.getIdSolicitud());
        dto.setEstado(historialEstados.getEstado());
        dto.setFechaHora(historialEstados.getFechaHora());
        dto.setUsuario(historialEstados.getUsuario());
        dto.setMotivo(historialEstados.getMotivo());
        dto.setVersion(historialEstados.getVersion());
        
        return dto;
    }
    
    public HistorialEstados toEntity(HistorialEstadosDTO dto) {
        if (dto == null) {
            return null;
        }
        
        HistorialEstados entity = new HistorialEstados();
        entity.setIdHistorial(dto.getIdHistorial());
        entity.setIdSolicitud(dto.getIdSolicitud());
        entity.setEstado(dto.getEstado());
        entity.setFechaHora(dto.getFechaHora());
        entity.setUsuario(dto.getUsuario());
        entity.setMotivo(dto.getMotivo());
        entity.setVersion(dto.getVersion());
        
        return entity;
    }
    
    public List<HistorialEstadosDTO> toDTOList(List<HistorialEstados> historialEstadosList) {
        if (historialEstadosList == null) {
            return new ArrayList<>();
        }
        
        List<HistorialEstadosDTO> dtoList = new ArrayList<>(historialEstadosList.size());
        for (HistorialEstados historialEstados : historialEstadosList) {
            dtoList.add(toDTO(historialEstados));
        }
        
        return dtoList;
    }
    
    public List<HistorialEstados> toEntityList(List<HistorialEstadosDTO> dtoList) {
        if (dtoList == null) {
            return new ArrayList<>();
        }
        
        List<HistorialEstados> entityList = new ArrayList<>(dtoList.size());
        for (HistorialEstadosDTO dto : dtoList) {
            entityList.add(toEntity(dto));
        }
        
        return entityList;
    }
} 