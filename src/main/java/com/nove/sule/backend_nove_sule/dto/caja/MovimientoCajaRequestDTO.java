package com.nove.sule.backend_nove_sule.dto.caja;

import com.nove.sule.backend_nove_sule.entity.enums.TipoMovimiento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para crear Movimiento de Caja
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoCajaRequestDTO {

    @NotNull(message = "La caja es requerida")
    private Long cajaId;

    @NotNull(message = "El tipo de movimiento es requerido")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "El monto es requerido")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "El concepto es requerido")
    @Size(max = 100, message = "El concepto no puede exceder 100 caracteres")
    private String concepto;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
}
