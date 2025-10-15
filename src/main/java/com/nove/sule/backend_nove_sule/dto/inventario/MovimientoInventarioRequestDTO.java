package com.nove.sule.backend_nove_sule.dto.inventario;

import com.nove.sule.backend_nove_sule.entity.enums.TipoMovimiento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para crear Movimiento de Inventario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventarioRequestDTO {

    @NotNull(message = "El producto es requerido")
    private Long productoId;

    @NotNull(message = "El tipo de movimiento es requerido")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @DecimalMin(value = "0.0", message = "El precio unitario debe ser mayor o igual a 0")
    private BigDecimal precioUnitario;

    @NotBlank(message = "El concepto es requerido")
    @Size(max = 100, message = "El concepto no puede exceder 100 caracteres")
    private String concepto;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    private Long ordenCompraId;

    private Long comprobanteVentaId;

    private LocalDateTime fechaMovimiento;
}
