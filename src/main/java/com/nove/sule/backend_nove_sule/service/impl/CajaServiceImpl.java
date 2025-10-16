package com.nove.sule.backend_nove_sule.service.impl;

import com.nove.sule.backend_nove_sule.dto.caja.*;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.*;
import com.nove.sule.backend_nove_sule.entity.enums.TipoMovimiento;
import com.nove.sule.backend_nove_sule.mapper.CajaMapper;
import com.nove.sule.backend_nove_sule.repository.*;
import com.nove.sule.backend_nove_sule.service.CajaService;
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
 * Implementación del servicio de Caja
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CajaServiceImpl implements CajaService {

    private final CajaRepository cajaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final CajaMapper cajaMapper;

    @Override
    @Transactional
    public CajaDTO crearCaja(CajaRequestDTO cajaRequest) {
        log.info("Creando caja: {}", cajaRequest.getNombre());

        // Validar que no exista una caja con el mismo nombre
        if (existeNombre(cajaRequest.getNombre())) {
            throw new RuntimeException("Ya existe una caja con el nombre: " + cajaRequest.getNombre());
        }

        // Validar responsable
        Empleado responsable = empleadoRepository.findById(cajaRequest.getResponsableId())
            .orElseThrow(() -> new RuntimeException("Empleado responsable no encontrado"));

        // Crear caja
        Caja caja = new Caja();
        caja.setNombre(cajaRequest.getNombre());
        caja.setDescripcion(cajaRequest.getDescripcion());
        caja.setSaldoInicial(cajaRequest.getSaldoInicial() != null ? cajaRequest.getSaldoInicial() : BigDecimal.ZERO);
        caja.setSaldoActual(caja.getSaldoInicial());
        caja.setFechaApertura(cajaRequest.getFechaApertura() != null ? cajaRequest.getFechaApertura() : LocalDate.now());
        caja.setResponsable(responsable);
        caja.setEstado(Caja.EstadoCaja.ABIERTA);

        caja = cajaRepository.save(caja);

        log.info("Caja creada con ID: {}", caja.getId());
        return cajaMapper.toDTO(caja);
    }

    @Override
    @Transactional
    public CajaDTO actualizarCaja(Long id, CajaRequestDTO cajaRequest) {
        log.info("Actualizando caja ID: {}", id);

        Caja caja = cajaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Caja no encontrada"));

        // Validar que no exista otra caja con el mismo nombre
        if (!caja.getNombre().equals(cajaRequest.getNombre()) && existeNombre(cajaRequest.getNombre())) {
            throw new RuntimeException("Ya existe una caja con el nombre: " + cajaRequest.getNombre());
        }

        // Validar responsable
        Empleado responsable = empleadoRepository.findById(cajaRequest.getResponsableId())
            .orElseThrow(() -> new RuntimeException("Empleado responsable no encontrado"));

        // Actualizar datos
        caja.setNombre(cajaRequest.getNombre());
        caja.setDescripcion(cajaRequest.getDescripcion());
        caja.setSaldoInicial(cajaRequest.getSaldoInicial() != null ? cajaRequest.getSaldoInicial() : caja.getSaldoInicial());
        caja.setFechaApertura(cajaRequest.getFechaApertura() != null ? cajaRequest.getFechaApertura() : caja.getFechaApertura());
        caja.setResponsable(responsable);

        caja = cajaRepository.save(caja);

        log.info("Caja actualizada: {}", id);
        return cajaMapper.toDTO(caja);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CajaDTO> buscarCajaPorId(Long id) {
        
        return cajaRepository.findByIdWithResponsable(id)
            .map(cajaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CajaDTO> buscarPorNombre(String nombre) {
        
        return cajaRepository.findByNombre(nombre)
            .map(cajaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<CajaDTO> listarCajas(Pageable pageable) {
        
        Page<Caja> pageCajas = cajaRepository.findAll(pageable);
        List<CajaDTO> cajas = pageCajas.getContent().stream()
            .map(cajaMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<CajaDTO>builder()
            .content(cajas)
            .page(pageCajas.getNumber())
            .size(pageCajas.getSize())
            .totalElements(pageCajas.getTotalElements())
            .totalPages(pageCajas.getTotalPages())
            .first(pageCajas.isFirst())
            .last(pageCajas.isLast())
            .empty(pageCajas.isEmpty())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaDTO> listarPorEstado(Caja.EstadoCaja estado) {
        
        return cajaRepository.findByEstadoOrderByNombreAsc(estado).stream()
            .map(cajaMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaDTO> listarCajasAbiertas() {
        
        return cajaRepository.findCajasAbiertas().stream()
            .map(cajaMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<CajaDTO> buscarConFiltros(String nombre, Caja.EstadoCaja estado, Pageable pageable) {
        
        Page<Caja> pageCajas = cajaRepository.findByFilters(nombre, estado, pageable);
        
        List<CajaDTO> cajas = pageCajas.getContent().stream()
            .map(cajaMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<CajaDTO>builder()
            .content(cajas)
            .page(pageCajas.getNumber())
            .size(pageCajas.getSize())
            .totalElements(pageCajas.getTotalElements())
            .totalPages(pageCajas.getTotalPages())
            .first(pageCajas.isFirst())
            .last(pageCajas.isLast())
            .empty(pageCajas.isEmpty())
            .build();
    }

    @Override
    @Transactional
    public CajaDTO abrirCaja(Long id, BigDecimal saldoInicial) {
        log.info("Abriendo caja ID: {} con saldo inicial: {}", id, saldoInicial);

        Caja caja = cajaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Caja no encontrada"));

        if (caja.getEstado() == Caja.EstadoCaja.ABIERTA) {
            throw new RuntimeException("La caja ya está abierta");
        }

        caja.setEstado(Caja.EstadoCaja.ABIERTA);
        caja.setSaldoInicial(saldoInicial != null ? saldoInicial : BigDecimal.ZERO);
        caja.setSaldoActual(caja.getSaldoInicial());
        caja.setFechaApertura(LocalDate.now());

        caja = cajaRepository.save(caja);

        log.info("Caja abierta: {}", id);
        return cajaMapper.toDTO(caja);
    }

    @Override
    @Transactional
    public CajaDTO cerrarCaja(Long id) {
        log.info("Cerrando caja ID: {}", id);

        Caja caja = cajaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Caja no encontrada"));

        if (caja.getEstado() == Caja.EstadoCaja.CERRADA) {
            throw new RuntimeException("La caja ya está cerrada");
        }

        caja.setEstado(Caja.EstadoCaja.CERRADA);
        caja = cajaRepository.save(caja);

        log.info("Caja cerrada: {}", id);
        return cajaMapper.toDTO(caja);
    }

    @Override
    @Transactional
    public CajaDTO cambiarEstado(Long id, Caja.EstadoCaja estado) {
        log.info("Cambiando estado de caja ID: {} a: {}", id, estado);

        Caja caja = cajaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Caja no encontrada"));

        caja.setEstado(estado);
        caja = cajaRepository.save(caja);

        log.info("Estado de caja actualizado: {} -> {}", id, estado);
        return cajaMapper.toDTO(caja);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaDTO> listarPorResponsable(Long responsableId) {
        
        return cajaRepository.findByResponsableId(responsableId).stream()
            .map(cajaMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeNombre(String nombre) {
        
        return cajaRepository.existsByNombre(nombre);
    }

    @Override
    @Transactional
    public MovimientoCajaDTO registrarMovimiento(MovimientoCajaRequestDTO movimientoRequest) {
        log.info("Registrando movimiento en caja ID: {}", movimientoRequest.getCajaId());

        Caja caja = cajaRepository.findById(movimientoRequest.getCajaId())
            .orElseThrow(() -> new RuntimeException("Caja no encontrada"));

        if (caja.getEstado() != Caja.EstadoCaja.ABIERTA) {
            throw new RuntimeException("No se pueden registrar movimientos en una caja cerrada");
        }

        // Calcular nuevo saldo
        BigDecimal saldoAnterior = caja.getSaldoActual();
        BigDecimal monto = movimientoRequest.getMonto();
        BigDecimal saldoNuevo;

        if (movimientoRequest.getTipoMovimiento() == TipoMovimiento.INGRESO) {
            saldoNuevo = saldoAnterior.add(monto);
        } else if (movimientoRequest.getTipoMovimiento() == TipoMovimiento.EGRESO) {
            if (saldoAnterior.compareTo(monto) < 0) {
                throw new RuntimeException("Saldo insuficiente para realizar el egreso");
            }
            saldoNuevo = saldoAnterior.subtract(monto);
        } else {
            throw new RuntimeException("Tipo de movimiento no válido para caja");
        }

        // Actualizar saldo de la caja
        caja.setSaldoActual(saldoNuevo);
        cajaRepository.save(caja);

        // Crear movimiento (simulado - en un sistema real se guardaría en una tabla de movimientos)
        MovimientoCajaDTO movimiento = MovimientoCajaDTO.builder()
            .cajaId(caja.getId())
            .cajaNombre(caja.getNombre())
            .tipoMovimiento(movimientoRequest.getTipoMovimiento())
            .monto(monto)
            .concepto(movimientoRequest.getConcepto())
            .observaciones(movimientoRequest.getObservaciones())
            .usuario("Sistema") // TODO: Obtener usuario actual
            .fechaMovimiento(LocalDateTime.now())
            .saldoAnterior(saldoAnterior)
            .saldoNuevo(saldoNuevo)
            .build();

        log.info("Movimiento registrado en caja {}: {} {}", caja.getId(), 
                movimientoRequest.getTipoMovimiento(), monto);
        return movimiento;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoCajaDTO> listarMovimientosCaja(Long cajaId) {
        
        // En un sistema real, esto consultaría la tabla de movimientos
        // Por ahora retornamos una lista vacía
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoCajaDTO> listarMovimientosPorFecha(Long cajaId, LocalDate fecha) {
        
        // En un sistema real, esto consultaría la tabla de movimientos por fecha
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoCajaDTO> listarMovimientosPorRangoFechas(Long cajaId, LocalDate fechaInicio, LocalDate fechaFin) {
        
        // En un sistema real, esto consultaría la tabla de movimientos por rango de fechas
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularSaldoActual(Long cajaId) {
        
        Caja caja = cajaRepository.findById(cajaId)
            .orElseThrow(() -> new RuntimeException("Caja no encontrada"));
        
        return caja.getSaldoActual();
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenCajaDTO obtenerResumenCaja(Long cajaId, LocalDate fecha) {
        
        Caja caja = cajaRepository.findById(cajaId)
            .orElseThrow(() -> new RuntimeException("Caja no encontrada"));

        // En un sistema real, esto calcularía los totales basándose en los movimientos reales
        BigDecimal totalIngresos = BigDecimal.ZERO; // TODO: Calcular desde movimientos
        BigDecimal totalEgresos = BigDecimal.ZERO; // TODO: Calcular desde movimientos

        return ResumenCajaDTO.builder()
            .cajaId(caja.getId())
            .cajaNombre(caja.getNombre())
            .fecha(fecha)
            .saldoInicial(caja.getSaldoInicial())
            .saldoFinal(caja.getSaldoActual())
            .totalIngresos(totalIngresos)
            .totalEgresos(totalEgresos)
            .saldoDiferencia(totalIngresos.subtract(totalEgresos))
            .totalMovimientos(0L) // TODO: Contar movimientos reales
            .ultimoMovimiento(null) // TODO: Obtener último movimiento real
            .estadoCaja(caja.getEstado().name())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumenCajaDTO> obtenerResumenTodasLasCajas(LocalDate fecha) {
        
        return cajaRepository.findAll().stream()
            .map(caja -> obtenerResumenCaja(caja.getId(), fecha))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalIngresos(Long cajaId, LocalDate fecha) {
        
        // En un sistema real, esto sumaría los movimientos de tipo INGRESO
        return BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalEgresos(Long cajaId, LocalDate fecha) {
        
        // En un sistema real, esto sumaría los movimientos de tipo EGRESO
        return BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MovimientoCajaDTO> obtenerUltimoMovimiento(Long cajaId) {
        
        // En un sistema real, esto consultaría el último movimiento de la tabla
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validarCajaActiva(Long cajaId) {
        
        return cajaRepository.findById(cajaId)
            .map(caja -> caja.getEstado() == Caja.EstadoCaja.ABIERTA)
            .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<MovimientoCajaDTO> listarMovimientosConPaginacion(Long cajaId, Pageable pageable) {
        
        // En un sistema real, esto consultaría la tabla de movimientos con paginación
        return PaginatedResponseDTO.<MovimientoCajaDTO>builder()
            .content(List.of())
            .page(0)
            .size(pageable.getPageSize())
            .totalElements(0L)
            .totalPages(0)
            .first(true)
            .last(true)
            .empty(true)
            .build();
    }
}
