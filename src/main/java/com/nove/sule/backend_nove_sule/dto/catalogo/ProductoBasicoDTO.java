package com.nove.sule.backend_nove_sule.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO básico para Producto (versión simplificada para listados y referencias)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoBasicoDTO {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String imagen;
}