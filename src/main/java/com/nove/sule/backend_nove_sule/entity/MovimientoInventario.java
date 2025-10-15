package com.nove.sule.backend_nove_sule.entity;

import com.nove.sule.backend_nove_sule.entity.enums.TipoMovimiento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Movimiento de Inventario
 */
@Entity
@Table(name = "movimientos_inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoInventario extends BaseEntity {

    // Relación muchos a uno con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @NotNull(message = "El tipo de movimiento es requerido")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Column(nullable = false)
    private Integer cantidad;

    @PositiveOrZero(message = "El precio unitario debe ser positivo o cero")
    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @NotBlank(message = "El concepto es requerido")
    @Size(max = 100, message = "El concepto no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String concepto;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // Relación muchos a uno con Usuario (quien registra el movimiento)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación muchos a uno con OrdenCompra (opcional, para movimientos de compra)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_compra_id")
    private OrdenCompra ordenCompra;

    // Relación muchos a uno con ComprobanteVenta (opcional, para movimientos de venta)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comprobante_venta_id")
    private ComprobanteVenta comprobanteVenta;

    @NotNull(message = "La fecha del movimiento es requerida")
    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;

    // Stock antes del movimiento
    @NotNull(message = "El stock anterior es requerido")
    @PositiveOrZero(message = "El stock anterior debe ser positivo o cero")
    @Column(name = "stock_anterior", nullable = false)
    private Integer stockAnterior;

    // Stock después del movimiento
    @NotNull(message = "El stock nuevo es requerido")
    @PositiveOrZero(message = "El stock nuevo debe ser positivo o cero")
    @Column(name = "stock_nuevo", nullable = false)
    private Integer stockNuevo;

    // Método para calcular el stock nuevo basado en el tipo de movimiento
    public void calcularStockNuevo() {
        if (tipoMovimiento == TipoMovimiento.ENTRADA || tipoMovimiento == TipoMovimiento.AJUSTE) {
            this.stockNuevo = this.stockAnterior + this.cantidad;
        } else if (tipoMovimiento == TipoMovimiento.SALIDA) {
            this.stockNuevo = this.stockAnterior - this.cantidad;
        } else if (tipoMovimiento == TipoMovimiento.TRANSFERENCIA) {
            // Para transferencias, el stock se maneja en dos movimientos separados
            this.stockNuevo = this.stockAnterior - this.cantidad;
        }
    }
}
