package com.nove.sule.backend_nove_sule.dto.inventario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para productos con stock bajo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockBajoDTO {

    private Long productoId;
    private String productoNombre;
    private String productoCodigo;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer diferencia;
    private BigDecimal precioCompra;
    private BigDecimal valorReposicion;
    private String proveedorNombre;
    private String categoriaNombre;
    private String ubicacion;
}
