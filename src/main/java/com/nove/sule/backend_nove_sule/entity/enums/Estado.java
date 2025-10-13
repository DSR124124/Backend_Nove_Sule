package com.nove.sule.backend_nove_sule.entity.enums;

/**
 * Enum para definir el estado general de las entidades
 */
public enum Estado {
    ACTIVO("Activo"),
    INACTIVO("Inactivo");

    private final String descripcion;

    Estado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
