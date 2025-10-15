package com.nove.sule.backend_nove_sule.dto.compras;

import com.nove.sule.backend_nove_sule.dto.catalogo.ProveedorBasicoDTO;
import com.nove.sule.backend_nove_sule.entity.enums.EstadoOrdenCompra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para Orden de Compra
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompraDTO {

    private Long id;
    private String numero;
    private ProveedorBasicoDTO proveedor;
    private LocalDate fechaOrden;
    private LocalDate fechaEntregaEsperada;
    private EstadoOrdenCompra estado;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal igv;
    private BigDecimal total;
    private String observaciones;
    private String usuario;
    private List<DetalleOrdenCompraDTO> detalles;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
