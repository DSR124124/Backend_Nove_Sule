package com.nove.sule.backend_nove_sule.controller;

import com.nove.sule.backend_nove_sule.dto.cliente.*;
import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoDocumento;
import com.nove.sule.backend_nove_sule.service.ClienteService;
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
 * Controlador para gestión de clientes
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_BASE_PATH + "/clientes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Clientes", description = "Endpoints para gestión de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Crear cliente", description = "Crea un nuevo cliente en el sistema")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR')")
    public ResponseEntity<ApiResponseDTO<ClienteDTO>> crear(@Valid @RequestBody ClienteRequestDTO clienteRequest) {
        try {
            ClienteDTO nuevoCliente = clienteService.crear(clienteRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Cliente creado exitosamente", nuevoCliente));
                
        } catch (Exception e) {
            log.error("Error creando cliente: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza un cliente existente")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR')")
    public ResponseEntity<ApiResponseDTO<ClienteDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO clienteRequest) {
        try {
            ClienteDTO clienteActualizado = clienteService.actualizar(id, clienteRequest);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Cliente actualizado exitosamente", clienteActualizado));
                
        } catch (Exception e) {
            log.error("Error actualizando cliente {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar cliente por ID", description = "Busca un cliente por su ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<ClienteDTO>> buscarPorId(@PathVariable Long id) {
        try {
            return clienteService.buscarPorId(id)
                .map(cliente -> ResponseEntity.ok(ApiResponseDTO.<ClienteDTO>success(cliente)))
                .orElse(ResponseEntity.notFound().build());
                
        } catch (Exception e) {
            log.error("Error buscando cliente {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar cliente por documento", description = "Busca un cliente por tipo y número de documento")
    @GetMapping("/buscar-documento")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<ClienteDTO>> buscarPorDocumento(
            @RequestParam TipoDocumento tipoDocumento,
            @RequestParam String numeroDocumento) {
        try {
            return clienteService.buscarPorTipoYNumeroDocumento(tipoDocumento, numeroDocumento)
                .map(cliente -> ResponseEntity.ok(ApiResponseDTO.<ClienteDTO>success(clienteService.buscarPorId(cliente.getId()).orElse(null))))
                .orElse(ResponseEntity.notFound().build());
                
        } catch (Exception e) {
            log.error("Error buscando cliente por documento {} {}: {}", tipoDocumento, numeroDocumento, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar clientes", description = "Lista todos los clientes con paginación")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<ClienteDTO>>> listarClientes(Pageable pageable) {
        try {
            PaginatedResponseDTO<ClienteDTO> clientes = clienteService.listarTodos(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(clientes));
                
        } catch (Exception e) {
            log.error("Error listando clientes: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar clientes por estado", description = "Lista clientes filtrados por estado")
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<ClienteDTO>>> listarPorEstado(@PathVariable Estado estado) {
        try {
            List<ClienteDTO> clientes = clienteService.listarPorEstado(estado);
            return ResponseEntity.ok(ApiResponseDTO.success(clientes));
                
        } catch (Exception e) {
            log.error("Error listando clientes por estado {}: {}", estado, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar clientes por tipo de documento", description = "Lista clientes filtrados por tipo de documento")
    @GetMapping("/tipo-documento/{tipoDocumento}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<ClienteDTO>>> listarPorTipoDocumento(@PathVariable TipoDocumento tipoDocumento) {
        try {
            List<ClienteDTO> clientes = clienteService.listarPorTipoDocumento(tipoDocumento);
            return ResponseEntity.ok(ApiResponseDTO.success(clientes));
                
        } catch (Exception e) {
            log.error("Error listando clientes por tipo documento {}: {}", tipoDocumento, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar clientes con filtros", description = "Busca clientes con múltiples filtros")
    @GetMapping("/buscar-filtros")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<ClienteDTO>>> buscarConFiltros(
            @RequestParam(required = false) String nombres,
            @RequestParam(required = false) String apellidos,
            @RequestParam(required = false) String razonSocial,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) TipoDocumento tipoDocumento,
            @RequestParam(required = false) Estado estado,
            Pageable pageable) {
        try {
            PaginatedResponseDTO<ClienteDTO> clientes = clienteService.buscarConFiltros(
                nombres, apellidos, razonSocial, email, tipoDocumento, estado, pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(clientes));
                
        } catch (Exception e) {
            log.error("Error buscando clientes con filtros: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente (soft delete)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(@PathVariable Long id) {
        try {
            clienteService.eliminar(id);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Cliente eliminado exitosamente", null));
                
        } catch (Exception e) {
            log.error("Error eliminando cliente {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Cambiar estado de cliente", description = "Cambia el estado de un cliente")
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<ClienteDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Estado estado) {
        try {
            ClienteDTO clienteActualizado = clienteService.cambiarEstado(id, estado);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Estado de cliente actualizado exitosamente", clienteActualizado));
                
        } catch (Exception e) {
            log.error("Error cambiando estado de cliente {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar clientes por nombre", description = "Busca clientes por nombre (nombres, apellidos o razón social)")
    @GetMapping("/buscar-nombre")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<ClienteResponseDTO>>> buscarPorNombre(@RequestParam String nombre) {
        try {
            List<ClienteResponseDTO> clientes = clienteService.buscarPorNombre(nombre);
            return ResponseEntity.ok(ApiResponseDTO.success(clientes));
                
        } catch (Exception e) {
            log.error("Error buscando clientes por nombre {}: {}", nombre, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar clientes activos", description = "Lista todos los clientes activos para selección")
    @GetMapping("/activos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<ClienteResponseDTO>>> listarActivos() {
        try {
            List<ClienteResponseDTO> clientes = clienteService.listarActivos();
            return ResponseEntity.ok(ApiResponseDTO.success(clientes));
                
        } catch (Exception e) {
            log.error("Error listando clientes activos: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Verificar existencia de cliente", description = "Verifica si existe un cliente con el mismo tipo y número de documento")
    @GetMapping("/verificar-existencia")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR')")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarExistencia(
            @RequestParam TipoDocumento tipoDocumento,
            @RequestParam String numeroDocumento) {
        try {
            boolean existe = clienteService.existePorTipoYNumeroDocumento(tipoDocumento, numeroDocumento);
            return ResponseEntity.ok(ApiResponseDTO.success(existe));
                
        } catch (Exception e) {
            log.error("Error verificando existencia de cliente {} {}: {}", tipoDocumento, numeroDocumento, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Verificar email único", description = "Verifica si existe un cliente con el mismo email")
    @GetMapping("/verificar-email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR')")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarEmail(@RequestParam String email) {
        try {
            boolean existe = clienteService.existePorEmail(email);
            return ResponseEntity.ok(ApiResponseDTO.success(existe));
                
        } catch (Exception e) {
            log.error("Error verificando email {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
