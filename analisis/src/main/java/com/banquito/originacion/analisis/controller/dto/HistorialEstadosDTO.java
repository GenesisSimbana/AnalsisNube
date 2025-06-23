package com.banquito.originacion.analisis.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.banquito.originacion.analisis.enums.EstadoHistorialEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Schema(description = "DTO para el historial de estados de solicitudes")
public class HistorialEstadosDTO {
    
    @Schema(description = "ID del historial", example = "1")
    private Integer idHistorial;
    
    @NotNull(message = "El ID de solicitud es obligatorio")
    @Schema(description = "ID de la solicitud", example = "12345")
    private Integer idSolicitud;
    
    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "Estado del historial", example = "PENDIENTE")
    private EstadoHistorialEnum estado;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha y hora del cambio de estado", example = "2024-01-15 10:30:00")
    private LocalDateTime fechaHora;
    
    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede tener más de 50 caracteres")
    @Schema(description = "Usuario que realizó el cambio", example = "analista01")
    private String usuario;
    
    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 120, message = "El motivo no puede tener más de 120 caracteres")
    @Schema(description = "Motivo del cambio de estado", example = "Solicitud en revisión inicial")
    private String motivo;
    
    @Schema(description = "Versión del registro", example = "1")
    private BigDecimal version;
} 