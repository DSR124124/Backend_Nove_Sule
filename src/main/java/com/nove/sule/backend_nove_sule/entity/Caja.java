package com.nove.sule.backend_nove_sule.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad Caja
 */
@Entity
@Table(name = "cajas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caja extends BaseEntity {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @PositiveOrZero(message = "El saldo inicial debe ser positivo o cero")
    @Column(name = "saldo_inicial", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal saldoInicial = BigDecimal.ZERO;

    @PositiveOrZero(message = "El saldo actual debe ser positivo o cero")
    @Column(name = "saldo_actual", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal saldoActual = BigDecimal.ZERO;

    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;

    // Relación muchos a uno con Empleado (responsable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_id")
    private Empleado responsable;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private EstadoCaja estado = EstadoCaja.ABIERTA;

    public enum EstadoCaja {
        ABIERTA,
        CERRADA
    }
}
