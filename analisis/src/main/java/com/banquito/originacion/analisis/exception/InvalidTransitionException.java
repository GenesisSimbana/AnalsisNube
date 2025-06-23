package com.banquito.originacion.analisis.exception;

import com.banquito.originacion.analisis.enums.EstadoHistorialEnum;

public class InvalidTransitionException extends RuntimeException {

    public InvalidTransitionException(String message) {
        super(message);
    }

    public InvalidTransitionException(Integer idSolicitud, EstadoHistorialEnum estadoActual, EstadoHistorialEnum nuevoEstado) {
        super("Transición de estado inválida para la solicitud " + idSolicitud + 
              ": no se puede cambiar de " + estadoActual + " a " + nuevoEstado + ".");
    }
} 