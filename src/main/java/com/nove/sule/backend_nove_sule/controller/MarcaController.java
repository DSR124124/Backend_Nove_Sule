package com.nove.sule.backend_nove_sule.controller;

import com.nove.sule.backend_nove_sule.dto.catalogo.MarcaBasicaDTO;
import com.nove.sule.backend_nove_sule.dto.catalogo.MarcaRequestDTO;
import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.service.MarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para Marca
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/marcas")
@RequiredArgsConstructor
@Tag(name = "Marca", description = "API para gestión de marcas")
public class MarcaController {

    private final MarcaService marcaService;

    @Operation(summary = "Crear marca", description = "Crea una nueva marca")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<MarcaBasicaDTO>> crear(@Valid @RequestBody MarcaRequestDTO marcaRequest) {
        try {
            MarcaBasicaDTO marca = marcaService.crear(marcaRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Marca creada exitosamente", marca));
        } catch (Exception e) {
            log.error("Error creando marca: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar marca", description = "Actualiza una marca existente")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<MarcaBasicaDTO>> actualizar(
            @PathVariable Long id, 
            @Valid @RequestBody MarcaRequestDTO marcaRequest) {
        try {
            MarcaBasicaDTO marca = marcaService.actualizar(id, marcaRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Marca actualizada exitosamente", marca));
        } catch (Exception e) {
            log.error("Error actualizando marca {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar marca por ID", description = "Obtiene una marca por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MarcaBasicaDTO>> buscarPorId(@PathVariable Long id) {
        try {
            return marcaService.buscarPorId(id)
                .map(marca -> ResponseEntity.ok(ApiResponseDTO.success(marca)))
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error buscando marca {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar marcas", description = "Lista todas las marcas con paginación")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<MarcaBasicaDTO>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            PaginatedResponseDTO<MarcaBasicaDTO> marcas = marcaService.listarTodos(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(marcas));
        } catch (Exception e) {
            log.error("Error listando marcas: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar marcas activas", description = "Lista todas las marcas activas")
    @GetMapping("/activas")
    public ResponseEntity<ApiResponseDTO<List<MarcaBasicaDTO>>> listarActivas() {
        try {
            List<MarcaBasicaDTO> marcas = marcaService.listarActivas();
            return ResponseEntity.ok(ApiResponseDTO.success(marcas));
        } catch (Exception e) {
            log.error("Error listando marcas activas: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar marcas con filtros", description = "Busca marcas con filtros y paginación")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<MarcaBasicaDTO>>> buscarConFiltros(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Estado estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            PaginatedResponseDTO<MarcaBasicaDTO> marcas = marcaService.buscarConFiltros(nombre, estado, pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(marcas));
        } catch (Exception e) {
            log.error("Error buscando marcas con filtros: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar marca", description = "Elimina una marca (soft delete)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<String>> eliminar(@PathVariable Long id) {
        try {
            marcaService.eliminar(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Marca eliminada exitosamente"));
        } catch (Exception e) {
            log.error("Error eliminando marca {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Cambiar estado", description = "Cambia el estado de una marca")
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<MarcaBasicaDTO>> cambiarEstado(
            @PathVariable Long id, 
            @RequestParam Estado estado) {
        try {
            MarcaBasicaDTO marca = marcaService.cambiarEstado(id, estado);
            return ResponseEntity.ok(ApiResponseDTO.success("Estado actualizado exitosamente", marca));
        } catch (Exception e) {
            log.error("Error cambiando estado de marca {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
