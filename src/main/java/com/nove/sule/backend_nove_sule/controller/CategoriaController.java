package com.nove.sule.backend_nove_sule.controller;

import com.nove.sule.backend_nove_sule.dto.catalogo.CategoriaBasicaDTO;
import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.service.CategoriaService;
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
 * Controlador para gestión de categorías
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_BASE_PATH + "/categorias")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Categorías", description = "Endpoints para gestión de categorías")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<CategoriaBasicaDTO>> crear(@Valid @RequestBody CategoriaBasicaDTO categoriaDTO) {
        try {
            CategoriaBasicaDTO nuevaCategoria = categoriaService.crear(categoriaDTO);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Categoría creada exitosamente", nuevaCategoria));
                
        } catch (Exception e) {
            log.error("Error creando categoría: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar categorías activas", description = "Lista todas las categorías activas ordenadas")
    @GetMapping("/activas")
    public ResponseEntity<ApiResponseDTO<List<CategoriaBasicaDTO>>> listarActivas() {
        List<CategoriaBasicaDTO> categorias = categoriaService.listarActivas();
        return ResponseEntity.ok(ApiResponseDTO.success(categorias));
    }

    @Operation(summary = "Listar categorías", description = "Lista categorías con paginación y filtros")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<CategoriaBasicaDTO>>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Estado estado,
            Pageable pageable) {
        
        PaginatedResponseDTO<CategoriaBasicaDTO> categorias = categoriaService.buscarConFiltros(
            nombre, estado, pageable);
        
        return ResponseEntity.ok(ApiResponseDTO.success(categorias));
    }

    @Operation(summary = "Buscar categoría por ID", description = "Obtiene una categoría por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CategoriaBasicaDTO>> buscarPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id)
            .map(categoria -> ResponseEntity.ok(ApiResponseDTO.success(categoria)))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría existente")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<CategoriaBasicaDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaBasicaDTO categoriaDTO) {
        try {
            CategoriaBasicaDTO categoriaActualizada = categoriaService.actualizar(id, categoriaDTO);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Categoría actualizada exitosamente", categoriaActualizada));
                
        } catch (Exception e) {
            log.error("Error actualizando categoría {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría (soft delete)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<String>> eliminar(@PathVariable Long id) {
        try {
            categoriaService.eliminar(id);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Categoría eliminada exitosamente"));
                
        } catch (Exception e) {
            log.error("Error eliminando categoría {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar categorías por texto", description = "Busca categorías por texto en nombre o descripción")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<CategoriaBasicaDTO>>> buscarPorTexto(@RequestParam String texto) {
        List<CategoriaBasicaDTO> categorias = categoriaService.buscarPorTexto(texto);
        return ResponseEntity.ok(ApiResponseDTO.success(categorias));
    }

    @Operation(summary = "Cambiar estado", description = "Cambia el estado de una categoría")
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<CategoriaBasicaDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Estado estado) {
        try {
            CategoriaBasicaDTO categoria = categoriaService.cambiarEstado(id, estado);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Estado cambiado exitosamente", categoria));
                
        } catch (Exception e) {
            log.error("Error cambiando estado de la categoría {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
