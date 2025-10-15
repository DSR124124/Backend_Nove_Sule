package com.nove.sule.backend_nove_sule.dto.compras;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para resumen de compras
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenComprasDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Long totalOrdenes;
    private BigDecimal totalCompras;
    private BigDecimal totalSubtotal;
    private BigDecimal totalIgv;
    private BigDecimal totalDescuentos;
    private Long totalProductosComprados;
    private BigDecimal compraPromedio;
    private Long ordenesPendientes;
    private Long ordenesEntregadas;
    private Long ordenesCanceladas;
}
