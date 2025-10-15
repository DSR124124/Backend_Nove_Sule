package com.nove.sule.backend_nove_sule.dto.inventario;

import com.nove.sule.backend_nove_sule.entity.enums.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para Movimiento de Inventario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventarioDTO {

    private Long id;
    private Long productoId;
    private String productoCodigo;
    private String productoNombre;
    private TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private String concepto;
    private String observaciones;
    private String usuarioNombre;
    private Long ordenCompraId;
    private String ordenCompraNumero;
    private Long comprobanteVentaId;
    private String comprobanteVentaNumero;
    private LocalDateTime fechaMovimiento;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
