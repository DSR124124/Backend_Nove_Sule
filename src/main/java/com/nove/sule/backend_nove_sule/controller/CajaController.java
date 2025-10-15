package com.nove.sule.backend_nove_sule.controller;

import com.nove.sule.backend_nove_sule.dto.caja.*;
import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Caja;
import com.nove.sule.backend_nove_sule.service.CajaService;
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
 * Controlador para gestión de cajas
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_BASE_PATH + "/cajas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cajas", description = "Endpoints para gestión de cajas")
public class CajaController {

    private final CajaService cajaService;

    @Operation(summary = "Crear caja", description = "Crea una nueva caja")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<CajaDTO>> crearCaja(@Valid @RequestBody CajaRequestDTO cajaRequest) {
        try {
            CajaDTO nuevaCaja = cajaService.crearCaja(cajaRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Caja creada exitosamente", nuevaCaja));
                
        } catch (Exception e) {
            log.error("Error creando caja: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar caja", description = "Actualiza una caja existente")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<CajaDTO>> actualizarCaja(
            @PathVariable Long id,
            @Valid @RequestBody CajaRequestDTO cajaRequest) {
        try {
            CajaDTO cajaActualizada = cajaService.actualizarCaja(id, cajaRequest);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Caja actualizada exitosamente", cajaActualizada));
                
        } catch (Exception e) {
            log.error("Error actualizando caja {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar caja por ID", description = "Busca una caja por su ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<CajaDTO>> buscarPorId(@PathVariable Long id) {
        try {
            return cajaService.buscarCajaPorId(id)
                .map(caja -> ResponseEntity.ok(ApiResponseDTO.<CajaDTO>success(caja)))
                .orElse(ResponseEntity.notFound().build());
                
        } catch (Exception e) {
            log.error("Error buscando caja {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar caja por nombre", description = "Busca una caja por su nombre")
    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<CajaDTO>> buscarPorNombre(@RequestParam String nombre) {
        try {
            return cajaService.buscarPorNombre(nombre)
                .map(caja -> ResponseEntity.ok(ApiResponseDTO.<CajaDTO>success(caja)))
                .orElse(ResponseEntity.notFound().build());
                
        } catch (Exception e) {
            log.error("Error buscando caja {}: {}", nombre, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar cajas", description = "Lista todas las cajas con paginación")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<CajaDTO>>> listarCajas(Pageable pageable) {
        try {
            PaginatedResponseDTO<CajaDTO> cajas = cajaService.listarCajas(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(cajas));
                
        } catch (Exception e) {
            log.error("Error listando cajas: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar cajas por estado", description = "Lista cajas filtradas por estado")
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<CajaDTO>>> listarPorEstado(@PathVariable Caja.EstadoCaja estado) {
        try {
            List<CajaDTO> cajas = cajaService.listarPorEstado(estado);
            return ResponseEntity.ok(ApiResponseDTO.success(cajas));
                
        } catch (Exception e) {
            log.error("Error listando cajas por estado {}: {}", estado, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar cajas abiertas", description = "Lista todas las cajas abiertas")
    @GetMapping("/abiertas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<CajaDTO>>> listarCajasAbiertas() {
        try {
            List<CajaDTO> cajas = cajaService.listarCajasAbiertas();
            return ResponseEntity.ok(ApiResponseDTO.success(cajas));
                
        } catch (Exception e) {
            log.error("Error listando cajas abiertas: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar cajas con filtros", description = "Busca cajas con múltiples filtros")
    @GetMapping("/buscar-filtros")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<CajaDTO>>> buscarConFiltros(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Caja.EstadoCaja estado,
            Pageable pageable) {
        try {
            PaginatedResponseDTO<CajaDTO> cajas = cajaService.buscarConFiltros(nombre, estado, pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(cajas));
                
        } catch (Exception e) {
            log.error("Error buscando cajas con filtros: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Abrir caja", description = "Abre una caja con saldo inicial")
    @PatchMapping("/{id}/abrir")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<CajaDTO>> abrirCaja(
            @PathVariable Long id,
            @RequestParam(required = false) BigDecimal saldoInicial) {
        try {
            CajaDTO caja = cajaService.abrirCaja(id, saldoInicial);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Caja abierta exitosamente", caja));
                
        } catch (Exception e) {
            log.error("Error abriendo caja {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Cerrar caja", description = "Cierra una caja")
    @PatchMapping("/{id}/cerrar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<CajaDTO>> cerrarCaja(@PathVariable Long id) {
        try {
            CajaDTO caja = cajaService.cerrarCaja(id);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Caja cerrada exitosamente", caja));
                
        } catch (Exception e) {
            log.error("Error cerrando caja {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Cambiar estado de caja", description = "Cambia el estado de una caja")
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<CajaDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Caja.EstadoCaja estado) {
        try {
            CajaDTO caja = cajaService.cambiarEstado(id, estado);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Estado cambiado exitosamente", caja));
                
        } catch (Exception e) {
            log.error("Error cambiando estado de caja {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar cajas por responsable", description = "Lista cajas de un responsable específico")
    @GetMapping("/responsable/{responsableId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<CajaDTO>>> listarPorResponsable(@PathVariable Long responsableId) {
        try {
            List<CajaDTO> cajas = cajaService.listarPorResponsable(responsableId);
            return ResponseEntity.ok(ApiResponseDTO.success(cajas));
                
        } catch (Exception e) {
            log.error("Error listando cajas por responsable {}: {}", responsableId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Verificar nombre de caja", description = "Verifica si existe una caja con el nombre")
    @GetMapping("/verificar-nombre")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarNombre(@RequestParam String nombre) {
        try {
            boolean existe = cajaService.existeNombre(nombre);
            return ResponseEntity.ok(ApiResponseDTO.success(existe));
                
        } catch (Exception e) {
            log.error("Error verificando nombre de caja {}: {}", nombre, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    // ===== MOVIMIENTOS DE CAJA =====

    @Operation(summary = "Registrar movimiento", description = "Registra un movimiento en la caja")
    @PostMapping("/movimientos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<MovimientoCajaDTO>> registrarMovimiento(@Valid @RequestBody MovimientoCajaRequestDTO movimientoRequest) {
        try {
            MovimientoCajaDTO movimiento = cajaService.registrarMovimiento(movimientoRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Movimiento registrado exitosamente", movimiento));
                
        } catch (Exception e) {
            log.error("Error registrando movimiento: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar movimientos de caja", description = "Lista todos los movimientos de una caja")
    @GetMapping("/{cajaId}/movimientos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<MovimientoCajaDTO>>> listarMovimientosCaja(@PathVariable Long cajaId) {
        try {
            List<MovimientoCajaDTO> movimientos = cajaService.listarMovimientosCaja(cajaId);
            return ResponseEntity.ok(ApiResponseDTO.success(movimientos));
                
        } catch (Exception e) {
            log.error("Error listando movimientos de caja {}: {}", cajaId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar movimientos por fecha", description = "Lista movimientos de una caja por fecha específica")
    @GetMapping("/{cajaId}/movimientos/fecha")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<MovimientoCajaDTO>>> listarMovimientosPorFecha(
            @PathVariable Long cajaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            List<MovimientoCajaDTO> movimientos = cajaService.listarMovimientosPorFecha(cajaId, fecha);
            return ResponseEntity.ok(ApiResponseDTO.success(movimientos));
                
        } catch (Exception e) {
            log.error("Error listando movimientos por fecha: {} - {}: {}", cajaId, fecha, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar movimientos por rango de fechas", description = "Lista movimientos de una caja por rango de fechas")
    @GetMapping("/{cajaId}/movimientos/rango")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<List<MovimientoCajaDTO>>> listarMovimientosPorRangoFechas(
            @PathVariable Long cajaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<MovimientoCajaDTO> movimientos = cajaService.listarMovimientosPorRangoFechas(cajaId, fechaInicio, fechaFin);
            return ResponseEntity.ok(ApiResponseDTO.success(movimientos));
                
        } catch (Exception e) {
            log.error("Error listando movimientos por rango: {} - {} a {}: {}", cajaId, fechaInicio, fechaFin, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Listar movimientos con paginación", description = "Lista movimientos de una caja con paginación")
    @GetMapping("/{cajaId}/movimientos/paginados")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<MovimientoCajaDTO>>> listarMovimientosConPaginacion(
            @PathVariable Long cajaId,
            Pageable pageable) {
        try {
            PaginatedResponseDTO<MovimientoCajaDTO> movimientos = cajaService.listarMovimientosConPaginacion(cajaId, pageable);
            return ResponseEntity.ok(ApiResponseDTO.success(movimientos));
                
        } catch (Exception e) {
            log.error("Error listando movimientos con paginación de caja {}: {}", cajaId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    // ===== REPORTES Y ESTADÍSTICAS =====

    @Operation(summary = "Calcular saldo actual", description = "Calcula el saldo actual de una caja")
    @GetMapping("/{cajaId}/saldo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> calcularSaldoActual(@PathVariable Long cajaId) {
        try {
            BigDecimal saldo = cajaService.calcularSaldoActual(cajaId);
            return ResponseEntity.ok(ApiResponseDTO.success(saldo));
                
        } catch (Exception e) {
            log.error("Error calculando saldo de caja {}: {}", cajaId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Obtener resumen de caja", description = "Obtiene un resumen de una caja para una fecha específica")
    @GetMapping("/{cajaId}/resumen")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<ResumenCajaDTO>> obtenerResumenCaja(
            @PathVariable Long cajaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            ResumenCajaDTO resumen = cajaService.obtenerResumenCaja(cajaId, fecha);
            return ResponseEntity.ok(ApiResponseDTO.success(resumen));
                
        } catch (Exception e) {
            log.error("Error obteniendo resumen de caja {}: {}", cajaId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Obtener resumen de todas las cajas", description = "Obtiene un resumen de todas las cajas para una fecha específica")
    @GetMapping("/resumen-todas")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<List<ResumenCajaDTO>>> obtenerResumenTodasLasCajas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            List<ResumenCajaDTO> resumenes = cajaService.obtenerResumenTodasLasCajas(fecha);
            return ResponseEntity.ok(ApiResponseDTO.success(resumenes));
                
        } catch (Exception e) {
            log.error("Error obteniendo resumen de todas las cajas: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Calcular total de ingresos", description = "Calcula el total de ingresos de una caja en una fecha")
    @GetMapping("/{cajaId}/ingresos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> calcularTotalIngresos(
            @PathVariable Long cajaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            BigDecimal total = cajaService.calcularTotalIngresos(cajaId, fecha);
            return ResponseEntity.ok(ApiResponseDTO.success(total));
                
        } catch (Exception e) {
            log.error("Error calculando total de ingresos de caja {}: {}", cajaId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Calcular total de egresos", description = "Calcula el total de egresos de una caja en una fecha")
    @GetMapping("/{cajaId}/egresos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> calcularTotalEgresos(
            @PathVariable Long cajaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            BigDecimal total = cajaService.calcularTotalEgresos(cajaId, fecha);
            return ResponseEntity.ok(ApiResponseDTO.success(total));
                
        } catch (Exception e) {
            log.error("Error calculando total de egresos de caja {}: {}", cajaId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Obtener último movimiento", description = "Obtiene el último movimiento de una caja")
    @GetMapping("/{cajaId}/ultimo-movimiento")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<MovimientoCajaDTO>> obtenerUltimoMovimiento(@PathVariable Long cajaId) {
        try {
            return cajaService.obtenerUltimoMovimiento(cajaId)
                .map(movimiento -> ResponseEntity.ok(ApiResponseDTO.<MovimientoCajaDTO>success(movimiento)))
                .orElse(ResponseEntity.notFound().build());
                
        } catch (Exception e) {
            log.error("Error obteniendo último movimiento de caja {}: {}", cajaId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Validar caja activa", description = "Valida si una caja está activa")
    @GetMapping("/{cajaId}/validar-activa")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('CAJERO')")
    public ResponseEntity<ApiResponseDTO<Boolean>> validarCajaActiva(@PathVariable Long cajaId) {
        try {
            boolean activa = cajaService.validarCajaActiva(cajaId);
            return ResponseEntity.ok(ApiResponseDTO.success(activa));
                
        } catch (Exception e) {
            log.error("Error validando caja activa {}: {}", cajaId, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
