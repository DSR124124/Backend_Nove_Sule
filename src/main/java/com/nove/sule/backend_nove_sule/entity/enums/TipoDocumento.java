package com.nove.sule.backend_nove_sule.entity.enums;

/**
 * Enum para definir los tipos de documento de identidad
 */
public enum TipoDocumento {
    DNI("DNI"),
    RUC("RUC"),
    PASAPORTE("Pasaporte");

    private final String descripcion;

    TipoDocumento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
