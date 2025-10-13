package com.nove.sule.backend_nove_sule.entity.enums;

/**
 * Enum para definir los medios de pago
 */
public enum MedioPago {
    EFECTIVO("Efectivo"),
    TARJETA("Tarjeta"),
    TRANSFERENCIA("Transferencia Bancaria"),
    CREDITO("Crédito");

    private final String descripcion;

    MedioPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
