package com.nove.sule.backend_nove_sule.controller;

import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.dto.ventas.*;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoComprobante;
import com.nove.sule.backend_nove_sule.service.VentaService;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para gestión de ventas
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_BASE_PATH + "/ventas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Ventas", description = "Endpoints para gestión de ventas")
public class VentaController {

    private final VentaService ventaService;

    @Operation(summary = "Crear comprobante de venta", description = "Crea un nuevo comprobante de venta")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<ComprobanteVentaDTO>> crearComprobante(@Valid @RequestBody ComprobanteVentaRequestDTO comprobanteRequest) {
        try {
            // Convertir request a DTO completo
            ComprobanteVentaDTO comprobanteDTO = ComprobanteVentaDTO.builder()
                .tipoComprobante(comprobanteRequest.getTipoComprobante())
                .cliente(ClienteBasicoDTO.builder().id(comprobanteRequest.getClienteId()).build())
                .fechaEmision(comprobanteRequest.getFechaEmision())
                .moneda(comprobanteRequest.getMoneda())
                .tipoCambio(comprobanteRequest.getTipoCambio())
                .descuento(comprobanteRequest.getDescuento())
                .estado(Estado.ACTIVO)
                .medioPago(comprobanteRequest.getMedioPago())
                .observaciones(comprobanteRequest.getObservaciones())
                .detalles(comprobanteRequest.getDetalles().stream()
                    .map(detalleRequest -> ComprobanteVentaDTO.DetalleComprobanteDTO.builder()
                        .productoId(detalleRequest.getProductoId())
                        .cantidad(detalleRequest.getCantidad())
                        .precioUnitario(detalleRequest.getPrecioUnitario())
                        .descuento(detalleRequest.getDescuento())
                        .build())
                    .toList())
                .build();

            ComprobanteVentaDTO nuevoComprobante = ventaService.crearComprobante(comprobanteDTO);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Comprobante de venta creado exitosamente", nuevoComprobante));
                
        } catch (Exception e) {
            log.error("Error creando comprobante de venta: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar comprobante por ID", description = "Busca un comprobante de venta por su ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<ComprobanteVentaDTO>> buscarPorId(@PathVariable Long id) {
        try {
            return ventaService.buscarComprobantePorId(id)
                .map(comprobante -> ResponseEntity.ok(ApiResponseDTO.<ComprobanteVentaDTO>success(comprobante)))
                .orElse(ResponseEntity.notFound().build());
                
        } catch (Exception e) {
            log.error("Error buscando comprobante {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar comprobante por tipo, serie y número", description = "Busca un comprobante por sus datos únicos")
    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<ComprobanteVentaDTO>> buscarPorTipoSerieNumero(
            @RequestParam TipoComprobante tipo,
            @RequestParam String serie,
            @RequestParam String numero) {
        try {
            return ventaService.buscarPorTipoSerieNumero(tipo, serie, numero)
                .map(comprobante -> ResponseEntity.ok(ApiResponseDTO.<ComprobanteVentaDTO>success(comprobante)))
                .orElse(ResponseEntity.notFound().build());
                
        } catch (Exception e) {
            log.error("Error buscando comprobante {} {} {}: {}", tipo, serie, numero, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar comprobantes", description = "Lista todos los comprobantes con paginación")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<ComprobanteVentaDTO>>> listarComprobantes(Pageable pageable) {
        try {
            PaginatedResponseDTO<ComprobanteVentaDTO> comprobantes = ventaService.listarComprobantes(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(comprobantes));
                
        } catch (Exception e) {
            log.error("Error listando comprobantes: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar comprobantes por estado", description = "Lista comprobantes filtrados por estado")
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<ComprobanteVentaDTO>>> listarPorEstado(@PathVariable Estado estado) {
        try {
            List<ComprobanteVentaDTO> comprobantes = ventaService.listarPorEstado(estado);
            return ResponseEntity.ok(ApiResponseDTO.success(comprobantes));
                
        } catch (Exception e) {
            log.error("Error listando comprobantes por estado {}: {}", estado, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar comprobantes con filtros", description = "Busca comprobantes con múltiples filtros")
    @GetMapping("/buscar-filtros")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<ComprobanteVentaDTO>>> buscarConFiltros(
            @RequestParam(required = false) TipoComprobante tipoComprobante,
            @RequestParam(required = false) String serie,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) Estado estado,
            Pageable pageable) {
        try {
            PaginatedResponseDTO<ComprobanteVentaDTO> comprobantes = ventaService.buscarConFiltros(
                tipoComprobante, serie, numero, clienteId, fechaInicio, fechaFin, estado, pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(comprobantes));
                
        } catch (Exception e) {
            log.error("Error buscando comprobantes con filtros: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Anular comprobante", description = "Anula un comprobante de venta")
    @PatchMapping("/{id}/anular")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR')")
    public ResponseEntity<ApiResponseDTO<ComprobanteVentaDTO>> anularComprobante(@PathVariable Long id) {
        try {
            ComprobanteVentaDTO comprobanteAnulado = ventaService.anularComprobante(id);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Comprobante anulado exitosamente", comprobanteAnulado));
                
        } catch (Exception e) {
            log.error("Error anulando comprobante {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar comprobantes por cliente", description = "Lista comprobantes de un cliente específico")
    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<ComprobanteVentaDTO>>> listarPorCliente(@PathVariable Long clienteId) {
        try {
            List<ComprobanteVentaDTO> comprobantes = ventaService.listarPorCliente(clienteId);
            return ResponseEntity.ok(ApiResponseDTO.success(comprobantes));
                
        } catch (Exception e) {
            log.error("Error listando comprobantes por cliente {}: {}", clienteId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar comprobantes por fecha", description = "Lista comprobantes en un rango de fechas")
    @GetMapping("/fecha")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<ComprobanteVentaDTO>>> listarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            List<ComprobanteVentaDTO> comprobantes = ventaService.listarPorFecha(fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(comprobantes));
                
        } catch (Exception e) {
            log.error("Error listando comprobantes por fecha: {} - {}: {}", fechaInicio, fechaFin, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Calcular total de ventas", description = "Calcula el total de ventas en un rango de fechas")
    @GetMapping("/total-ventas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> calcularTotalVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            BigDecimal total = ventaService.calcularTotalVentas(fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(total));
                
        } catch (Exception e) {
            log.error("Error calculando total de ventas: {} - {}: {}", fechaInicio, fechaFin, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Contar comprobantes", description = "Cuenta el número de comprobantes en un rango de fechas")
    @GetMapping("/contar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<Long>> contarComprobantes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            Long cantidad = ventaService.contarComprobantes(fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(cantidad));
                
        } catch (Exception e) {
            log.error("Error contando comprobantes: {} - {}: {}", fechaInicio, fechaFin, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Generar número de comprobante", description = "Genera el siguiente número de comprobante para un tipo y serie")
    @GetMapping("/generar-numero")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<String>> generarNumeroComprobante(
            @RequestParam TipoComprobante tipoComprobante,
            @RequestParam String serie) {
        try {
            String numero = ventaService.generarNumeroComprobante(tipoComprobante, serie);
            return ResponseEntity.ok(ApiResponseDTO.success(numero));
                
        } catch (Exception e) {
            log.error("Error generando número de comprobante: {} {}: {}", tipoComprobante, serie, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Verificar número de comprobante", description = "Verifica si un número de comprobante ya existe")
    @GetMapping("/verificar-numero")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('VENDEDOR') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarNumeroComprobante(
            @RequestParam TipoComprobante tipoComprobante,
            @RequestParam String serie,
            @RequestParam String numero) {
        try {
            boolean existe = ventaService.existeNumeroComprobante(tipoComprobante, serie, numero);
            return ResponseEntity.ok(ApiResponseDTO.success(existe));
                
        } catch (Exception e) {
            log.error("Error verificando número de comprobante: {} {} {}: {}", tipoComprobante, serie, numero, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Obtener resumen de ventas", description = "Obtiene un resumen de ventas por rango de fechas")
    @GetMapping("/resumen")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<ResumenVentasDTO>> obtenerResumenVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            ResumenVentasDTO resumen = ventaService.obtenerResumenVentas(fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(resumen));
                
        } catch (Exception e) {
            log.error("Error obteniendo resumen de ventas: {} - {}: {}", fechaInicio, fechaFin, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Productos más vendidos", description = "Obtiene los productos más vendidos en un rango de fechas")
    @GetMapping("/productos-mas-vendidos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<List<ProductoVendidoDTO>>> obtenerProductosMasVendidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(defaultValue = "10") int limite) {
        try {
            List<ProductoVendidoDTO> productos = ventaService.obtenerProductosMasVendidos(fechaInicio, fechaFin, limite);
            return ResponseEntity.ok(ApiResponseDTO.success(productos));
                
        } catch (Exception e) {
            log.error("Error obteniendo productos más vendidos: {} - {}: {}", fechaInicio, fechaFin, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
