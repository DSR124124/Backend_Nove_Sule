package com.nove.sule.backend_nove_sule.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidad Detalle de Orden de Compra
 */
@Entity
@Table(name = "detalle_orden_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleOrdenCompra extends BaseEntity {

    // Relación muchos a uno con OrdenCompra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_compra_id", nullable = false)
    private OrdenCompra ordenCompra;

    // Relación muchos a uno con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario es requerido")
    @PositiveOrZero(message = "El precio unitario debe ser positivo o cero")
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @PositiveOrZero(message = "El descuento debe ser positivo o cero")
    @Column(precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal descuento = BigDecimal.ZERO;

    @NotNull(message = "El subtotal es requerido")
    @PositiveOrZero(message = "El subtotal debe ser positivo o cero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    // Método para calcular el subtotal
    @PrePersist
    @PreUpdate
    private void calcularSubtotal() {
        BigDecimal total = precioUnitario.multiply(new BigDecimal(cantidad));
        this.subtotal = total.subtract(descuento != null ? descuento : BigDecimal.ZERO);
    }
}
