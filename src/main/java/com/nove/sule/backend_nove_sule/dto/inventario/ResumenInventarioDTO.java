package com.nove.sule.backend_nove_sule.dto.inventario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para resumen de inventario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenInventarioDTO {

    private Long productoId;
    private String productoNombre;
    private String productoCodigo;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private BigDecimal valorInventario;
    private Integer totalEntradas;
    private Integer totalSalidas;
    private LocalDate fechaUltimoMovimiento;
    private LocalDateTime ultimoMovimiento;
    private String estadoStock; // "NORMAL", "BAJO", "SOBRE_STOCK"
    private BigDecimal precioPromedio;
    private Integer movimientosMes;
}
