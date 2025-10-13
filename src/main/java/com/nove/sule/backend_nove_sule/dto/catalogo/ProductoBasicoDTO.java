package com.nove.sule.backend_nove_sule.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO básico para Producto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoBasicoDTO {

    private Long id;
    private String nombre;
    private String codigo;
    private BigDecimal precio;
    private Integer stock;
    private String unidad;
    private String imagen;
}
