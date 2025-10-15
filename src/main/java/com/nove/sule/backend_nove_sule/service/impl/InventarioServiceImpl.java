package com.nove.sule.backend_nove_sule.service.impl;

import com.nove.sule.backend_nove_sule.dto.inventario.*;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.*;
import com.nove.sule.backend_nove_sule.entity.enums.TipoMovimiento;
import com.nove.sule.backend_nove_sule.mapper.InventarioMapper;
import com.nove.sule.backend_nove_sule.repository.*;
import com.nove.sule.backend_nove_sule.service.InventarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Inventario
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoRepository productoRepository;
    private final OrdenCompraRepository ordenCompraRepository;
    private final ComprobanteVentaRepository comprobanteVentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final InventarioMapper inventarioMapper;

    @Override
    @Transactional
    public MovimientoInventarioDTO registrarMovimiento(MovimientoInventarioRequestDTO movimientoRequest) {
        log.info("Registrando movimiento de inventario para producto: {}", movimientoRequest.getProductoId());

        // Validar producto
        Producto producto = productoRepository.findById(movimientoRequest.getProductoId())
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validar usuario (simulado - en un sistema real se obtendría del contexto de seguridad)
        Usuario usuario = usuarioRepository.findById(1L) // TODO: Obtener usuario actual
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar stock suficiente para salidas
        if (movimientoRequest.getTipoMovimiento() == TipoMovimiento.SALIDA) {
            if (!validarStockSuficiente(movimientoRequest.getProductoId(), movimientoRequest.getCantidad())) {
                throw new RuntimeException("Stock insuficiente para realizar la salida");
            }
        }

        // Crear movimiento
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(movimientoRequest.getTipoMovimiento());
        movimiento.setCantidad(movimientoRequest.getCantidad());
        movimiento.setPrecioUnitario(movimientoRequest.getPrecioUnitario());
        movimiento.setConcepto(movimientoRequest.getConcepto());
        movimiento.setObservaciones(movimientoRequest.getObservaciones());
        movimiento.setUsuario(usuario);
        movimiento.setFechaMovimiento(movimientoRequest.getFechaMovimiento() != null ? 
            movimientoRequest.getFechaMovimiento() : LocalDateTime.now());

        // Obtener relaciones opcionales
        if (movimientoRequest.getOrdenCompraId() != null) {
            OrdenCompra ordenCompra = ordenCompraRepository.findById(movimientoRequest.getOrdenCompraId())
                .orElse(null);
            movimiento.setOrdenCompra(ordenCompra);
        }

        if (movimientoRequest.getComprobanteVentaId() != null) {
            ComprobanteVenta comprobanteVenta = comprobanteVentaRepository.findById(movimientoRequest.getComprobanteVentaId())
                .orElse(null);
            movimiento.setComprobanteVenta(comprobanteVenta);
        }

        // Calcular stocks
        movimiento.setStockAnterior(producto.getStock());
        movimiento.calcularStockNuevo();

        // Actualizar stock del producto
        producto.setStock(movimiento.getStockNuevo());
        productoRepository.save(producto);

        // Guardar movimiento
        movimiento = movimientoInventarioRepository.save(movimiento);

        log.info("Movimiento de inventario registrado con ID: {}", movimiento.getId());
        return inventarioMapper.toDTO(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovimientoInventarioDTO> buscarMovimientoPorId(Long id) {
        log.debug("Buscando movimiento de inventario por ID: {}", id);
        
        return movimientoInventarioRepository.findById(id)
            .map(inventarioMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> listarMovimientosProducto(Long productoId) {
        log.debug("Listando movimientos del producto: {}", productoId);
        
        return movimientoInventarioRepository.findByProductoIdOrderByFechaMovimientoDesc(productoId).stream()
            .map(inventarioMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> listarMovimientosPorTipo(TipoMovimiento tipoMovimiento) {
        log.debug("Listando movimientos por tipo: {}", tipoMovimiento);
        
        return movimientoInventarioRepository.findByTipoMovimientoOrderByFechaMovimientoDesc(tipoMovimiento).stream()
            .map(inventarioMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<MovimientoInventarioDTO> buscarMovimientosConFiltros(Long productoId,
                                                                                     TipoMovimiento tipoMovimiento,
                                                                                     LocalDateTime fechaInicio,
                                                                                     LocalDateTime fechaFin,
                                                                                     Pageable pageable) {
        log.debug("Buscando movimientos con filtros - producto: {}, tipo: {}", productoId, tipoMovimiento);
        
        Page<MovimientoInventario> pageMovimientos = movimientoInventarioRepository.findByFilters(
            productoId, tipoMovimiento, fechaInicio, fechaFin, pageable);
        
        List<MovimientoInventarioDTO> movimientos = pageMovimientos.getContent().stream()
            .map(inventarioMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<MovimientoInventarioDTO>builder()
            .content(movimientos)
            .page(pageMovimientos.getNumber())
            .size(pageMovimientos.getSize())
            .totalElements(pageMovimientos.getTotalElements())
            .totalPages(pageMovimientos.getTotalPages())
            .first(pageMovimientos.isFirst())
            .last(pageMovimientos.isLast())
            .empty(pageMovimientos.isEmpty())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> listarMovimientosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.debug("Listando movimientos por fecha: {} - {}", fechaInicio, fechaFin);
        
        return movimientoInventarioRepository.findByFechaRange(fechaInicio, fechaFin).stream()
            .map(inventarioMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> listarMovimientosOrdenCompra(Long ordenCompraId) {
        log.debug("Listando movimientos de orden de compra: {}", ordenCompraId);
        
        return movimientoInventarioRepository.findByOrdenCompraId(ordenCompraId).stream()
            .map(inventarioMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> listarMovimientosComprobanteVenta(Long comprobanteVentaId) {
        log.debug("Listando movimientos de comprobante de venta: {}", comprobanteVentaId);
        
        return movimientoInventarioRepository.findByComprobanteVentaId(comprobanteVentaId).stream()
            .map(inventarioMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovimientoInventarioDTO> obtenerUltimoMovimientoProducto(Long productoId) {
        log.debug("Obteniendo último movimiento del producto: {}", productoId);
        
        return movimientoInventarioRepository.findUltimoMovimientoByProductoId(productoId)
            .map(inventarioMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer calcularStockActual(Long productoId) {
        log.debug("Calculando stock actual del producto: {}", productoId);
        
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        return producto.getStock();
    }

    @Override
    @Transactional
    public void actualizarStockProducto(Long productoId, Integer nuevoStock) {
        log.info("Actualizando stock del producto {} a {}", productoId, nuevoStock);
        
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setStock(nuevoStock);
        productoRepository.save(producto);
        
        log.info("Stock del producto actualizado");
    }

    @Override
    @Transactional
    public MovimientoInventarioDTO registrarEntrada(MovimientoInventarioRequestDTO movimientoRequest) {
        log.info("Registrando entrada de inventario para producto: {}", movimientoRequest.getProductoId());
        
        movimientoRequest.setTipoMovimiento(TipoMovimiento.ENTRADA);
        return registrarMovimiento(movimientoRequest);
    }

    @Override
    @Transactional
    public MovimientoInventarioDTO registrarSalida(MovimientoInventarioRequestDTO movimientoRequest) {
        log.info("Registrando salida de inventario para producto: {}", movimientoRequest.getProductoId());
        
        movimientoRequest.setTipoMovimiento(TipoMovimiento.SALIDA);
        return registrarMovimiento(movimientoRequest);
    }

    @Override
    @Transactional
    public MovimientoInventarioDTO registrarAjuste(MovimientoInventarioRequestDTO movimientoRequest) {
        log.info("Registrando ajuste de inventario para producto: {}", movimientoRequest.getProductoId());
        
        movimientoRequest.setTipoMovimiento(TipoMovimiento.AJUSTE);
        return registrarMovimiento(movimientoRequest);
    }

    @Override
    @Transactional
    public MovimientoInventarioDTO registrarTransferencia(TransferenciaInventarioDTO transferenciaRequest) {
        log.info("Registrando transferencia de inventario para producto: {}", transferenciaRequest.getProductoId());
        
        // Crear movimiento de salida desde ubicación origen
        MovimientoInventarioRequestDTO salidaRequest = MovimientoInventarioRequestDTO.builder()
            .productoId(transferenciaRequest.getProductoId())
            .tipoMovimiento(TipoMovimiento.SALIDA)
            .cantidad(transferenciaRequest.getCantidad())
            .concepto("Transferencia - " + transferenciaRequest.getConcepto())
            .observaciones("De: " + transferenciaRequest.getUbicacionOrigen() + " - " + transferenciaRequest.getObservaciones())
            .fechaMovimiento(transferenciaRequest.getFechaTransferencia())
            .build();
        
        registrarMovimiento(salidaRequest);
        
        // Crear movimiento de entrada a ubicación destino
        MovimientoInventarioRequestDTO entradaRequest = MovimientoInventarioRequestDTO.builder()
            .productoId(transferenciaRequest.getProductoId())
            .tipoMovimiento(TipoMovimiento.ENTRADA)
            .cantidad(transferenciaRequest.getCantidad())
            .concepto("Transferencia - " + transferenciaRequest.getConcepto())
            .observaciones("A: " + transferenciaRequest.getUbicacionDestino() + " - " + transferenciaRequest.getObservaciones())
            .fechaMovimiento(transferenciaRequest.getFechaTransferencia())
            .build();
        
        return registrarMovimiento(entradaRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockBajoDTO> listarProductosConStockBajo() {
        log.debug("Listando productos con stock bajo");
        
        return productoRepository.findByStockLessThanEqualStockMinimo().stream()
            .map(inventarioMapper::toStockBajoDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenInventarioDTO obtenerResumenProducto(Long productoId, LocalDate fecha) {
        log.debug("Obteniendo resumen del producto {} para fecha: {}", productoId, fecha);
        
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        LocalDateTime fechaInicio = fecha.atStartOfDay();
        LocalDateTime fechaFin = fecha.plusDays(1).atStartOfDay();

        // Calcular totales del período
        Integer totalEntradas = movimientoInventarioRepository.sumCantidadByProductoAndTipoAndFechaRange(
            productoId, TipoMovimiento.ENTRADA, fechaInicio, fechaFin);
        Integer totalSalidas = movimientoInventarioRepository.sumCantidadByProductoAndTipoAndFechaRange(
            productoId, TipoMovimiento.SALIDA, fechaInicio, fechaFin);
        Long movimientosMes = movimientoInventarioRepository.countMovimientosByProductoAndFechaRange(
            productoId, fechaInicio, fechaFin);

        // Obtener último movimiento
        Optional<MovimientoInventario> ultimoMovimiento = movimientoInventarioRepository
            .findUltimoMovimientoByProductoId(productoId);

        return ResumenInventarioDTO.builder()
            .productoId(producto.getId())
            .productoNombre(producto.getNombre())
            .productoCodigo(producto.getCodigo())
            .stockActual(producto.getStock())
            .stockMinimo(producto.getStockMinimo())
            .stockMaximo(producto.getStockMaximo())
            .valorInventario(calcularValorInventarioProducto(productoId))
            .totalEntradas(totalEntradas != null ? totalEntradas : 0)
            .totalSalidas(totalSalidas != null ? totalSalidas : 0)
            .fechaUltimoMovimiento(ultimoMovimiento.map(m -> m.getFechaMovimiento().toLocalDate()).orElse(null))
            .ultimoMovimiento(ultimoMovimiento.map(m -> m.getFechaMovimiento()).orElse(null))
            .estadoStock(producto.isStockBajo() ? "BAJO" : "NORMAL")
            .precioPromedio(calcularPrecioPromedio(productoId))
            .movimientosMes(movimientosMes.intValue())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumenInventarioDTO> obtenerResumenGeneral(LocalDate fecha) {
        log.debug("Obteniendo resumen general de inventario para fecha: {}", fecha);
        
        return productoRepository.findAll().stream()
            .map(producto -> obtenerResumenProducto(producto.getId(), fecha))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularValorTotalInventario() {
        log.debug("Calculando valor total del inventario");
        
        return productoRepository.findAll().stream()
            .map(producto -> calcularValorInventarioProducto(producto.getId()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularValorInventarioProducto(Long productoId) {
        log.debug("Calculando valor del inventario del producto: {}", productoId);
        
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (producto.getPrecioCompra() != null) {
            return producto.getPrecioCompra().multiply(new BigDecimal(producto.getStock()));
        }
        
        return BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> obtenerEstadisticasMovimientos(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.debug("Obteniendo estadísticas de movimientos: {} - {}", fechaInicio, fechaFin);
        
        return movimientoInventarioRepository.findByFechaRange(fechaInicio, fechaFin).stream()
            .map(inventarioMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> listarMovimientosRecientes(int limite) {
        log.debug("Listando {} movimientos recientes", limite);
        
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(30);
        return movimientoInventarioRepository.findMovimientosRecientes(fechaLimite).stream()
            .limit(limite)
            .map(inventarioMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarStockSuficiente(Long productoId, Integer cantidad) {
        log.debug("Validando stock suficiente para producto {}: cantidad {}", productoId, cantidad);
        
        Integer stockActual = calcularStockActual(productoId);
        return stockActual >= cantidad;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> obtenerHistorialStock(Long productoId, LocalDate fechaInicio, LocalDate fechaFin) {
        log.debug("Obteniendo historial de stock del producto {}: {} - {}", productoId, fechaInicio, fechaFin);
        
        LocalDateTime fechaInicioDateTime = fechaInicio.atStartOfDay();
        LocalDateTime fechaFinDateTime = fechaFin.plusDays(1).atStartOfDay();
        
        return movimientoInventarioRepository.findByProductoIdAndFechaRange(
            productoId, fechaInicioDateTime, fechaFinDateTime).stream()
            .map(inventarioMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularPrecioPromedio(Long productoId) {
        log.debug("Calculando precio promedio del producto: {}", productoId);
        
        // En un sistema real, esto calcularía el precio promedio basándose en los movimientos de entrada
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        return producto.getPrecioCompra() != null ? producto.getPrecioCompra() : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumenInventarioDTO> obtenerProductosProximosVencer(int dias) {
        log.debug("Obteniendo productos próximos a vencer en {} días", dias);
        
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        
        return productoRepository.findByFechaVencimientoBeforeAndEstado(fechaLimite, com.nove.sule.backend_nove_sule.entity.enums.Estado.ACTIVO).stream()
            .map(producto -> inventarioMapper.toResumenDTO(producto))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioDTO> generarReporteMovimientos(Long productoId,
                                                                 TipoMovimiento tipoMovimiento,
                                                                 LocalDateTime fechaInicio,
                                                                 LocalDateTime fechaFin) {
        log.debug("Generando reporte de movimientos - producto: {}, tipo: {}", productoId, tipoMovimiento);
        
        return movimientoInventarioRepository.findByFilters(
            productoId, tipoMovimiento, fechaInicio, fechaFin, Pageable.unpaged()).getContent().stream()
            .map(inventarioMapper::toDTO)
            .toList();
    }
}
