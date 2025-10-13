package com.nove.sule.backend_nove_sule.entity;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.MedioPago;
import com.nove.sule.backend_nove_sule.entity.enums.Moneda;
import com.nove.sule.backend_nove_sule.entity.enums.TipoComprobante;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Comprobante de Venta
 */
@Entity
@Table(name = "comprobantes_venta",
       uniqueConstraints = @UniqueConstraint(columnNames = {"tipo_comprobante", "serie", "numero"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobanteVenta extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comprobante", nullable = false, length = 20)
    private TipoComprobante tipoComprobante;

    @NotBlank(message = "La serie es requerida")
    @Size(min = 4, max = 4, message = "La serie debe tener 4 caracteres")
    @Column(nullable = false, length = 4)
    private String serie;

    @NotBlank(message = "El número es requerido")
    @Size(max = 8, message = "El número no puede exceder 8 caracteres")
    @Column(nullable = false, length = 8)
    private String numero;

    // Relación muchos a uno con Cliente (opcional para boletas)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "fecha_emision", nullable = false)
    @Builder.Default
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(length = 5)
    @Builder.Default
    private Moneda moneda = Moneda.PEN;

    @PositiveOrZero(message = "El tipo de cambio debe ser positivo")
    @Column(name = "tipo_cambio", precision = 6, scale = 4)
    @Builder.Default
    private BigDecimal tipoCambio = BigDecimal.ONE;

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

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Estado estado = Estado.ACTIVO;

    @Enumerated(EnumType.STRING)
    @Column(name = "medio_pago", nullable = false, length = 20)
    private MedioPago medioPago;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // Relación muchos a uno con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación muchos a uno con Caja
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caja_id")
    private Caja caja;

    // Relación uno a muchos con DetalleComprobante
    @OneToMany(mappedBy = "comprobante", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<DetalleComprobante> detalles = new ArrayList<>();

    // Método para calcular totales
    public void calcularTotales() {
        BigDecimal subtotalCalculado = detalles.stream()
                .map(DetalleComprobante::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.subtotal = subtotalCalculado;
        BigDecimal subtotalConDescuento = subtotalCalculado.subtract(descuento != null ? descuento : BigDecimal.ZERO);
        this.igv = subtotalConDescuento.multiply(new BigDecimal("0.18"));
        this.total = subtotalConDescuento.add(igv);
    }

    // Método para obtener el número completo del comprobante
    public String getNumeroCompleto() {
        return serie + "-" + numero;
    }
}
