package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.ComprobanteVenta;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoComprobante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para ComprobanteVenta
 */
@Repository
public interface ComprobanteVentaRepository extends JpaRepository<ComprobanteVenta, Long> {

    Optional<ComprobanteVenta> findByTipoComprobanteAndSerieAndNumero(
            TipoComprobante tipoComprobante, String serie, String numero);

    boolean existsByTipoComprobanteAndSerieAndNumero(
            TipoComprobante tipoComprobante, String serie, String numero);

    List<ComprobanteVenta> findByEstadoOrderByFechaEmisionDesc(Estado estado);

    Page<ComprobanteVenta> findByEstado(Estado estado, Pageable pageable);

    @Query("SELECT c FROM ComprobanteVenta c LEFT JOIN FETCH c.cliente LEFT JOIN FETCH c.detalles WHERE c.id = :id")
    Optional<ComprobanteVenta> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT c FROM ComprobanteVenta c WHERE " +
           "(:tipoComprobante IS NULL OR c.tipoComprobante = :tipoComprobante) AND " +
           "(:serie IS NULL OR c.serie = :serie) AND " +
           "(:numero IS NULL OR c.numero LIKE CONCAT('%', :numero, '%')) AND " +
           "(:clienteId IS NULL OR c.cliente.id = :clienteId) AND " +
           "(:fechaInicio IS NULL OR c.fechaEmision >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR c.fechaEmision <= :fechaFin) AND " +
           "(:estado IS NULL OR c.estado = :estado)")
    Page<ComprobanteVenta> findByFilters(@Param("tipoComprobante") TipoComprobante tipoComprobante,
                                        @Param("serie") String serie,
                                        @Param("numero") String numero,
                                        @Param("clienteId") Long clienteId,
                                        @Param("fechaInicio") LocalDateTime fechaInicio,
                                        @Param("fechaFin") LocalDateTime fechaFin,
                                        @Param("estado") Estado estado,
                                        Pageable pageable);

    @Query("SELECT COALESCE(MAX(c.numero), '00000000') FROM ComprobanteVenta c " +
           "WHERE c.tipoComprobante = :tipoComprobante AND c.serie = :serie")
    String findMaxNumeroByTipoAndSerie(@Param("tipoComprobante") TipoComprobante tipoComprobante,
                                       @Param("serie") String serie);

    @Query("SELECT SUM(c.total) FROM ComprobanteVenta c WHERE " +
           "c.fechaEmision >= :fechaInicio AND c.fechaEmision <= :fechaFin AND c.estado = 'ACTIVO'")
    BigDecimal sumTotalByFechaRange(@Param("fechaInicio") LocalDateTime fechaInicio,
                                    @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COUNT(c) FROM ComprobanteVenta c WHERE " +
           "c.fechaEmision >= :fechaInicio AND c.fechaEmision <= :fechaFin AND c.estado = 'ACTIVO'")
    Long countByFechaRange(@Param("fechaInicio") LocalDateTime fechaInicio,
                           @Param("fechaFin") LocalDateTime fechaFin);
}
