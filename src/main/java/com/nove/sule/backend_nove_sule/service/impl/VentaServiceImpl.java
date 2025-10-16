package com.nove.sule.backend_nove_sule.service.impl;

import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.dto.ventas.*;
import com.nove.sule.backend_nove_sule.entity.*;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoComprobante;
import com.nove.sule.backend_nove_sule.mapper.VentaMapper;
import com.nove.sule.backend_nove_sule.repository.*;
import com.nove.sule.backend_nove_sule.service.VentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Ventas
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final ComprobanteVentaRepository comprobanteVentaRepository;
    private final DetalleComprobanteRepository detalleComprobanteRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final VentaMapper ventaMapper;

    @Override
    @Transactional
    public ComprobanteVentaDTO crearComprobante(ComprobanteVentaDTO comprobanteDTO) {
        log.info("Creando comprobante de venta: {} {}", comprobanteDTO.getTipoComprobante(), comprobanteDTO.getSerie());

        // Validar cliente
        Cliente cliente = clienteRepository.findById(comprobanteDTO.getCliente().getId())
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Crear comprobante
        ComprobanteVenta comprobante = new ComprobanteVenta();
        comprobante.setTipoComprobante(comprobanteDTO.getTipoComprobante());
        comprobante.setSerie(comprobanteDTO.getSerie());
        comprobante.setNumero(comprobanteDTO.getNumero());
        comprobante.setCliente(cliente);
        comprobante.setFechaEmision(comprobanteDTO.getFechaEmision() != null ? comprobanteDTO.getFechaEmision() : LocalDateTime.now());
        comprobante.setMoneda(comprobanteDTO.getMoneda());
        comprobante.setTipoCambio(comprobanteDTO.getTipoCambio() != null ? comprobanteDTO.getTipoCambio() : BigDecimal.ONE);
        comprobante.setDescuento(comprobanteDTO.getDescuento() != null ? comprobanteDTO.getDescuento() : BigDecimal.ZERO);
        comprobante.setEstado(comprobanteDTO.getEstado() != null ? comprobanteDTO.getEstado() : Estado.ACTIVO);
        comprobante.setMedioPago(comprobanteDTO.getMedioPago());
        comprobante.setObservaciones(comprobanteDTO.getObservaciones());
        // TODO: Implementar mapeo de usuario y caja cuando estén disponibles
        // comprobante.setUsuario(usuario);
        // comprobante.setCaja(caja);

        // Calcular totales
        calcularTotales(comprobante, comprobanteDTO.getDetalles());

        // Guardar comprobante
        comprobante = comprobanteVentaRepository.save(comprobante);

        // Guardar detalles
        if (comprobanteDTO.getDetalles() != null && !comprobanteDTO.getDetalles().isEmpty()) {
            for (ComprobanteVentaDTO.DetalleComprobanteDTO detalleDTO : comprobanteDTO.getDetalles()) {
                DetalleComprobante detalle = new DetalleComprobante();
                detalle.setComprobante(comprobante);
                
                Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                detalle.setProducto(producto);
                
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setDescuento(detalleDTO.getDescuento() != null ? detalleDTO.getDescuento() : BigDecimal.ZERO);
                
                // Calcular subtotal
                BigDecimal subtotal = detalle.getCantidad().multiply(detalle.getPrecioUnitario()).subtract(detalle.getDescuento());
                detalle.setSubtotal(subtotal.max(BigDecimal.ZERO));
                
                detalleComprobanteRepository.save(detalle);
            }
        }

        log.info("Comprobante creado con ID: {}", comprobante.getId());
        return ventaMapper.toDTO(comprobante);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobanteVentaDTO> buscarComprobantePorId(Long id) {
        
        return comprobanteVentaRepository.findById(id)
            .map(ventaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobanteVentaDTO> buscarPorTipoSerieNumero(TipoComprobante tipo, String serie, String numero) {
        
        return comprobanteVentaRepository.findByTipoComprobanteAndSerieAndNumero(tipo, serie, numero)
            .map(ventaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<ComprobanteVentaDTO> listarComprobantes(Pageable pageable) {
        
        Page<ComprobanteVenta> pageComprobantes = comprobanteVentaRepository.findAll(pageable);
        List<ComprobanteVentaDTO> comprobantes = pageComprobantes.getContent().stream()
            .map(ventaMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<ComprobanteVentaDTO>builder()
            .content(comprobantes)
            .page(pageComprobantes.getNumber())
            .size(pageComprobantes.getSize())
            .totalElements(pageComprobantes.getTotalElements())
            .totalPages(pageComprobantes.getTotalPages())
            .first(pageComprobantes.isFirst())
            .last(pageComprobantes.isLast())
            .empty(pageComprobantes.isEmpty())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteVentaDTO> listarPorEstado(Estado estado) {
        
        return comprobanteVentaRepository.findByEstadoOrderByFechaEmisionDesc(estado).stream()
            .map(ventaMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<ComprobanteVentaDTO> buscarConFiltros(TipoComprobante tipoComprobante,
                                                                      String serie, String numero,
                                                                      Long clienteId,
                                                                      LocalDateTime fechaInicio,
                                                                      LocalDateTime fechaFin,
                                                                      Estado estado,
                                                                      Pageable pageable) {
        
        Page<ComprobanteVenta> pageComprobantes = comprobanteVentaRepository.findByFilters(
            tipoComprobante != null ? tipoComprobante.name() : null, serie, numero, clienteId, fechaInicio, fechaFin, 
            estado != null ? estado.name() : null, pageable);
        
        List<ComprobanteVentaDTO> comprobantes = pageComprobantes.getContent().stream()
            .map(ventaMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<ComprobanteVentaDTO>builder()
            .content(comprobantes)
            .page(pageComprobantes.getNumber())
            .size(pageComprobantes.getSize())
            .totalElements(pageComprobantes.getTotalElements())
            .totalPages(pageComprobantes.getTotalPages())
            .first(pageComprobantes.isFirst())
            .last(pageComprobantes.isLast())
            .empty(pageComprobantes.isEmpty())
            .build();
    }

    @Override
    @Transactional
    public ComprobanteVentaDTO anularComprobante(Long id) {
        log.info("Anulando comprobante ID: {}", id);

        ComprobanteVenta comprobante = comprobanteVentaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comprobante no encontrado con ID: " + id));

        if (comprobante.getEstado() == Estado.INACTIVO) {
            throw new RuntimeException("El comprobante ya está anulado");
        }

        comprobante.setEstado(Estado.INACTIVO);
        comprobante = comprobanteVentaRepository.save(comprobante);
        
        log.info("Comprobante anulado: {}", id);
        return ventaMapper.toDTO(comprobante);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteVentaDTO> listarPorCliente(Long clienteId) {
        
        return comprobanteVentaRepository.findByClienteIdOrderByFechaEmisionDesc(clienteId).stream()
            .map(ventaMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComprobanteVentaDTO> listarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        
        return comprobanteVentaRepository.findByFechaEmisionBetweenOrderByFechaEmisionDesc(fechaInicio, fechaFin).stream()
            .map(ventaMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalVentas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        
        return comprobanteVentaRepository.calcularTotalVentas(fechaInicio, fechaFin, Estado.ACTIVO);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarComprobantes(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        
        return comprobanteVentaRepository.contarComprobantes(fechaInicio, fechaFin, Estado.ACTIVO);
    }

    @Override
    @Transactional(readOnly = true)
    public String generarNumeroComprobante(TipoComprobante tipoComprobante, String serie) {
        
        String maxNumero = comprobanteVentaRepository.findMaxNumeroByTipoAndSerie(tipoComprobante, serie);
        if (maxNumero == null || maxNumero.equals("00000000")) {
            return "00000001";
        }
        
        try {
            Long numero = Long.parseLong(maxNumero) + 1;
            return String.format("%08d", numero);
        } catch (NumberFormatException e) {
            log.warn("Error parseando número de comprobante: {}", maxNumero);
            return "00000001";
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeNumeroComprobante(TipoComprobante tipoComprobante, String serie, String numero) {
        
        return comprobanteVentaRepository.existsByTipoComprobanteAndSerieAndNumero(tipoComprobante, serie, numero);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenVentasDTO obtenerResumenVentas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        
        // Obtener totales de comprobantes activos
        Long totalComprobantes = comprobanteVentaRepository.contarComprobantes(fechaInicio, fechaFin, Estado.ACTIVO);
        BigDecimal totalVentas = comprobanteVentaRepository.calcularTotalVentas(fechaInicio, fechaFin, Estado.ACTIVO);
        
        // Obtener totales de comprobantes anulados
        Long comprobantesAnulados = comprobanteVentaRepository.contarComprobantes(fechaInicio, fechaFin, Estado.INACTIVO);
        BigDecimal ventasAnuladas = comprobanteVentaRepository.calcularTotalVentas(fechaInicio, fechaFin, Estado.INACTIVO);
        
        // Calcular subtotal e IGV (asumiendo 18% de IGV)
        BigDecimal totalSubtotal = totalVentas.divide(new BigDecimal("1.18"), 2, java.math.RoundingMode.HALF_UP);
        BigDecimal totalIgv = totalVentas.subtract(totalSubtotal);
        
        // Calcular ticket promedio
        BigDecimal ticketPromedio = totalComprobantes > 0 ? 
            totalVentas.divide(new BigDecimal(totalComprobantes), 2, java.math.RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;
        
        return ResumenVentasDTO.builder()
            .fechaInicio(fechaInicio)
            .fechaFin(fechaFin)
            .totalComprobantes(totalComprobantes)
            .totalVentas(totalVentas)
            .totalSubtotal(totalSubtotal)
            .totalIgv(totalIgv)
            .totalDescuentos(BigDecimal.ZERO) // TODO: Implementar cálculo de descuentos
            .totalProductosVendidos(0L) // TODO: Implementar cálculo de productos vendidos
            .ticketPromedio(ticketPromedio)
            .comprobantesAnulados(comprobantesAnulados)
            .ventasAnuladas(ventasAnuladas)
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoVendidoDTO> obtenerProductosMasVendidos(LocalDateTime fechaInicio, LocalDateTime fechaFin, int limite) {
        
        // TODO: Implementar consulta para productos más vendidos
        // Por ahora retornamos una lista vacía
        return List.of();
    }

    // ===== MÉTODOS PRIVADOS =====

    private void calcularTotales(ComprobanteVenta comprobante, List<ComprobanteVentaDTO.DetalleComprobanteDTO> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            comprobante.setSubtotal(BigDecimal.ZERO);
            comprobante.setIgv(BigDecimal.ZERO);
            comprobante.setTotal(BigDecimal.ZERO);
            return;
        }

        // Calcular subtotal
        BigDecimal subtotal = detalles.stream()
            .map(detalle -> {
                BigDecimal subtotalDetalle = detalle.getCantidad().multiply(detalle.getPrecioUnitario());
                if (detalle.getDescuento() != null) {
                    subtotalDetalle = subtotalDetalle.subtract(detalle.getDescuento());
                }
                return subtotalDetalle.max(BigDecimal.ZERO);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Aplicar descuento general
        if (comprobante.getDescuento() != null) {
            subtotal = subtotal.subtract(comprobante.getDescuento());
        }
        subtotal = subtotal.max(BigDecimal.ZERO);

        // Calcular IGV (18%)
        BigDecimal igv = subtotal.multiply(new BigDecimal("0.18"));

        // Calcular total
        BigDecimal total = subtotal.add(igv);

        comprobante.setSubtotal(subtotal);
        comprobante.setIgv(igv);
        comprobante.setTotal(total);
    }
}
