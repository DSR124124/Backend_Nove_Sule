package com.nove.sule.backend_nove_sule.exception;

import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseEntity.badRequest()
            .body(ApiResponseDTO.<Map<String, String>>builder()
                .success(false)
                .error("Errores de validación")
                .data(errors)
                .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleBadCredentials(BadCredentialsException ex) {
        log.error("Credenciales inválidas: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponseDTO.error("Credenciales inválidas", HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleAccessDenied(AccessDeniedException ex) {
        log.error("Acceso denegado: {}", ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponseDTO.error("Acceso denegado", HttpStatus.FORBIDDEN.value()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("Error de aplicación: {}", ex.getMessage());
        
        return ResponseEntity.badRequest()
            .body(ApiResponseDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleGenericException(Exception ex) {
        log.error("Error interno del servidor: {}", ex.getMessage(), ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponseDTO.error("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
