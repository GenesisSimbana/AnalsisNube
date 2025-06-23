package com.banquito.originacion.analisis.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Schema(description = "DTO para las observaciones de analistas")
public class ObservacionAnalistasDTO {
    
    @Schema(description = "ID de la observación", example = "1")
    private Integer idObservacionAnalista;
    
    @NotNull(message = "El ID de solicitud es obligatorio")
    @Schema(description = "ID de la solicitud", example = "12345")
    private Integer idSolicitud;
    
    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 50, message = "El usuario no puede tener más de 50 caracteres")
    @Schema(description = "Usuario que realizó la observación", example = "analista01")
    private String usuario;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Fecha y hora de la observación", example = "2024-01-15 10:30:00")
    private LocalDateTime fechaHora;
    
    @NotBlank(message = "La razón de intervención es obligatoria")
    @Size(min = 30, max = 500, message = "La razón de intervención debe tener entre 30 y 500 caracteres")
    @Schema(description = "Razón de la intervención del analista", example = "Se requiere documentación adicional para completar el análisis de riesgo crediticio")
    private String razonIntervencion;
    
    @Schema(description = "Versión del registro", example = "1")
    private BigDecimal version;
} 