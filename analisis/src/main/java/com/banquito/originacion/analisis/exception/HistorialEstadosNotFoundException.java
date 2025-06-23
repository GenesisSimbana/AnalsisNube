package com.banquito.originacion.analisis.exception;

public class HistorialEstadosNotFoundException extends RuntimeException {

    private final String data;
    private final String entity;

    public HistorialEstadosNotFoundException(String data, String entity) {
        super();
        this.data = data;
        this.entity = entity;
    }

    @Override
    public String getMessage() {
        return "No se encontró ningún historial de estados para: " + this.entity + ", con el dato: " + data;
    }
} 