package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.inventario.*;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.enums.TipoMovimiento;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de Inventario
 */
public interface InventarioService {

    /**
     * Registra un movimiento de inventario
     */
    MovimientoInventarioDTO registrarMovimiento(MovimientoInventarioRequestDTO movimientoRequest);

    /**
     * Busca un movimiento por ID
     */
    Optional<MovimientoInventarioDTO> buscarMovimientoPorId(Long id);

    /**
     * Lista movimientos de un producto
     */
    List<MovimientoInventarioDTO> listarMovimientosProducto(Long productoId);

    /**
     * Lista movimientos por tipo
     */
    List<MovimientoInventarioDTO> listarMovimientosPorTipo(TipoMovimiento tipoMovimiento);

    /**
     * Busca movimientos con filtros
     */
    PaginatedResponseDTO<MovimientoInventarioDTO> buscarMovimientosConFiltros(Long productoId,
                                                                             TipoMovimiento tipoMovimiento,
                                                                             LocalDateTime fechaInicio,
                                                                             LocalDateTime fechaFin,
                                                                             Pageable pageable);

    /**
     * Lista movimientos por rango de fechas
     */
    List<MovimientoInventarioDTO> listarMovimientosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Lista movimientos de una orden de compra
     */
    List<MovimientoInventarioDTO> listarMovimientosOrdenCompra(Long ordenCompraId);

    /**
     * Lista movimientos de un comprobante de venta
     */
    List<MovimientoInventarioDTO> listarMovimientosComprobanteVenta(Long comprobanteVentaId);

    /**
     * Obtiene el último movimiento de un producto
     */
    Optional<MovimientoInventarioDTO> obtenerUltimoMovimientoProducto(Long productoId);

    /**
     * Calcula el stock actual de un producto
     */
    Integer calcularStockActual(Long productoId);

    /**
     * Actualiza el stock de un producto
     */
    void actualizarStockProducto(Long productoId, Integer nuevoStock);

    /**
     * Registra entrada de inventario
     */
    MovimientoInventarioDTO registrarEntrada(MovimientoInventarioRequestDTO movimientoRequest);

    /**
     * Registra salida de inventario
     */
    MovimientoInventarioDTO registrarSalida(MovimientoInventarioRequestDTO movimientoRequest);

    /**
     * Registra ajuste de inventario
     */
    MovimientoInventarioDTO registrarAjuste(MovimientoInventarioRequestDTO movimientoRequest);

    /**
     * Registra transferencia de inventario
     */
    MovimientoInventarioDTO registrarTransferencia(TransferenciaInventarioDTO transferenciaRequest);

    /**
     * Lista productos con stock bajo
     */
    List<StockBajoDTO> listarProductosConStockBajo();

    /**
     * Obtiene resumen de inventario de un producto
     */
    ResumenInventarioDTO obtenerResumenProducto(Long productoId, LocalDate fecha);

    /**
     * Obtiene resumen general de inventario
     */
    List<ResumenInventarioDTO> obtenerResumenGeneral(LocalDate fecha);

    /**
     * Calcula el valor total del inventario
     */
    BigDecimal calcularValorTotalInventario();

    /**
     * Calcula el valor del inventario de un producto
     */
    BigDecimal calcularValorInventarioProducto(Long productoId);

    /**
     * Obtiene estadísticas de movimientos por período
     */
    List<MovimientoInventarioDTO> obtenerEstadisticasMovimientos(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Lista movimientos recientes
     */
    List<MovimientoInventarioDTO> listarMovimientosRecientes(int limite);

    /**
     * Valida si hay stock suficiente para una salida
     */
    boolean validarStockSuficiente(Long productoId, Integer cantidad);

    /**
     * Obtiene el historial de stock de un producto
     */
    List<MovimientoInventarioDTO> obtenerHistorialStock(Long productoId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Calcula el precio promedio de un producto
     */
    BigDecimal calcularPrecioPromedio(Long productoId);

    /**
     * Obtiene productos próximos a vencer
     */
    List<ResumenInventarioDTO> obtenerProductosProximosVencer(int dias);

    /**
     * Genera reporte de movimientos
     */
    List<MovimientoInventarioDTO> generarReporteMovimientos(Long productoId,
                                                           TipoMovimiento tipoMovimiento,
                                                           LocalDateTime fechaInicio,
                                                           LocalDateTime fechaFin);
}
