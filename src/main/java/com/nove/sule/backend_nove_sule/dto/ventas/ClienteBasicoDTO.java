package com.nove.sule.backend_nove_sule.dto.ventas;

import com.nove.sule.backend_nove_sule.entity.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO básico para Cliente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteBasicoDTO {

    private Long id;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;
    private String razonSocial;
    private String email;
    private String telefono;
    private String nombreCompleto;
}
