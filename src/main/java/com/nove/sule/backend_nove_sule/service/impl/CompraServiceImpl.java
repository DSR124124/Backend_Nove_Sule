package com.nove.sule.backend_nove_sule.service.impl;

import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.dto.compras.*;
import com.nove.sule.backend_nove_sule.entity.*;
import com.nove.sule.backend_nove_sule.entity.enums.EstadoOrdenCompra;
import com.nove.sule.backend_nove_sule.mapper.CompraMapper;
import com.nove.sule.backend_nove_sule.repository.*;
import com.nove.sule.backend_nove_sule.service.CompraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Compras
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final CompraMapper compraMapper;

    @Override
    @Transactional
    public OrdenCompraDTO crearOrdenCompra(OrdenCompraRequestDTO ordenRequest) {
        log.info("Creando orden de compra para proveedor: {}", ordenRequest.getProveedorId());

        // Validar proveedor
        Proveedor proveedor = proveedorRepository.findById(ordenRequest.getProveedorId())
            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        // Crear orden de compra
        OrdenCompra ordenCompra = new OrdenCompra();
        ordenCompra.setNumero(generarNumeroOrdenCompra());
        ordenCompra.setProveedor(proveedor);
        ordenCompra.setFechaOrden(ordenRequest.getFechaOrden() != null ? ordenRequest.getFechaOrden() : LocalDate.now());
        ordenCompra.setFechaEntregaEsperada(ordenRequest.getFechaEntregaEsperada());
        ordenCompra.setEstado(EstadoOrdenCompra.PENDIENTE);
        ordenCompra.setDescuento(ordenRequest.getDescuento() != null ? ordenRequest.getDescuento() : BigDecimal.ZERO);
        ordenCompra.setObservaciones(ordenRequest.getObservaciones());
        // TODO: Implementar mapeo de usuario cuando esté disponible
        // ordenCompra.setUsuario(usuario);

        // Guardar orden
        ordenCompra = ordenCompraRepository.save(ordenCompra);

        // Guardar detalles
        if (ordenRequest.getDetalles() != null && !ordenRequest.getDetalles().isEmpty()) {
            for (OrdenCompraRequestDTO.DetalleOrdenCompraRequestDTO detalleRequest : ordenRequest.getDetalles()) {
                DetalleOrdenCompra detalle = new DetalleOrdenCompra();
                detalle.setOrdenCompra(ordenCompra);
                
                Producto producto = productoRepository.findById(detalleRequest.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                detalle.setProducto(producto);
                
                detalle.setCantidad(detalleRequest.getCantidad());
                detalle.setPrecioUnitario(detalleRequest.getPrecioUnitario());
                detalle.setDescuento(detalleRequest.getDescuento() != null ? detalleRequest.getDescuento() : BigDecimal.ZERO);
                
                // Calcular subtotal
                BigDecimal subtotal = detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad()));
                subtotal = subtotal.subtract(detalle.getDescuento());
                detalle.setSubtotal(subtotal.max(BigDecimal.ZERO));
                
                detalleOrdenCompraRepository.save(detalle);
            }
        }

        // Calcular totales
        ordenCompra.calcularTotales();
        ordenCompra = ordenCompraRepository.save(ordenCompra);

        log.info("Orden de compra creada con ID: {}", ordenCompra.getId());
        return compraMapper.toDTO(ordenCompra);
    }

    @Override
    @Transactional
    public OrdenCompraDTO actualizarOrdenCompra(Long id, OrdenCompraRequestDTO ordenRequest) {
        log.info("Actualizando orden de compra ID: {}", id);

        OrdenCompra ordenCompra = ordenCompraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));

        if (ordenCompra.getEstado() != EstadoOrdenCompra.PENDIENTE) {
            throw new RuntimeException("Solo se pueden actualizar órdenes en estado PENDIENTE");
        }

        // Validar proveedor
        Proveedor proveedor = proveedorRepository.findById(ordenRequest.getProveedorId())
            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        // Actualizar datos básicos
        ordenCompra.setProveedor(proveedor);
        ordenCompra.setFechaOrden(ordenRequest.getFechaOrden());
        ordenCompra.setFechaEntregaEsperada(ordenRequest.getFechaEntregaEsperada());
        ordenCompra.setDescuento(ordenRequest.getDescuento() != null ? ordenRequest.getDescuento() : BigDecimal.ZERO);
        ordenCompra.setObservaciones(ordenRequest.getObservaciones());

        // Eliminar detalles existentes
        ordenCompra.getDetalles().clear();

        // Agregar nuevos detalles
        if (ordenRequest.getDetalles() != null && !ordenRequest.getDetalles().isEmpty()) {
            for (OrdenCompraRequestDTO.DetalleOrdenCompraRequestDTO detalleRequest : ordenRequest.getDetalles()) {
                DetalleOrdenCompra detalle = new DetalleOrdenCompra();
                detalle.setOrdenCompra(ordenCompra);
                
                Producto producto = productoRepository.findById(detalleRequest.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                detalle.setProducto(producto);
                
                detalle.setCantidad(detalleRequest.getCantidad());
                detalle.setPrecioUnitario(detalleRequest.getPrecioUnitario());
                detalle.setDescuento(detalleRequest.getDescuento() != null ? detalleRequest.getDescuento() : BigDecimal.ZERO);
                
                // Calcular subtotal
                BigDecimal subtotal = detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad()));
                subtotal = subtotal.subtract(detalle.getDescuento());
                detalle.setSubtotal(subtotal.max(BigDecimal.ZERO));
                
                detalleOrdenCompraRepository.save(detalle);
            }
        }

        // Recalcular totales
        ordenCompra.calcularTotales();
        ordenCompra = ordenCompraRepository.save(ordenCompra);

        log.info("Orden de compra actualizada: {}", id);
        return compraMapper.toDTO(ordenCompra);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenCompraDTO> buscarOrdenCompraPorId(Long id) {
        
        return ordenCompraRepository.findByIdWithDetails(id)
            .map(compraMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenCompraDTO> buscarPorNumero(String numero) {
        
        return ordenCompraRepository.findByNumero(numero)
            .map(compraMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<OrdenCompraDTO> listarOrdenesCompra(Pageable pageable) {
        
        Page<OrdenCompra> pageOrdenes = ordenCompraRepository.findAll(pageable);
        List<OrdenCompraDTO> ordenes = pageOrdenes.getContent().stream()
            .map(compraMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<OrdenCompraDTO>builder()
            .content(ordenes)
            .page(pageOrdenes.getNumber())
            .size(pageOrdenes.getSize())
            .totalElements(pageOrdenes.getTotalElements())
            .totalPages(pageOrdenes.getTotalPages())
            .first(pageOrdenes.isFirst())
            .last(pageOrdenes.isLast())
            .empty(pageOrdenes.isEmpty())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarPorEstado(EstadoOrdenCompra estado) {
        
        return ordenCompraRepository.findByEstadoOrderByFechaOrdenDesc(estado).stream()
            .map(compraMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<OrdenCompraDTO> buscarConFiltros(String numero,
                                                                 Long proveedorId,
                                                                 LocalDate fechaInicio,
                                                                 LocalDate fechaFin,
                                                                 EstadoOrdenCompra estado,
                                                                 Pageable pageable) {
        
        Page<OrdenCompra> pageOrdenes = ordenCompraRepository.findByFilters(
            numero, proveedorId, fechaInicio, fechaFin, estado != null ? estado.name() : null, pageable);
        
        List<OrdenCompraDTO> ordenes = pageOrdenes.getContent().stream()
            .map(compraMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<OrdenCompraDTO>builder()
            .content(ordenes)
            .page(pageOrdenes.getNumber())
            .size(pageOrdenes.getSize())
            .totalElements(pageOrdenes.getTotalElements())
            .totalPages(pageOrdenes.getTotalPages())
            .first(pageOrdenes.isFirst())
            .last(pageOrdenes.isLast())
            .empty(pageOrdenes.isEmpty())
            .build();
    }

    @Override
    @Transactional
    public OrdenCompraDTO cambiarEstado(Long id, EstadoOrdenCompra estado) {
        log.info("Cambiando estado de orden de compra ID: {} a: {}", id, estado);

        OrdenCompra ordenCompra = ordenCompraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));

        ordenCompra.setEstado(estado);
        ordenCompra = ordenCompraRepository.save(ordenCompra);
        
        log.info("Estado de orden de compra actualizado: {} -> {}", id, estado);
        return compraMapper.toDTO(ordenCompra);
    }

    @Override
    @Transactional
    public OrdenCompraDTO cancelarOrdenCompra(Long id) {
        log.info("Cancelando orden de compra ID: {}", id);

        OrdenCompra ordenCompra = ordenCompraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));

        if (ordenCompra.getEstado() == EstadoOrdenCompra.CANCELADA) {
            throw new RuntimeException("La orden ya está cancelada");
        }

        ordenCompra.setEstado(EstadoOrdenCompra.CANCELADA);
        ordenCompra = ordenCompraRepository.save(ordenCompra);
        
        log.info("Orden de compra cancelada: {}", id);
        return compraMapper.toDTO(ordenCompra);
    }

    @Override
    @Transactional
    public OrdenCompraDTO marcarComoEntregada(Long id) {
        log.info("Marcando orden de compra como entregada ID: {}", id);

        OrdenCompra ordenCompra = ordenCompraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));

        if (ordenCompra.getEstado() != EstadoOrdenCompra.PENDIENTE) {
            throw new RuntimeException("Solo se pueden marcar como entregadas las órdenes pendientes");
        }

        ordenCompra.setEstado(EstadoOrdenCompra.RECIBIDA);
        ordenCompra = ordenCompraRepository.save(ordenCompra);
        
        log.info("Orden de compra marcada como entregada: {}", id);
        return compraMapper.toDTO(ordenCompra);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarPorProveedor(Long proveedorId) {
        
        return ordenCompraRepository.findByProveedorId(proveedorId).stream()
            .map(compraMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        
        return ordenCompraRepository.findByFilters(null, null, fechaInicio, fechaFin, null, Pageable.unpaged())
            .getContent().stream()
            .map(compraMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalCompras(LocalDate fechaInicio, LocalDate fechaFin) {
        
        return ordenCompraRepository.sumTotalByFechaRange(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarOrdenesCompra(LocalDate fechaInicio, LocalDate fechaFin) {
        
        return ordenCompraRepository.findByFilters(null, null, fechaInicio, fechaFin, null, Pageable.unpaged())
            .getTotalElements();
    }

    @Override
    @Transactional(readOnly = true)
    public String generarNumeroOrdenCompra() {
        
        String prefix = "OC-";
        Integer maxNumero = ordenCompraRepository.findMaxNumeroByPrefix(prefix);
        Integer siguienteNumero = maxNumero + 1;
        
        return prefix + String.format("%06d", siguienteNumero);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeNumeroOrdenCompra(String numero) {
        
        return ordenCompraRepository.existsByNumero(numero);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenComprasDTO obtenerResumenCompras(LocalDate fechaInicio, LocalDate fechaFin) {
        
        // Obtener totales
        Long totalOrdenes = contarOrdenesCompra(fechaInicio, fechaFin);
        BigDecimal totalCompras = calcularTotalCompras(fechaInicio, fechaFin);
        
        // Obtener órdenes por estado
        Long ordenesPendientes = ordenCompraRepository.findByFilters(null, null, fechaInicio, fechaFin, EstadoOrdenCompra.PENDIENTE.name(), Pageable.unpaged()).getTotalElements();
        Long ordenesEntregadas = ordenCompraRepository.findByFilters(null, null, fechaInicio, fechaFin, EstadoOrdenCompra.RECIBIDA.name(), Pageable.unpaged()).getTotalElements();
        Long ordenesCanceladas = ordenCompraRepository.findByFilters(null, null, fechaInicio, fechaFin, EstadoOrdenCompra.CANCELADA.name(), Pageable.unpaged()).getTotalElements();
        
        // Calcular subtotal e IGV (asumiendo 18% de IGV)
        BigDecimal totalSubtotal = totalCompras.divide(new BigDecimal("1.18"), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal totalIgv = totalCompras.subtract(totalSubtotal);
        
        // Calcular compra promedio
        BigDecimal compraPromedio = totalOrdenes > 0 ? 
            totalCompras.divide(new BigDecimal(totalOrdenes), 2, java.math.RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;
        
        return ResumenComprasDTO.builder()
            .fechaInicio(fechaInicio)
            .fechaFin(fechaFin)
            .totalOrdenes(totalOrdenes)
            .totalCompras(totalCompras)
            .totalSubtotal(totalSubtotal)
            .totalIgv(totalIgv)
            .totalDescuentos(BigDecimal.ZERO) // TODO: Implementar cálculo de descuentos
            .totalProductosComprados(0L) // TODO: Implementar cálculo de productos comprados
            .compraPromedio(compraPromedio)
            .ordenesPendientes(ordenesPendientes)
            .ordenesEntregadas(ordenesEntregadas)
            .ordenesCanceladas(ordenesCanceladas)
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> obtenerOrdenesPendientes() {
        
        return listarPorEstado(EstadoOrdenCompra.PENDIENTE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> obtenerOrdenesVencidas() {
        
        LocalDate hoy = LocalDate.now();
        return ordenCompraRepository.findByFilters(null, null, null, hoy, EstadoOrdenCompra.PENDIENTE.name(), Pageable.unpaged())
            .getContent().stream()
            .filter(orden -> orden.getFechaEntregaEsperada() != null && orden.getFechaEntregaEsperada().isBefore(hoy))
            .map(compraMapper::toDTO)
            .toList();
    }
}
