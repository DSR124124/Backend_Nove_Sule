package com.nove.sule.backend_nove_sule.dto.cliente;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoDocumento;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO completo para Cliente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long id;

    @NotBlank(message = "El tipo de documento es requerido")
    private TipoDocumento tipoDocumento;

    @NotBlank(message = "El número de documento es requerido")
    @Size(max = 15, message = "El número de documento no puede exceder 15 caracteres")
    private String numeroDocumento;

    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    private String nombres;

    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    private String apellidos;

    @Size(max = 150, message = "La razón social no puede exceder 150 caracteres")
    private String razonSocial;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    private String direccion;

    private LocalDate fechaNacimiento;

    @PositiveOrZero(message = "El límite de crédito debe ser mayor o igual a 0")
    private BigDecimal limiteCredito;

    private Estado estado;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    // Método de conveniencia para obtener el nombre completo
    public String getNombreCompleto() {
        if (tipoDocumento == TipoDocumento.RUC) {
            return razonSocial != null ? razonSocial : "Sin razón social";
        } else {
            StringBuilder nombre = new StringBuilder();
            if (nombres != null) nombre.append(nombres);
            if (apellidos != null) {
                if (nombre.length() > 0) nombre.append(" ");
                nombre.append(apellidos);
            }
            return nombre.length() > 0 ? nombre.toString() : "Sin nombre";
        }
    }

    // Método para verificar si es persona jurídica
    public boolean esPersonaJuridica() {
        return tipoDocumento == TipoDocumento.RUC;
    }

    // Método para verificar si es persona natural
    public boolean esPersonaNatural() {
        return tipoDocumento == TipoDocumento.DNI;
    }
}
