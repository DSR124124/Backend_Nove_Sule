package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.dto.ventas.ComprobanteVentaDTO;
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
    ComprobanteVentaDTO anularComprobante(Long id, String motivo);

    /**
     * Genera el siguiente número para un tipo y serie
     */
    String generarSiguienteNumero(TipoComprobante tipo, String serie);

    /**
     * Calcula el total de ventas por rango de fechas
     */
    BigDecimal calcularTotalVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Cuenta el número de ventas por rango de fechas
     */
    Long contarVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Verifica disponibilidad de stock antes de vender
     */
    boolean verificarDisponibilidadStock(List<ComprobanteVentaDTO.DetalleComprobanteDTO> detalles);

    /**
     * Actualiza el stock después de una venta
     */
    void actualizarStockPorVenta(Long comprobanteId);
}
