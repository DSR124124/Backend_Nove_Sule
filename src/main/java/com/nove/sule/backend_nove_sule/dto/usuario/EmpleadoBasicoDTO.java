package com.nove.sule.backend_nove_sule.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO básico para Empleado (para evitar referencias circulares)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoBasicoDTO {

    private Long id;
    private String nombres;
    private String apellidos;
    private String dni;
    private String cargo;
    private String nombreCompleto;
}
