package com.nove.sule.backend_nove_sule.entity.enums;

/**
 * Enum para definir los tipos de comprobante de venta
 */
public enum TipoComprobante {
    BOLETA("Boleta de Venta"),
    FACTURA("Factura"),
    NOTA_CREDITO("Nota de Crédito"),
    NOTA_DEBITO("Nota de Débito");

    private final String descripcion;

    TipoComprobante(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
