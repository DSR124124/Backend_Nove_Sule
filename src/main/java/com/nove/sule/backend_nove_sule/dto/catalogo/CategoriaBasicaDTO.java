package com.nove.sule.backend_nove_sule.dto.catalogo;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO básico para Categoria
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaBasicaDTO {

    private Long id;
    
    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    private String descripcion;
    
    @Size(max = 255, message = "La URL de imagen no puede exceder 255 caracteres")
    private String imagen;
    
    @Size(max = 7, message = "El color debe ser un código hex válido")
    private String color;
    
    private Integer orden;
    
    private Estado estado;
}
