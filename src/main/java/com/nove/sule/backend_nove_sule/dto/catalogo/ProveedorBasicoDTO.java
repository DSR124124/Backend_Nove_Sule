package com.nove.sule.backend_nove_sule.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO básico para Proveedor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorBasicoDTO {

    private Long id;
    private String nombre;
    private String razonSocial;
    private String ruc;
    private String telefono;
    private String email;
}
