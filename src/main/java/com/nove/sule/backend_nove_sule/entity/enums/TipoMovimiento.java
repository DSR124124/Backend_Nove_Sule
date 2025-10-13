package com.nove.sule.backend_nove_sule.entity.enums;

/**
 * Enum para definir los tipos de movimiento de inventario y caja
 */
public enum TipoMovimiento {
    ENTRADA("Entrada"),
    SALIDA("Salida"),
    TRANSFERENCIA("Transferencia"),
    AJUSTE("Ajuste"),
    INGRESO("Ingreso"),
    EGRESO("Egreso");

    private final String descripcion;

    TipoMovimiento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
