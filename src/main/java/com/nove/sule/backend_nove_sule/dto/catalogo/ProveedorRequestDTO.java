package com.nove.sule.backend_nove_sule.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * DTO para crear/actualizar Proveedor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorRequestDTO {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 150, message = "La razón social no puede exceder 150 caracteres")
    private String razonSocial;

    @NotBlank(message = "El RUC es requerido")
    @Size(min = 11, max = 11, message = "El RUC debe tener 11 dígitos")
    private String ruc;

    @NotBlank(message = "La dirección es requerida")
    private String direccion;

    @NotBlank(message = "El distrito es requerido")
    @Size(max = 50, message = "El distrito no puede exceder 50 caracteres")
    private String distrito;

    @NotBlank(message = "La provincia es requerida")
    @Size(max = 50, message = "La provincia no puede exceder 50 caracteres")
    private String provincia;

    @NotBlank(message = "El departamento es requerido")
    @Size(max = 50, message = "El departamento no puede exceder 50 caracteres")
    private String departamento;

    @Size(max = 10, message = "El código postal no puede exceder 10 caracteres")
    private String codigoPostal;

    @NotBlank(message = "El teléfono es requerido")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    @Size(max = 255, message = "La URL del sitio web no puede exceder 255 caracteres")
    private String sitioWeb;

    @NotBlank(message = "El contacto es requerido")
    @Size(max = 100, message = "El contacto no puede exceder 100 caracteres")
    private String contacto;

    @Size(max = 50, message = "El cargo no puede exceder 50 caracteres")
    private String cargo;

    // Información bancaria
    @Size(max = 50, message = "El banco no puede exceder 50 caracteres")
    private String banco;

    @Size(max = 20, message = "El número de cuenta no puede exceder 20 caracteres")
    private String numeroCuenta;

    private String tipoCuenta;

    // Términos comerciales
    private Integer plazoPago;

    private BigDecimal descuento;

    private BigDecimal limiteCredito;

    private String observaciones;
}
