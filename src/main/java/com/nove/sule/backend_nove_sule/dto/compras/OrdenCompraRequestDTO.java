package com.nove.sule.backend_nove_sule.dto.compras;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para crear Orden de Compra
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompraRequestDTO {

    @NotNull(message = "El proveedor es requerido")
    private Long proveedorId;

    private LocalDate fechaOrden;

    private LocalDate fechaEntregaEsperada;

    @DecimalMin(value = "0.0", message = "El descuento debe ser mayor o igual a 0")
    private BigDecimal descuento;

    private String observaciones;

    @NotEmpty(message = "Debe incluir al menos un detalle")
    @Valid
    private List<DetalleOrdenCompraRequestDTO> detalles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetalleOrdenCompraRequestDTO {
        
        @NotNull(message = "El producto es requerido")
        private Long productoId;

        @NotNull(message = "La cantidad es requerida")
        @Positive(message = "La cantidad debe ser mayor a 0")
        private Integer cantidad;

        @NotNull(message = "El precio unitario es requerido")
        @DecimalMin(value = "0.0", message = "El precio unitario debe ser mayor o igual a 0")
        private BigDecimal precioUnitario;

        @DecimalMin(value = "0.0", message = "El descuento debe ser mayor o igual a 0")
        private BigDecimal descuento;
    }
}
