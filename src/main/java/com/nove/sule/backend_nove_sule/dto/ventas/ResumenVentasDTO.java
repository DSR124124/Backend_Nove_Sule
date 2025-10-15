package com.nove.sule.backend_nove_sule.dto.ventas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para resumen de ventas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenVentasDTO {

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Long totalComprobantes;
    private BigDecimal totalVentas;
    private BigDecimal totalSubtotal;
    private BigDecimal totalIgv;
    private BigDecimal totalDescuentos;
    private Long totalProductosVendidos;
    private BigDecimal ticketPromedio;
    private Long comprobantesAnulados;
    private BigDecimal ventasAnuladas;
}
