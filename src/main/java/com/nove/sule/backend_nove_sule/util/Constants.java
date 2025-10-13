package com.nove.sule.backend_nove_sule.util;

/**
 * Clase de constantes para la aplicación
 */
public final class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    // JWT Constants
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER_NAME = "Authorization";
    
    // API Constants
    public static final String API_BASE_PATH = "/api/v1";
    
    // Role Constants
    public static final String ROLE_PREFIX = "ROLE_";
    
    // Date Patterns
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
    
    // Error Messages
    public static final String ERROR_USUARIO_NO_ENCONTRADO = "Usuario no encontrado";
    public static final String ERROR_CREDENCIALES_INVALIDAS = "Credenciales inválidas";
    public static final String ERROR_TOKEN_INVALIDO = "Token inválido";
    public static final String ERROR_ACCESO_DENEGADO = "Acceso denegado";
    
    // Success Messages
    public static final String SUCCESS_LOGIN = "Login exitoso";
    public static final String SUCCESS_LOGOUT = "Logout exitoso";
    public static final String SUCCESS_REGISTRO = "Usuario registrado exitosamente";
}
