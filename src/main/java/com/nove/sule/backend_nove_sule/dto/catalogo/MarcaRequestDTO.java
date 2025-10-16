package com.nove.sule.backend_nove_sule.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear/actualizar Marca
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarcaRequestDTO {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Size(max = 255, message = "La URL del logo no puede exceder 255 caracteres")
    private String logo;

    @Size(max = 255, message = "La URL del sitio web no puede exceder 255 caracteres")
    private String sitioWeb;

    @Size(max = 100, message = "El contacto no puede exceder 100 caracteres")
    private String contacto;

    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
}
