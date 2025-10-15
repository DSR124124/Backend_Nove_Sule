package com.nove.sule.backend_nove_sule.dto.ventas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para productos más vendidos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoVendidoDTO {

    private Long productoId;
    private String productoNombre;
    private String productoCodigo;
    private BigDecimal cantidadVendida;
    private BigDecimal totalVendido;
    private BigDecimal precioPromedio;
    private Long numeroVentas;
}
