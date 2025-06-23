package com.banquito.originacion.analisis.controller.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.banquito.originacion.analisis.controller.dto.ObservacionAnalistasDTO;
import com.banquito.originacion.analisis.model.ObservacionAnalistas;

@Component
public class ObservacionAnalistasMapper {
    
    public ObservacionAnalistasDTO toDTO(ObservacionAnalistas observacionAnalistas) {
        if (observacionAnalistas == null) {
            return null;
        }
        
        ObservacionAnalistasDTO dto = new ObservacionAnalistasDTO();
        dto.setIdObservacionAnalista(observacionAnalistas.getIdObservacionAnalista());
        dto.setIdSolicitud(observacionAnalistas.getIdSolicitud());
        dto.setUsuario(observacionAnalistas.getUsuario());
        dto.setFechaHora(observacionAnalistas.getFechaHora());
        dto.setRazonIntervencion(observacionAnalistas.getRazonIntervencion());
        dto.setVersion(observacionAnalistas.getVersion());
        
        return dto;
    }
    
    public ObservacionAnalistas toEntity(ObservacionAnalistasDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ObservacionAnalistas entity = new ObservacionAnalistas();
        entity.setIdObservacionAnalista(dto.getIdObservacionAnalista());
        entity.setIdSolicitud(dto.getIdSolicitud());
        entity.setUsuario(dto.getUsuario());
        entity.setFechaHora(dto.getFechaHora());
        entity.setRazonIntervencion(dto.getRazonIntervencion());
        entity.setVersion(dto.getVersion());
        
        return entity;
    }
    
    public List<ObservacionAnalistasDTO> toDTOList(List<ObservacionAnalistas> observacionAnalistasList) {
        if (observacionAnalistasList == null) {
            return new ArrayList<>();
        }
        
        List<ObservacionAnalistasDTO> dtoList = new ArrayList<>(observacionAnalistasList.size());
        for (ObservacionAnalistas observacionAnalistas : observacionAnalistasList) {
            dtoList.add(toDTO(observacionAnalistas));
        }
        
        return dtoList;
    }
    
    public List<ObservacionAnalistas> toEntityList(List<ObservacionAnalistasDTO> dtoList) {
        if (dtoList == null) {
            return new ArrayList<>();
        }
        
        List<ObservacionAnalistas> entityList = new ArrayList<>(dtoList.size());
        for (ObservacionAnalistasDTO dto : dtoList) {
            entityList.add(toEntity(dto));
        }
        
        return entityList;
    }
} 