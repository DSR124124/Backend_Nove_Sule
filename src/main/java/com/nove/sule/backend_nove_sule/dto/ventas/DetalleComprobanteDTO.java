package com.nove.sule.backend_nove_sule.dto.ventas;

import com.nove.sule.backend_nove_sule.dto.catalogo.ProductoBasicoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para Detalle de Comprobante de Venta
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleComprobanteDTO {

    private Long id;
    private ProductoBasicoDTO producto;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuento;
    private BigDecimal subtotal;
    private String observaciones;
}
