package com.nove.sule.backend_nove_sule.entity.enums;

/**
 * Enum para definir los estados de una factura
 */
public enum EstadoFactura {
    PENDIENTE("Pendiente"),
    PAGADA("Pagada"),
    VENCIDA("Vencida");

    private final String descripcion;

    EstadoFactura(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
