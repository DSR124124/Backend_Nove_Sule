package com.nove.sule.backend_nove_sule.entity.enums;

/**
 * Enum para definir los roles de usuario en el sistema
 */
public enum Rol {
    ADMIN("Administrador"),
    GERENTE("Gerente"),
    VENDEDOR("Vendedor"),
    CAJERO("Cajero"),
    ALMACENERO("Almacenero");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
