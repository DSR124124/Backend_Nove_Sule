package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.dto.ventas.ComprobanteVentaDTO;
import com.nove.sule.backend_nove_sule.dto.ventas.ProductoVendidoDTO;
import com.nove.sule.backend_nove_sule.dto.ventas.ResumenVentasDTO;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoComprobante;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de Ventas
 */
public interface VentaService {

    /**
     * Crea un nuevo comprobante de venta
     */
    ComprobanteVentaDTO crearComprobante(ComprobanteVentaDTO comprobanteDTO);

    /**
     * Busca un comprobante por ID con detalles
     */
    Optional<ComprobanteVentaDTO> buscarComprobantePorId(Long id);

    /**
     * Busca un comprobante por tipo, serie y número
     */
    Optional<ComprobanteVentaDTO> buscarPorTipoSerieNumero(TipoComprobante tipo, String serie, String numero);

    /**
     * Lista todos los comprobantes con paginación
     */
    PaginatedResponseDTO<ComprobanteVentaDTO> listarComprobantes(Pageable pageable);

    /**
     * Lista comprobantes por estado
     */
    List<ComprobanteVentaDTO> listarPorEstado(Estado estado);

    /**
     * Busca comprobantes con filtros
     */
    PaginatedResponseDTO<ComprobanteVentaDTO> buscarConFiltros(TipoComprobante tipoComprobante,
                                                              String serie, String numero,
                                                              Long clienteId,
                                                              LocalDateTime fechaInicio,
                                                              LocalDateTime fechaFin,
                                                              Estado estado,
                                                              Pageable pageable);

    /**
     * Anula un comprobante de venta
     */
    ComprobanteVentaDTO anularComprobante(Long id);

    /**
     * Lista comprobantes por cliente
     */
    List<ComprobanteVentaDTO> listarPorCliente(Long clienteId);

    /**
     * Lista comprobantes por rango de fechas
     */
    List<ComprobanteVentaDTO> listarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Calcula el total de ventas por rango de fechas
     */
    BigDecimal calcularTotalVentas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Cuenta el número de comprobantes por rango de fechas
     */
    Long contarComprobantes(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Genera el siguiente número de comprobante para un tipo y serie
     */
    String generarNumeroComprobante(TipoComprobante tipoComprobante, String serie);

    /**
     * Verifica si existe un número de comprobante
     */
    boolean existeNumeroComprobante(TipoComprobante tipoComprobante, String serie, String numero);

    /**
     * Obtiene un resumen de ventas por rango de fechas
     */
    ResumenVentasDTO obtenerResumenVentas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Obtiene los productos más vendidos en un rango de fechas
     */
    List<ProductoVendidoDTO> obtenerProductosMasVendidos(LocalDateTime fechaInicio, LocalDateTime fechaFin, int limite);
}
