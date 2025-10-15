package com.nove.sule.backend_nove_sule.dto.caja;

import com.nove.sule.backend_nove_sule.entity.enums.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para Movimiento de Caja
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoCajaDTO {

    private Long id;
    private Long cajaId;
    private String cajaNombre;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal monto;
    private String concepto;
    private String observaciones;
    private String usuario;
    private LocalDateTime fechaMovimiento;
    private BigDecimal saldoAnterior;
    private BigDecimal saldoNuevo;
}
