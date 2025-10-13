package com.nove.sule.backend_nove_sule.entity.enums;

/**
 * Enum para definir los tipos de IGV
 */
public enum TipoIGV {
    GRAVADO("Gravado"),
    EXONERADO("Exonerado"),
    INAFECTO("Inafecto");

    private final String descripcion;

    TipoIGV(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
