package com.nove.sule.backend_nove_sule.controller;

import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.dto.compras.*;
import com.nove.sule.backend_nove_sule.entity.enums.EstadoOrdenCompra;
import com.nove.sule.backend_nove_sule.service.CompraService;
import com.nove.sule.backend_nove_sule.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para gestión de compras
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_BASE_PATH + "/compras")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Compras", description = "Endpoints para gestión de compras")
public class CompraController {

    private final CompraService compraService;

    @Operation(summary = "Crear orden de compra", description = "Crea una nueva orden de compra")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> crearOrdenCompra(@Valid @RequestBody OrdenCompraRequestDTO ordenRequest) {
        try {
            OrdenCompraDTO nuevaOrden = compraService.crearOrdenCompra(ordenRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Orden de compra creada exitosamente", nuevaOrden));
                
        } catch (Exception e) {
            log.error("Error creando orden de compra: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar orden de compra", description = "Actualiza una orden de compra existente")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> actualizarOrdenCompra(
            @PathVariable Long id,
            @Valid @RequestBody OrdenCompraRequestDTO ordenRequest) {
        try {
            OrdenCompraDTO ordenActualizada = compraService.actualizarOrdenCompra(id, ordenRequest);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Orden de compra actualizada exitosamente", ordenActualizada));
                
        } catch (Exception e) {
            log.error("Error actualizando orden de compra {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar orden de compra por ID", description = "Busca una orden de compra por su ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> buscarPorId(@PathVariable Long id) {
        try {
            return compraService.buscarOrdenCompraPorId(id)
                .map(orden -> ResponseEntity.ok(ApiResponseDTO.<OrdenCompraDTO>success(orden)))
                .orElse(ResponseEntity.notFound().build());
                
        } catch (Exception e) {
            log.error("Error buscando orden de compra {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar orden de compra por número", description = "Busca una orden de compra por su número")
    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> buscarPorNumero(@RequestParam String numero) {
        try {
            return compraService.buscarPorNumero(numero)
                .map(orden -> ResponseEntity.ok(ApiResponseDTO.<OrdenCompraDTO>success(orden)))
                .orElse(ResponseEntity.notFound().build());
                
        } catch (Exception e) {
            log.error("Error buscando orden de compra {}: {}", numero, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar órdenes de compra", description = "Lista todas las órdenes de compra con paginación")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<OrdenCompraDTO>>> listarOrdenesCompra(Pageable pageable) {
        try {
            PaginatedResponseDTO<OrdenCompraDTO> ordenes = compraService.listarOrdenesCompra(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(ordenes));
                
        } catch (Exception e) {
            log.error("Error listando órdenes de compra: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar órdenes por estado", description = "Lista órdenes de compra filtradas por estado")
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<OrdenCompraDTO>>> listarPorEstado(@PathVariable EstadoOrdenCompra estado) {
        try {
            List<OrdenCompraDTO> ordenes = compraService.listarPorEstado(estado);
            return ResponseEntity.ok(ApiResponseDTO.success(ordenes));
                
        } catch (Exception e) {
            log.error("Error listando órdenes por estado {}: {}", estado, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar órdenes con filtros", description = "Busca órdenes de compra con múltiples filtros")
    @GetMapping("/buscar-filtros")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<OrdenCompraDTO>>> buscarConFiltros(
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) Long proveedorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) EstadoOrdenCompra estado,
            Pageable pageable) {
        try {
            PaginatedResponseDTO<OrdenCompraDTO> ordenes = compraService.buscarConFiltros(
                numero, proveedorId, fechaInicio, fechaFin, estado, pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(ordenes));
                
        } catch (Exception e) {
            log.error("Error buscando órdenes con filtros: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Cambiar estado de orden", description = "Cambia el estado de una orden de compra")
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoOrdenCompra estado) {
        try {
            OrdenCompraDTO orden = compraService.cambiarEstado(id, estado);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Estado cambiado exitosamente", orden));
                
        } catch (Exception e) {
            log.error("Error cambiando estado de orden {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Cancelar orden de compra", description = "Cancela una orden de compra")
    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> cancelarOrdenCompra(@PathVariable Long id) {
        try {
            OrdenCompraDTO orden = compraService.cancelarOrdenCompra(id);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Orden de compra cancelada exitosamente", orden));
                
        } catch (Exception e) {
            log.error("Error cancelando orden de compra {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Marcar como entregada", description = "Marca una orden de compra como entregada")
    @PatchMapping("/{id}/entregar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<OrdenCompraDTO>> marcarComoEntregada(@PathVariable Long id) {
        try {
            OrdenCompraDTO orden = compraService.marcarComoEntregada(id);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Orden de compra marcada como entregada", orden));
                
        } catch (Exception e) {
            log.error("Error marcando orden como entregada {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar órdenes por proveedor", description = "Lista órdenes de compra de un proveedor específico")
    @GetMapping("/proveedor/{proveedorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<OrdenCompraDTO>>> listarPorProveedor(@PathVariable Long proveedorId) {
        try {
            List<OrdenCompraDTO> ordenes = compraService.listarPorProveedor(proveedorId);
            return ResponseEntity.ok(ApiResponseDTO.success(ordenes));
                
        } catch (Exception e) {
            log.error("Error listando órdenes por proveedor {}: {}", proveedorId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar órdenes por fecha", description = "Lista órdenes de compra en un rango de fechas")
    @GetMapping("/fecha")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<OrdenCompraDTO>>> listarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<OrdenCompraDTO> ordenes = compraService.listarPorFecha(fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(ordenes));
                
        } catch (Exception e) {
            log.error("Error listando órdenes por fecha: {} - {}: {}", fechaInicio, fechaFin, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Calcular total de compras", description = "Calcula el total de compras en un rango de fechas")
    @GetMapping("/total-compras")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> calcularTotalCompras(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            BigDecimal total = compraService.calcularTotalCompras(fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(total));
                
        } catch (Exception e) {
            log.error("Error calculando total de compras: {} - {}: {}", fechaInicio, fechaFin, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Contar órdenes de compra", description = "Cuenta el número de órdenes de compra en un rango de fechas")
    @GetMapping("/contar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<Long>> contarOrdenesCompra(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            Long cantidad = compraService.contarOrdenesCompra(fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(cantidad));
                
        } catch (Exception e) {
            log.error("Error contando órdenes de compra: {} - {}: {}", fechaInicio, fechaFin, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Generar número de orden", description = "Genera el siguiente número de orden de compra")
    @GetMapping("/generar-numero")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<String>> generarNumeroOrdenCompra() {
        try {
            String numero = compraService.generarNumeroOrdenCompra();
            return ResponseEntity.ok(ApiResponseDTO.success(numero));
                
        } catch (Exception e) {
            log.error("Error generando número de orden: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Verificar número de orden", description = "Verifica si un número de orden de compra ya existe")
    @GetMapping("/verificar-numero")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarNumeroOrdenCompra(@RequestParam String numero) {
        try {
            boolean existe = compraService.existeNumeroOrdenCompra(numero);
            return ResponseEntity.ok(ApiResponseDTO.success(existe));
                
        } catch (Exception e) {
            log.error("Error verificando número de orden {}: {}", numero, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Obtener resumen de compras", description = "Obtiene un resumen de compras por rango de fechas")
    @GetMapping("/resumen")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<ResumenComprasDTO>> obtenerResumenCompras(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            ResumenComprasDTO resumen = compraService.obtenerResumenCompras(fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(resumen));
                
        } catch (Exception e) {
            log.error("Error obteniendo resumen de compras: {} - {}: {}", fechaInicio, fechaFin, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Órdenes pendientes", description = "Obtiene las órdenes de compra pendientes")
    @GetMapping("/pendientes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<OrdenCompraDTO>>> obtenerOrdenesPendientes() {
        try {
            List<OrdenCompraDTO> ordenes = compraService.obtenerOrdenesPendientes();
            return ResponseEntity.ok(ApiResponseDTO.success(ordenes));
                
        } catch (Exception e) {
            log.error("Error obteniendo órdenes pendientes: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Órdenes vencidas", description = "Obtiene las órdenes de compra vencidas")
    @GetMapping("/vencidas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<OrdenCompraDTO>>> obtenerOrdenesVencidas() {
        try {
            List<OrdenCompraDTO> ordenes = compraService.obtenerOrdenesVencidas();
            return ResponseEntity.ok(ApiResponseDTO.success(ordenes));
                
        } catch (Exception e) {
            log.error("Error obteniendo órdenes vencidas: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
