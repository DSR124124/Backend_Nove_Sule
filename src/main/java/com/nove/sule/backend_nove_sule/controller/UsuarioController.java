package com.nove.sule.backend_nove_sule.controller;

import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.dto.usuario.UsuarioDTO;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.Rol;
import com.nove.sule.backend_nove_sule.service.UsuarioService;
import com.nove.sule.backend_nove_sule.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para gestión de usuarios
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_BASE_PATH + "/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> crear(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO nuevoUsuario = usuarioService.crear(usuarioDTO);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Usuario creado exitosamente", nuevoUsuario));
                
        } catch (Exception e) {
            log.error("Error creando usuario: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario existente")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO usuarioActualizado = usuarioService.actualizar(id, usuarioDTO);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Usuario actualizado exitosamente", usuarioActualizado));
                
        } catch (Exception e) {
            log.error("Error actualizando usuario {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar usuario por ID", description = "Obtiene un usuario por su ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
            .map(usuario -> ResponseEntity.ok(ApiResponseDTO.success(usuario)))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar usuarios", description = "Lista usuarios con paginación y filtros")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<UsuarioDTO>>> listar(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Rol rol,
            @RequestParam(required = false) Estado estado,
            Pageable pageable) {
        
        PaginatedResponseDTO<UsuarioDTO> usuarios = usuarioService.buscarConFiltros(
            username, email, rol, estado, pageable);
        
        return ResponseEntity.ok(ApiResponseDTO.success(usuarios));
    }

    @Operation(summary = "Listar por rol", description = "Lista usuarios por rol específico")
    @GetMapping("/por-rol/{rol}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<List<UsuarioDTO>>> listarPorRol(@PathVariable Rol rol) {
        List<UsuarioDTO> usuarios = usuarioService.listarPorRol(rol);
        return ResponseEntity.ok(ApiResponseDTO.success(usuarios));
    }

    @Operation(summary = "Cambiar estado", description = "Cambia el estado de un usuario")
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Estado estado) {
        try {
            UsuarioDTO usuario = usuarioService.cambiarEstado(id, estado);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Estado cambiado exitosamente", usuario));
                
        } catch (Exception e) {
            log.error("Error cambiando estado del usuario {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Cambiar contraseña", description = "Cambia la contraseña de un usuario")
    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<String>> cambiarPassword(
            @PathVariable Long id,
            @RequestParam String nuevaPassword) {
        try {
            usuarioService.cambiarPassword(id, nuevaPassword);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Contraseña cambiada exitosamente"));
                
        } catch (Exception e) {
            log.error("Error cambiando contraseña del usuario {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario (soft delete)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<String>> eliminar(@PathVariable Long id) {
        try {
            usuarioService.eliminar(id);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Usuario eliminado exitosamente"));
                
        } catch (Exception e) {
            log.error("Error eliminando usuario {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Verificar username", description = "Verifica si un username ya existe")
    @GetMapping("/verificar-username")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarUsername(@RequestParam String username) {
        boolean existe = usuarioService.existeUsername(username);
        return ResponseEntity.ok(ApiResponseDTO.success(existe));
    }

    @Operation(summary = "Verificar email", description = "Verifica si un email ya existe")
    @GetMapping("/verificar-email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarEmail(@RequestParam String email) {
        boolean existe = usuarioService.existeEmail(email);
        return ResponseEntity.ok(ApiResponseDTO.success(existe));
    }
}
