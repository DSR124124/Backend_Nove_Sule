package com.nove.sule.backend_nove_sule.entity.enums;

/**
 * Enum para definir los tipos de cuenta bancaria
 */
public enum TipoCuenta {
    AHORROS("Ahorros"),
    CORRIENTE("Corriente");

    private final String descripcion;

    TipoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
