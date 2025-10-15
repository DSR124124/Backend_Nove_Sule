package com.nove.sule.backend_nove_sule.dto.compras;

import com.nove.sule.backend_nove_sule.dto.catalogo.ProductoBasicoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para Detalle de Orden de Compra
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleOrdenCompraDTO {

    private Long id;
    private ProductoBasicoDTO producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;
}
