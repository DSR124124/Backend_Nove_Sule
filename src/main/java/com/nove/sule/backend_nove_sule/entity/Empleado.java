package com.nove.sule.backend_nove_sule.entity;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad Empleado
 */
@Entity
@Table(name = "empleados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empleado extends BaseEntity {

    @NotBlank(message = "Los nombres son requeridos")
    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son requeridos")
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String apellidos;

    @NotBlank(message = "El DNI es requerido")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos")
    @Column(unique = true, nullable = false, length = 8)
    private String dni;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(length = 20)
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Column(precision = 10, scale = 2)
    private BigDecimal salario;

    @Size(max = 50, message = "El cargo no puede exceder 50 caracteres")
    @Column(length = 50)
    private String cargo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Estado estado = Estado.ACTIVO;

    // Relación uno a uno con Usuario
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // Método de conveniencia para obtener el nombre completo
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
