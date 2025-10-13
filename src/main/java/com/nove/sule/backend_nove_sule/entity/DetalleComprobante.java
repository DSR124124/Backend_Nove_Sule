package com.nove.sule.backend_nove_sule.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidad Detalle de Comprobante de Venta
 */
@Entity
@Table(name = "detalle_comprobante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleComprobante extends BaseEntity {

    // Relación muchos a uno con ComprobanteVenta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comprobante_id", nullable = false)
    private ComprobanteVenta comprobante;

    // Relación muchos a uno con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(nullable = false, precision = 8, scale = 3)
    private BigDecimal cantidad;

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
        BigDecimal total = precioUnitario.multiply(cantidad);
        this.subtotal = total.subtract(descuento != null ? descuento : BigDecimal.ZERO);
    }
}
