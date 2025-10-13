package com.nove.sule.backend_nove_sule.entity;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Marca
 */
@Entity
@Table(name = "marcas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marca extends BaseEntity {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Size(max = 255, message = "La URL del logo no puede exceder 255 caracteres")
    @Column(length = 255)
    private String logo;

    @Size(max = 255, message = "La URL del sitio web no puede exceder 255 caracteres")
    @Column(name = "sitio_web", length = 255)
    private String sitioWeb;

    @Size(max = 100, message = "El contacto no puede exceder 100 caracteres")
    @Column(length = 100)
    private String contacto;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(length = 100)
    private String email;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Estado estado = Estado.ACTIVO;

    // Relación uno a muchos con Producto
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Producto> productos = new ArrayList<>();
}
