package com.nove.sule.backend_nove_sule.controller;

import com.nove.sule.backend_nove_sule.dto.catalogo.ProveedorBasicoDTO;
import com.nove.sule.backend_nove_sule.dto.catalogo.ProveedorRequestDTO;
import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para Proveedor
 */
@RestController
@RequestMapping("/api/v1/proveedores")
@RequiredArgsConstructor
@Tag(name = "Proveedor", description = "API para gestión de proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @Operation(summary = "Crear proveedor", description = "Crea un nuevo proveedor")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<ProveedorBasicoDTO>> crear(@Valid @RequestBody ProveedorRequestDTO proveedorRequest) {
        try {
            ProveedorBasicoDTO proveedor = proveedorService.crear(proveedorRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Proveedor creado exitosamente", proveedor));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar proveedor", description = "Actualiza un proveedor existente")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<ProveedorBasicoDTO>> actualizar(
            @PathVariable Long id, 
            @Valid @RequestBody ProveedorRequestDTO proveedorRequest) {
        try {
            ProveedorBasicoDTO proveedor = proveedorService.actualizar(id, proveedorRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Proveedor actualizado exitosamente", proveedor));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar proveedor por ID", description = "Obtiene un proveedor por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProveedorBasicoDTO>> buscarPorId(@PathVariable Long id) {
        try {
            return proveedorService.buscarPorId(id)
                .map(proveedor -> ResponseEntity.ok(ApiResponseDTO.success(proveedor)))
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar proveedores", description = "Lista todos los proveedores con paginación")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<ProveedorBasicoDTO>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            PaginatedResponseDTO<ProveedorBasicoDTO> proveedores = proveedorService.listarTodos(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(proveedores));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar proveedores activos", description = "Lista todos los proveedores activos")
    @GetMapping("/activos")
    public ResponseEntity<ApiResponseDTO<List<ProveedorBasicoDTO>>> listarActivos() {
        try {
            List<ProveedorBasicoDTO> proveedores = proveedorService.listarActivos();
            return ResponseEntity.ok(ApiResponseDTO.success(proveedores));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar proveedores con filtros", description = "Busca proveedores con filtros y paginación")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<ProveedorBasicoDTO>>> buscarConFiltros(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String ruc,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Estado estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            PaginatedResponseDTO<ProveedorBasicoDTO> proveedores = proveedorService.buscarConFiltros(nombre, ruc, email, estado, pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(proveedores));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor (soft delete)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<String>> eliminar(@PathVariable Long id) {
        try {
            proveedorService.eliminar(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Proveedor eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Cambiar estado", description = "Cambia el estado de un proveedor")
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<ProveedorBasicoDTO>> cambiarEstado(
            @PathVariable Long id, 
            @RequestParam Estado estado) {
        try {
            ProveedorBasicoDTO proveedor = proveedorService.cambiarEstado(id, estado);
            return ResponseEntity.ok(ApiResponseDTO.success("Estado actualizado exitosamente", proveedor));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
