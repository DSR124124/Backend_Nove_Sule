package com.nove.sule.backend_nove_sule.entity;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoDocumento;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad Cliente
 */
@Entity
@Table(name = "clientes",
       uniqueConstraints = @UniqueConstraint(columnNames = {"tipo_documento", "numero_documento"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private TipoDocumento tipoDocumento;

    @NotBlank(message = "El número de documento es requerido")
    @Size(max = 15, message = "El número de documento no puede exceder 15 caracteres")
    @Column(name = "numero_documento", nullable = false, length = 15)
    private String numeroDocumento;

    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    @Column(length = 100)
    private String nombres;

    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    @Column(length = 100)
    private String apellidos;

    @Size(max = 150, message = "La razón social no puede exceder 150 caracteres")
    @Column(name = "razon_social", length = 150)
    private String razonSocial;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(length = 100)
    private String email;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(length = 20)
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String direccion;

    @Size(max = 50, message = "El distrito no puede exceder 50 caracteres")
    @Column(length = 50)
    private String distrito;

    @Size(max = 50, message = "La provincia no puede exceder 50 caracteres")
    @Column(length = 50)
    private String provincia;

    @Size(max = 50, message = "El departamento no puede exceder 50 caracteres")
    @Column(length = 50)
    private String departamento;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @PositiveOrZero(message = "El límite de crédito debe ser positivo o cero")
    @Column(name = "limite_credito", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal limiteCredito = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Estado estado = Estado.ACTIVO;

    // Método de conveniencia para obtener el nombre completo
    public String getNombreCompleto() {
        if (tipoDocumento == TipoDocumento.RUC) {
            return razonSocial != null ? razonSocial : "Sin Razón Social";
        }
        return (nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : "");
    }

    // Método para validar si es persona natural o jurídica
    public boolean esPersonaJuridica() {
        return tipoDocumento == TipoDocumento.RUC;
    }
}
