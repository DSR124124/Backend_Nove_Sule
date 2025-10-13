package com.nove.sule.backend_nove_sule.entity;

import com.nove.sule.backend_nove_sule.entity.enums.EstadoOrdenCompra;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Orden de Compra
 */
@Entity
@Table(name = "ordenes_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompra extends BaseEntity {

    @NotBlank(message = "El número de orden es requerido")
    @Size(max = 20, message = "El número de orden no puede exceder 20 caracteres")
    @Column(unique = true, nullable = false, length = 20)
    private String numero;

    // Relación muchos a uno con Proveedor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @NotNull(message = "La fecha de orden es requerida")
    @Column(name = "fecha_orden", nullable = false)
    private LocalDate fechaOrden;

    @Column(name = "fecha_entrega_esperada")
    private LocalDate fechaEntregaEsperada;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    @Builder.Default
    private EstadoOrdenCompra estado = EstadoOrdenCompra.PENDIENTE;

    @NotNull(message = "El subtotal es requerido")
    @PositiveOrZero(message = "El subtotal debe ser positivo o cero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @PositiveOrZero(message = "El descuento debe ser positivo o cero")
    @Column(precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal descuento = BigDecimal.ZERO;

    @NotNull(message = "El IGV es requerido")
    @PositiveOrZero(message = "El IGV debe ser positivo o cero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal igv;

    @NotNull(message = "El total es requerido")
    @PositiveOrZero(message = "El total debe ser positivo o cero")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // Relación muchos a uno con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación uno a muchos con DetalleOrdenCompra
    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<DetalleOrdenCompra> detalles = new ArrayList<>();

    // Método para calcular totales
    public void calcularTotales() {
        BigDecimal subtotalCalculado = detalles.stream()
                .map(DetalleOrdenCompra::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.subtotal = subtotalCalculado;
        BigDecimal subtotalConDescuento = subtotalCalculado.subtract(descuento != null ? descuento : BigDecimal.ZERO);
        this.igv = subtotalConDescuento.multiply(new BigDecimal("0.18"));
        this.total = subtotalConDescuento.add(igv);
    }
}
