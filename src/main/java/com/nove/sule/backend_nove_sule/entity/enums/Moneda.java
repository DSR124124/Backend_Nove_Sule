package com.nove.sule.backend_nove_sule.entity.enums;

/**
 * Enum para definir las monedas
 */
public enum Moneda {
    PEN("Soles Peruanos"),
    USD("Dólares Americanos");

    private final String descripcion;

    Moneda(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
