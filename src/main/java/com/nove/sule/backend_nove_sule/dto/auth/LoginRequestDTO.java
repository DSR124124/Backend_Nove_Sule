package com.nove.sule.backend_nove_sule.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para request de login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    @NotBlank(message = "El username es requerido")
    private String username;

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}
