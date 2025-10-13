package com.nove.sule.backend_nove_sule.entity.enums;

/**
 * Enum para definir los estados de una orden de compra
 */
public enum EstadoOrdenCompra {
    PENDIENTE("Pendiente"),
    APROBADA("Aprobada"),
    PARCIALMENTE_RECIBIDA("Parcialmente Recibida"),
    RECIBIDA("Recibida"),
    CANCELADA("Cancelada");

    private final String descripcion;

    EstadoOrdenCompra(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
