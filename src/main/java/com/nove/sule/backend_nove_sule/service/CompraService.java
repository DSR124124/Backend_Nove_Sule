package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.dto.compras.*;
import com.nove.sule.backend_nove_sule.entity.enums.EstadoOrdenCompra;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de Compras
 */
public interface CompraService {

    /**
     * Crea una nueva orden de compra
     */
    OrdenCompraDTO crearOrdenCompra(OrdenCompraRequestDTO ordenRequest);

    /**
     * Actualiza una orden de compra existente
     */
    OrdenCompraDTO actualizarOrdenCompra(Long id, OrdenCompraRequestDTO ordenRequest);

    /**
     * Busca una orden de compra por ID
     */
    Optional<OrdenCompraDTO> buscarOrdenCompraPorId(Long id);

    /**
     * Busca una orden de compra por número
     */
    Optional<OrdenCompraDTO> buscarPorNumero(String numero);

    /**
     * Lista todas las órdenes de compra con paginación
     */
    PaginatedResponseDTO<OrdenCompraDTO> listarOrdenesCompra(Pageable pageable);

    /**
     * Lista órdenes de compra por estado
     */
    List<OrdenCompraDTO> listarPorEstado(EstadoOrdenCompra estado);

    /**
     * Busca órdenes de compra con filtros
     */
    PaginatedResponseDTO<OrdenCompraDTO> buscarConFiltros(String numero,
                                                          Long proveedorId,
                                                          LocalDate fechaInicio,
                                                          LocalDate fechaFin,
                                                          EstadoOrdenCompra estado,
                                                          Pageable pageable);

    /**
     * Cambia el estado de una orden de compra
     */
    OrdenCompraDTO cambiarEstado(Long id, EstadoOrdenCompra estado);

    /**
     * Cancela una orden de compra
     */
    OrdenCompraDTO cancelarOrdenCompra(Long id);

    /**
     * Marca una orden como entregada
     */
    OrdenCompraDTO marcarComoEntregada(Long id);

    /**
     * Lista órdenes de compra por proveedor
     */
    List<OrdenCompraDTO> listarPorProveedor(Long proveedorId);

    /**
     * Lista órdenes de compra por rango de fechas
     */
    List<OrdenCompraDTO> listarPorFecha(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Calcula el total de compras por rango de fechas
     */
    BigDecimal calcularTotalCompras(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Cuenta el número de órdenes de compra por rango de fechas
     */
    Long contarOrdenesCompra(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Genera el siguiente número de orden de compra
     */
    String generarNumeroOrdenCompra();

    /**
     * Verifica si existe un número de orden de compra
     */
    boolean existeNumeroOrdenCompra(String numero);

    /**
     * Obtiene un resumen de compras por rango de fechas
     */
    ResumenComprasDTO obtenerResumenCompras(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtiene las órdenes de compra pendientes
     */
    List<OrdenCompraDTO> obtenerOrdenesPendientes();

    /**
     * Obtiene las órdenes de compra vencidas
     */
    List<OrdenCompraDTO> obtenerOrdenesVencidas();
}
