package com.nove.sule.backend_nove_sule.controller;

import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.dto.inventario.*;
import com.nove.sule.backend_nove_sule.entity.enums.TipoMovimiento;
import com.nove.sule.backend_nove_sule.service.InventarioService;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para gestión de inventario
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_BASE_PATH + "/inventario")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Inventario", description = "Endpoints para gestión de inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    // ===== MOVIMIENTOS DE INVENTARIO =====

    @Operation(summary = "Registrar movimiento", description = "Registra un movimiento de inventario")
    @PostMapping("/movimientos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<MovimientoInventarioDTO>> registrarMovimiento(@Valid @RequestBody MovimientoInventarioRequestDTO movimientoRequest) {
        try {
            MovimientoInventarioDTO movimiento = inventarioService.registrarMovimiento(movimientoRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Movimiento registrado exitosamente", movimiento));
                
        } catch (Exception e) {
            log.error("Error registrando movimiento: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar movimiento por ID", description = "Busca un movimiento de inventario por su ID")
    @GetMapping("/movimientos/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<MovimientoInventarioDTO>> buscarMovimientoPorId(@PathVariable Long id) {
        try {
            return inventarioService.buscarMovimientoPorId(id)
                .map(movimiento -> ResponseEntity.ok(ApiResponseDTO.<MovimientoInventarioDTO>success(movimiento)))
                .orElse(ResponseEntity.notFound().build());
                
        } catch (Exception e) {
            log.error("Error buscando movimiento {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar movimientos de producto", description = "Lista todos los movimientos de un producto")
    @GetMapping("/productos/{productoId}/movimientos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<MovimientoInventarioDTO>>> listarMovimientosProducto(@PathVariable Long productoId) {
        try {
            List<MovimientoInventarioDTO> movimientos = inventarioService.listarMovimientosProducto(productoId);
            return ResponseEntity.ok(ApiResponseDTO.success(movimientos));
                
        } catch (Exception e) {
            log.error("Error listando movimientos del producto {}: {}", productoId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar movimientos por tipo", description = "Lista movimientos filtrados por tipo")
    @GetMapping("/movimientos/tipo/{tipoMovimiento}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<MovimientoInventarioDTO>>> listarMovimientosPorTipo(@PathVariable TipoMovimiento tipoMovimiento) {
        try {
            List<MovimientoInventarioDTO> movimientos = inventarioService.listarMovimientosPorTipo(tipoMovimiento);
            return ResponseEntity.ok(ApiResponseDTO.success(movimientos));
                
        } catch (Exception e) {
            log.error("Error listando movimientos por tipo {}: {}", tipoMovimiento, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar movimientos con filtros", description = "Busca movimientos con múltiples filtros")
    @GetMapping("/movimientos/buscar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<MovimientoInventarioDTO>>> buscarMovimientosConFiltros(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) TipoMovimiento tipoMovimiento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            Pageable pageable) {
        try {
            PaginatedResponseDTO<MovimientoInventarioDTO> movimientos = inventarioService.buscarMovimientosConFiltros(
                productoId, tipoMovimiento, fechaInicio, fechaFin, pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(movimientos));
                
        } catch (Exception e) {
            log.error("Error buscando movimientos con filtros: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar movimientos por fecha", description = "Lista movimientos en un rango de fechas")
    @GetMapping("/movimientos/fecha")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<MovimientoInventarioDTO>>> listarMovimientosPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            List<MovimientoInventarioDTO> movimientos = inventarioService.listarMovimientosPorFecha(fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(movimientos));
                
        } catch (Exception e) {
            log.error("Error listando movimientos por fecha: {} - {}: {}", fechaInicio, fechaFin, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar movimientos recientes", description = "Lista los movimientos más recientes")
    @GetMapping("/movimientos/recientes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<MovimientoInventarioDTO>>> listarMovimientosRecientes(
            @RequestParam(defaultValue = "10") int limite) {
        try {
            List<MovimientoInventarioDTO> movimientos = inventarioService.listarMovimientosRecientes(limite);
            return ResponseEntity.ok(ApiResponseDTO.success(movimientos));
                
        } catch (Exception e) {
            log.error("Error listando movimientos recientes: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    // ===== TIPOS ESPECÍFICOS DE MOVIMIENTOS =====

    @Operation(summary = "Registrar entrada", description = "Registra una entrada de inventario")
    @PostMapping("/entradas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<MovimientoInventarioDTO>> registrarEntrada(@Valid @RequestBody MovimientoInventarioRequestDTO movimientoRequest) {
        try {
            MovimientoInventarioDTO movimiento = inventarioService.registrarEntrada(movimientoRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Entrada registrada exitosamente", movimiento));
                
        } catch (Exception e) {
            log.error("Error registrando entrada: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Registrar salida", description = "Registra una salida de inventario")
    @PostMapping("/salidas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<MovimientoInventarioDTO>> registrarSalida(@Valid @RequestBody MovimientoInventarioRequestDTO movimientoRequest) {
        try {
            MovimientoInventarioDTO movimiento = inventarioService.registrarSalida(movimientoRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Salida registrada exitosamente", movimiento));
                
        } catch (Exception e) {
            log.error("Error registrando salida: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Registrar ajuste", description = "Registra un ajuste de inventario")
    @PostMapping("/ajustes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<MovimientoInventarioDTO>> registrarAjuste(@Valid @RequestBody MovimientoInventarioRequestDTO movimientoRequest) {
        try {
            MovimientoInventarioDTO movimiento = inventarioService.registrarAjuste(movimientoRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Ajuste registrado exitosamente", movimiento));
                
        } catch (Exception e) {
            log.error("Error registrando ajuste: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Registrar transferencia", description = "Registra una transferencia de inventario")
    @PostMapping("/transferencias")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<MovimientoInventarioDTO>> registrarTransferencia(@Valid @RequestBody TransferenciaInventarioDTO transferenciaRequest) {
        try {
            MovimientoInventarioDTO movimiento = inventarioService.registrarTransferencia(transferenciaRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Transferencia registrada exitosamente", movimiento));
                
        } catch (Exception e) {
            log.error("Error registrando transferencia: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    // ===== GESTIÓN DE STOCK =====

    @Operation(summary = "Calcular stock actual", description = "Calcula el stock actual de un producto")
    @GetMapping("/productos/{productoId}/stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<Integer>> calcularStockActual(@PathVariable Long productoId) {
        try {
            Integer stock = inventarioService.calcularStockActual(productoId);
            return ResponseEntity.ok(ApiResponseDTO.success(stock));
                
        } catch (Exception e) {
            log.error("Error calculando stock del producto {}: {}", productoId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar stock", description = "Actualiza el stock de un producto")
    @PutMapping("/productos/{productoId}/stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<String>> actualizarStock(
            @PathVariable Long productoId,
            @RequestParam Integer nuevoStock) {
        try {
            inventarioService.actualizarStockProducto(productoId, nuevoStock);
            return ResponseEntity.ok(ApiResponseDTO.success("Stock actualizado exitosamente"));
                
        } catch (Exception e) {
            log.error("Error actualizando stock del producto {}: {}", productoId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Validar stock suficiente", description = "Valida si hay stock suficiente para una operación")
    @GetMapping("/productos/{productoId}/validar-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<Boolean>> validarStockSuficiente(
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {
        try {
            boolean suficiente = inventarioService.validarStockSuficiente(productoId, cantidad);
            return ResponseEntity.ok(ApiResponseDTO.success(suficiente));
                
        } catch (Exception e) {
            log.error("Error validando stock del producto {}: {}", productoId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    // ===== REPORTES Y RESÚMENES =====

    @Operation(summary = "Productos con stock bajo", description = "Lista productos con stock bajo")
    @GetMapping("/stock-bajo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<StockBajoDTO>>> listarProductosConStockBajo() {
        try {
            List<StockBajoDTO> productos = inventarioService.listarProductosConStockBajo();
            return ResponseEntity.ok(ApiResponseDTO.success(productos));
                
        } catch (Exception e) {
            log.error("Error listando productos con stock bajo: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Resumen de producto", description = "Obtiene un resumen de inventario de un producto")
    @GetMapping("/productos/{productoId}/resumen")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<ResumenInventarioDTO>> obtenerResumenProducto(
            @PathVariable Long productoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            ResumenInventarioDTO resumen = inventarioService.obtenerResumenProducto(productoId, fecha);
            return ResponseEntity.ok(ApiResponseDTO.success(resumen));
                
        } catch (Exception e) {
            log.error("Error obteniendo resumen del producto {}: {}", productoId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Resumen general", description = "Obtiene un resumen general del inventario")
    @GetMapping("/resumen-general")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<List<ResumenInventarioDTO>>> obtenerResumenGeneral(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            List<ResumenInventarioDTO> resumenes = inventarioService.obtenerResumenGeneral(fecha);
            return ResponseEntity.ok(ApiResponseDTO.success(resumenes));
                
        } catch (Exception e) {
            log.error("Error obteniendo resumen general: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Valor total del inventario", description = "Calcula el valor total del inventario")
    @GetMapping("/valor-total")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> calcularValorTotalInventario() {
        try {
            BigDecimal valor = inventarioService.calcularValorTotalInventario();
            return ResponseEntity.ok(ApiResponseDTO.success(valor));
                
        } catch (Exception e) {
            log.error("Error calculando valor total del inventario: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Valor del inventario de producto", description = "Calcula el valor del inventario de un producto")
    @GetMapping("/productos/{productoId}/valor")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> calcularValorInventarioProducto(@PathVariable Long productoId) {
        try {
            BigDecimal valor = inventarioService.calcularValorInventarioProducto(productoId);
            return ResponseEntity.ok(ApiResponseDTO.success(valor));
                
        } catch (Exception e) {
            log.error("Error calculando valor del inventario del producto {}: {}", productoId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Historial de stock", description = "Obtiene el historial de stock de un producto")
    @GetMapping("/productos/{productoId}/historial")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<MovimientoInventarioDTO>>> obtenerHistorialStock(
            @PathVariable Long productoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<MovimientoInventarioDTO> historial = inventarioService.obtenerHistorialStock(productoId, fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(historial));
                
        } catch (Exception e) {
            log.error("Error obteniendo historial del producto {}: {}", productoId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Productos próximos a vencer", description = "Lista productos próximos a vencer")
    @GetMapping("/productos-proximos-vencer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<ResumenInventarioDTO>>> obtenerProductosProximosVencer(
            @RequestParam(defaultValue = "30") int dias) {
        try {
            List<ResumenInventarioDTO> productos = inventarioService.obtenerProductosProximosVencer(dias);
            return ResponseEntity.ok(ApiResponseDTO.success(productos));
                
        } catch (Exception e) {
            log.error("Error obteniendo productos próximos a vencer: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Generar reporte de movimientos", description = "Genera un reporte de movimientos con filtros")
    @GetMapping("/reportes/movimientos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<List<MovimientoInventarioDTO>>> generarReporteMovimientos(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) TipoMovimiento tipoMovimiento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            List<MovimientoInventarioDTO> reporte = inventarioService.generarReporteMovimientos(
                productoId, tipoMovimiento, fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(reporte));
                
        } catch (Exception e) {
            log.error("Error generando reporte de movimientos: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    // ===== MOVIMIENTOS RELACIONADOS =====

    @Operation(summary = "Movimientos de orden de compra", description = "Lista movimientos de una orden de compra")
    @GetMapping("/ordenes-compra/{ordenCompraId}/movimientos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<MovimientoInventarioDTO>>> listarMovimientosOrdenCompra(@PathVariable Long ordenCompraId) {
        try {
            List<MovimientoInventarioDTO> movimientos = inventarioService.listarMovimientosOrdenCompra(ordenCompraId);
            return ResponseEntity.ok(ApiResponseDTO.success(movimientos));
                
        } catch (Exception e) {
            log.error("Error listando movimientos de orden de compra {}: {}", ordenCompraId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Movimientos de comprobante de venta", description = "Lista movimientos de un comprobante de venta")
    @GetMapping("/comprobantes-venta/{comprobanteVentaId}/movimientos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<MovimientoInventarioDTO>>> listarMovimientosComprobanteVenta(@PathVariable Long comprobanteVentaId) {
        try {
            List<MovimientoInventarioDTO> movimientos = inventarioService.listarMovimientosComprobanteVenta(comprobanteVentaId);
            return ResponseEntity.ok(ApiResponseDTO.success(movimientos));
                
        } catch (Exception e) {
            log.error("Error listando movimientos de comprobante de venta {}: {}", comprobanteVentaId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Último movimiento de producto", description = "Obtiene el último movimiento de un producto")
    @GetMapping("/productos/{productoId}/ultimo-movimiento")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<MovimientoInventarioDTO>> obtenerUltimoMovimientoProducto(@PathVariable Long productoId) {
        try {
            return inventarioService.obtenerUltimoMovimientoProducto(productoId)
                .map(movimiento -> ResponseEntity.ok(ApiResponseDTO.<MovimientoInventarioDTO>success(movimiento)))
                .orElse(ResponseEntity.notFound().build());
                
        } catch (Exception e) {
            log.error("Error obteniendo último movimiento del producto {}: {}", productoId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
