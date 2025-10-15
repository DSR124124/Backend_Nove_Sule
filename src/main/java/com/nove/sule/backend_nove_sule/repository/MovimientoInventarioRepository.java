package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.MovimientoInventario;
import com.nove.sule.backend_nove_sule.entity.enums.TipoMovimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para MovimientoInventario
 */
@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findByProductoIdOrderByFechaMovimientoDesc(Long productoId);

    List<MovimientoInventario> findByTipoMovimientoOrderByFechaMovimientoDesc(TipoMovimiento tipoMovimiento);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByProductoIdWithDetails(@Param("productoId") Long productoId);

    @Query("SELECT m FROM MovimientoInventario m WHERE " +
           "(:productoId IS NULL OR m.producto.id = :productoId) AND " +
           "(:tipoMovimiento IS NULL OR m.tipoMovimiento = :tipoMovimiento) AND " +
           "(:fechaInicio IS NULL OR m.fechaMovimiento >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR m.fechaMovimiento <= :fechaFin)")
    Page<MovimientoInventario> findByFilters(@Param("productoId") Long productoId,
                                           @Param("tipoMovimiento") TipoMovimiento tipoMovimiento,
                                           @Param("fechaInicio") LocalDateTime fechaInicio,
                                           @Param("fechaFin") LocalDateTime fechaFin,
                                           Pageable pageable);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId AND " +
           "m.fechaMovimiento >= :fechaInicio AND m.fechaMovimiento <= :fechaFin " +
           "ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByProductoIdAndFechaRange(@Param("productoId") Long productoId,
                                                           @Param("fechaInicio") LocalDateTime fechaInicio,
                                                           @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.ordenCompra.id = :ordenCompraId")
    List<MovimientoInventario> findByOrdenCompraId(@Param("ordenCompraId") Long ordenCompraId);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.comprobanteVenta.id = :comprobanteVentaId")
    List<MovimientoInventario> findByComprobanteVentaId(@Param("comprobanteVentaId") Long comprobanteVentaId);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
           "ORDER BY m.fechaMovimiento DESC LIMIT 1")
    Optional<MovimientoInventario> findUltimoMovimientoByProductoId(@Param("productoId") Long productoId);

    @Query("SELECT SUM(m.cantidad) FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
           "AND m.tipoMovimiento = :tipoMovimiento AND m.fechaMovimiento >= :fechaInicio " +
           "AND m.fechaMovimiento <= :fechaFin")
    Integer sumCantidadByProductoAndTipoAndFechaRange(@Param("productoId") Long productoId,
                                                    @Param("tipoMovimiento") TipoMovimiento tipoMovimiento,
                                                    @Param("fechaInicio") LocalDateTime fechaInicio,
                                                    @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COUNT(m) FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
           "AND m.fechaMovimiento >= :fechaInicio AND m.fechaMovimiento <= :fechaFin")
    Long countMovimientosByProductoAndFechaRange(@Param("productoId") Long productoId,
                                               @Param("fechaInicio") LocalDateTime fechaInicio,
                                               @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.fechaMovimiento >= :fechaInicio " +
           "AND m.fechaMovimiento <= :fechaFin ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByFechaRange(@Param("fechaInicio") LocalDateTime fechaInicio,
                                              @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.usuario.id = :usuarioId " +
           "ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
           "AND m.tipoMovimiento = :tipoMovimiento ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByProductoIdAndTipoMovimiento(@Param("productoId") Long productoId,
                                                               @Param("tipoMovimiento") TipoMovimiento tipoMovimiento);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.fechaMovimiento >= :fecha " +
           "ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findMovimientosRecientes(@Param("fecha") LocalDateTime fecha);
}
