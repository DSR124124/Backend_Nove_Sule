package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.caja.*;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Caja;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de Caja
 */
public interface CajaService {

    /**
     * Crea una nueva caja
     */
    CajaDTO crearCaja(CajaRequestDTO cajaRequest);

    /**
     * Actualiza una caja existente
     */
    CajaDTO actualizarCaja(Long id, CajaRequestDTO cajaRequest);

    /**
     * Busca una caja por ID
     */
    Optional<CajaDTO> buscarCajaPorId(Long id);

    /**
     * Busca una caja por nombre
     */
    Optional<CajaDTO> buscarPorNombre(String nombre);

    /**
     * Lista todas las cajas con paginación
     */
    PaginatedResponseDTO<CajaDTO> listarCajas(Pageable pageable);

    /**
     * Lista cajas por estado
     */
    List<CajaDTO> listarPorEstado(Caja.EstadoCaja estado);

    /**
     * Lista cajas abiertas
     */
    List<CajaDTO> listarCajasAbiertas();

    /**
     * Busca cajas con filtros
     */
    PaginatedResponseDTO<CajaDTO> buscarConFiltros(String nombre, Caja.EstadoCaja estado, Pageable pageable);

    /**
     * Abre una caja
     */
    CajaDTO abrirCaja(Long id, BigDecimal saldoInicial);

    /**
     * Cierra una caja
     */
    CajaDTO cerrarCaja(Long id);

    /**
     * Cambia el estado de una caja
     */
    CajaDTO cambiarEstado(Long id, Caja.EstadoCaja estado);

    /**
     * Lista cajas por responsable
     */
    List<CajaDTO> listarPorResponsable(Long responsableId);

    /**
     * Verifica si existe una caja con el nombre
     */
    boolean existeNombre(String nombre);

    /**
     * Registra un movimiento en la caja
     */
    MovimientoCajaDTO registrarMovimiento(MovimientoCajaRequestDTO movimientoRequest);

    /**
     * Lista movimientos de una caja
     */
    List<MovimientoCajaDTO> listarMovimientosCaja(Long cajaId);

    /**
     * Lista movimientos de una caja por fecha
     */
    List<MovimientoCajaDTO> listarMovimientosPorFecha(Long cajaId, LocalDate fecha);

    /**
     * Lista movimientos de una caja por rango de fechas
     */
    List<MovimientoCajaDTO> listarMovimientosPorRangoFechas(Long cajaId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Calcula el saldo actual de una caja
     */
    BigDecimal calcularSaldoActual(Long cajaId);

    /**
     * Obtiene el resumen de una caja
     */
    ResumenCajaDTO obtenerResumenCaja(Long cajaId, LocalDate fecha);

    /**
     * Obtiene el resumen de todas las cajas
     */
    List<ResumenCajaDTO> obtenerResumenTodasLasCajas(LocalDate fecha);

    /**
     * Calcula el total de ingresos de una caja en una fecha
     */
    BigDecimal calcularTotalIngresos(Long cajaId, LocalDate fecha);

    /**
     * Calcula el total de egresos de una caja en una fecha
     */
    BigDecimal calcularTotalEgresos(Long cajaId, LocalDate fecha);

    /**
     * Obtiene el último movimiento de una caja
     */
    Optional<MovimientoCajaDTO> obtenerUltimoMovimiento(Long cajaId);

    /**
     * Valida si una caja puede realizar movimientos
     */
    boolean validarCajaActiva(Long cajaId);

    /**
     * Obtiene el historial de movimientos con paginación
     */
    PaginatedResponseDTO<MovimientoCajaDTO> listarMovimientosConPaginacion(Long cajaId, Pageable pageable);
}
