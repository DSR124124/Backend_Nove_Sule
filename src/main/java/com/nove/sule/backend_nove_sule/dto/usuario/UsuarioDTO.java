package com.nove.sule.backend_nove_sule.dto.usuario;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para Usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long id;
    private String username;
    private String email;
    private Rol rol;
    private Estado estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private EmpleadoBasicoDTO empleado;
}
