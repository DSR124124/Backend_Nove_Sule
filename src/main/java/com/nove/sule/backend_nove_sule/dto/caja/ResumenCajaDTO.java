package com.nove.sule.backend_nove_sule.dto.caja;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para resumen de caja
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenCajaDTO {

    private Long cajaId;
    private String cajaNombre;
    private LocalDate fecha;
    private BigDecimal saldoInicial;
    private BigDecimal saldoFinal;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal saldoDiferencia;
    private Long totalMovimientos;
    private LocalDateTime ultimoMovimiento;
    private String estadoCaja;
}
