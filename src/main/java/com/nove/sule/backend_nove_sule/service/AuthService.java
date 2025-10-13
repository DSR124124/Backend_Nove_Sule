package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.auth.LoginRequestDTO;
import com.nove.sule.backend_nove_sule.dto.auth.LoginResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Usuario;

/**
 * Interfaz para el servicio de autenticación
 */
public interface AuthService {

    /**
     * Autentica un usuario con username y password
     */
    LoginResponseDTO login(LoginRequestDTO loginRequest);

    /**
     * Genera un token JWT para el usuario
     */
    String generateToken(Usuario usuario);

    /**
     * Valida un token JWT
     */
    boolean validateToken(String token);

    /**
     * Obtiene el username del token
     */
    String getUsernameFromToken(String token);

    /**
     * Verifica si el token ha expirado
     */
    boolean isTokenExpired(String token);
}
