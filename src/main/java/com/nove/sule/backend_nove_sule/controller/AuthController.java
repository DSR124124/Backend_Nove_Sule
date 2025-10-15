package com.nove.sule.backend_nove_sule.controller;

import com.nove.sule.backend_nove_sule.dto.auth.LoginRequestDTO;
import com.nove.sule.backend_nove_sule.dto.auth.LoginResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import com.nove.sule.backend_nove_sule.dto.usuario.UsuarioDTO;
import com.nove.sule.backend_nove_sule.mapper.UsuarioMapper;
import com.nove.sule.backend_nove_sule.service.AuthService;
import com.nove.sule.backend_nove_sule.service.UsuarioService;
import com.nove.sule.backend_nove_sule.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para autenticación
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_BASE_PATH + "/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y autorización")
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("Intento de login para usuario: {}", loginRequest.getUsername());
        
        try {
            LoginResponseDTO response = authService.login(loginRequest);
            
            return ResponseEntity.ok(ApiResponseDTO.success(
                Constants.SUCCESS_LOGIN, 
                response
            ));
            
        } catch (Exception e) {
            log.error("Error en login para usuario {}: {}", loginRequest.getUsername(), e.getMessage());
            
            return ResponseEntity.badRequest().body(
                ApiResponseDTO.error(e.getMessage())
            );
        }
    }

    @Operation(summary = "Validar token", description = "Valida si un token JWT es válido")
    @PostMapping("/validate")
    public ResponseEntity<ApiResponseDTO<Boolean>> validateToken(@RequestParam String token) {
        try {
            boolean isValid = authService.validateToken(token);
            
            return ResponseEntity.ok(ApiResponseDTO.success(isValid));
            
        } catch (Exception e) {
            log.error("Error validando token: {}", e.getMessage());
            
            return ResponseEntity.badRequest().body(
                ApiResponseDTO.error("Token inválido")
            );
        }
    }

    @Operation(summary = "Cerrar sesión", description = "Invalida el token del usuario (lado cliente)")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<String>> logout() {
        // En JWT stateless, el logout es principalmente del lado del cliente
        // El cliente debe eliminar el token de su almacenamiento
        
        return ResponseEntity.ok(ApiResponseDTO.success(
            Constants.SUCCESS_LOGOUT, 
            "Sesión cerrada exitosamente"
        ));
    }

    @Operation(summary = "Obtener perfil", description = "Obtiene el perfil del usuario autenticado")
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CAJERO')")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> getProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            log.info("Obteniendo perfil para usuario: {}", username);
            
            UsuarioDTO usuario = usuarioService.buscarPorUsername(username)
                .map(usuarioMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            return ResponseEntity.ok(ApiResponseDTO.success("Perfil obtenido exitosamente", usuario));
            
        } catch (Exception e) {
            log.error("Error obteniendo perfil: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
