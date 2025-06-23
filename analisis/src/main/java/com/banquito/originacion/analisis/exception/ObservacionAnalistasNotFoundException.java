package com.banquito.originacion.analisis.exception;

public class ObservacionAnalistasNotFoundException extends RuntimeException {

    private final String data;
    private final String entity;

    public ObservacionAnalistasNotFoundException(String data, String entity) {
        super();
        this.data = data;
        this.entity = entity;
    }

    @Override
    public String getMessage() {
        return "No se encontró ninguna observación de analista para: " + this.entity + ", con el dato: " + data;
    }
} 