package com.nove.sule.backend_nove_sule.dto.catalogo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO básico para Marca
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarcaBasicaDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String logo;
}
