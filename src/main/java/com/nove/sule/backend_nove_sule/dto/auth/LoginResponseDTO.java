package com.nove.sule.backend_nove_sule.dto.auth;

import com.nove.sule.backend_nove_sule.entity.enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para response de login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String token;
    private String type;
    private String username;
    private String email;
    private Rol rol;
    private String nombreCompleto;
}
