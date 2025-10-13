package com.nove.sule.backend_nove_sule.entity;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoCuenta;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Proveedor
 */
@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor extends BaseEntity {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Size(max = 150, message = "La razón social no puede exceder 150 caracteres")
    @Column(name = "razon_social", length = 150)
    private String razonSocial;

    @NotBlank(message = "El RUC es requerido")
    @Size(min = 11, max = 11, message = "El RUC debe tener 11 dígitos")
    @Column(unique = true, nullable = false, length = 11)
    private String ruc;

    @NotBlank(message = "La dirección es requerida")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String direccion;

    @NotBlank(message = "El distrito es requerido")
    @Size(max = 50, message = "El distrito no puede exceder 50 caracteres")
    @Column(nullable = false, length = 50)
    private String distrito;

    @NotBlank(message = "La provincia es requerida")
    @Size(max = 50, message = "La provincia no puede exceder 50 caracteres")
    @Column(nullable = false, length = 50)
    private String provincia;

    @NotBlank(message = "El departamento es requerido")
    @Size(max = 50, message = "El departamento no puede exceder 50 caracteres")
    @Column(nullable = false, length = 50)
    private String departamento;

    @Size(max = 10, message = "El código postal no puede exceder 10 caracteres")
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;

    @NotBlank(message = "El teléfono es requerido")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(nullable = false, length = 20)
    private String telefono;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String email;

    @Size(max = 255, message = "La URL del sitio web no puede exceder 255 caracteres")
    @Column(name = "sitio_web", length = 255)
    private String sitioWeb;

    @NotBlank(message = "El contacto es requerido")
    @Size(max = 100, message = "El contacto no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String contacto;

    @Size(max = 50, message = "El cargo no puede exceder 50 caracteres")
    @Column(length = 50)
    private String cargo;

    // Información bancaria
    @Size(max = 50, message = "El banco no puede exceder 50 caracteres")
    @Column(length = 50)
    private String banco;

    @Size(max = 20, message = "El número de cuenta no puede exceder 20 caracteres")
    @Column(name = "numero_cuenta", length = 20)
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", length = 20)
    private TipoCuenta tipoCuenta;

    // Términos comerciales
    @Column(name = "plazo_pago")
    @Builder.Default
    private Integer plazoPago = 30;

    @Column(precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "limite_credito", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal limiteCredito = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Estado estado = Estado.ACTIVO;

    // Relación uno a muchos con Producto
    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();
}
