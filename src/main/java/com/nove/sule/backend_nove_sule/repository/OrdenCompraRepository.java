package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.OrdenCompra;
import com.nove.sule.backend_nove_sule.entity.enums.EstadoOrdenCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para OrdenCompra
 */
@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {

    Optional<OrdenCompra> findByNumero(String numero);

    boolean existsByNumero(String numero);

    List<OrdenCompra> findByEstadoOrderByFechaOrdenDesc(EstadoOrdenCompra estado);

    Page<OrdenCompra> findByEstado(EstadoOrdenCompra estado, Pageable pageable);

    @Query("SELECT o FROM OrdenCompra o LEFT JOIN FETCH o.proveedor LEFT JOIN FETCH o.detalles WHERE o.id = :id")
    Optional<OrdenCompra> findByIdWithDetails(@Param("id") Long id);

    @Query(value = "SELECT oc.* FROM ordenes_compra oc " +
           "WHERE (:numero IS NULL OR oc.numero LIKE CONCAT('%', :numero, '%')) " +
           "AND (:proveedorId IS NULL OR oc.proveedor_id = :proveedorId) " +
           "AND (:fechaInicio IS NULL OR oc.fecha_orden >= :fechaInicio) " +
           "AND (:fechaFin IS NULL OR oc.fecha_orden <= :fechaFin) " +
           "AND (:estado IS NULL OR oc.estado = :estado) " +
           "ORDER BY oc.fecha_orden DESC",
           countQuery = "SELECT COUNT(*) FROM ordenes_compra oc " +
           "WHERE (:numero IS NULL OR oc.numero LIKE CONCAT('%', :numero, '%')) " +
           "AND (:proveedorId IS NULL OR oc.proveedor_id = :proveedorId) " +
           "AND (:fechaInicio IS NULL OR oc.fecha_orden >= :fechaInicio) " +
           "AND (:fechaFin IS NULL OR oc.fecha_orden <= :fechaFin) " +
           "AND (:estado IS NULL OR oc.estado = :estado)",
           nativeQuery = true)
    Page<OrdenCompra> findByFilters(@Param("numero") String numero,
                                   @Param("proveedorId") Long proveedorId,
                                   @Param("fechaInicio") LocalDate fechaInicio,
                                   @Param("fechaFin") LocalDate fechaFin,
                                   @Param("estado") String estado,
                                   Pageable pageable);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(o.numero, 4) AS INTEGER)), 0) FROM OrdenCompra o " +
           "WHERE o.numero LIKE CONCAT(:prefix, '%')")
    Integer findMaxNumeroByPrefix(@Param("prefix") String prefix);

    @Query("SELECT o FROM OrdenCompra o WHERE o.proveedor.id = :proveedorId")
    List<OrdenCompra> findByProveedorId(@Param("proveedorId") Long proveedorId);

    @Query("SELECT SUM(o.total) FROM OrdenCompra o WHERE " +
           "o.fechaOrden >= :fechaInicio AND o.fechaOrden <= :fechaFin AND o.estado != 'CANCELADA'")
    BigDecimal sumTotalByFechaRange(@Param("fechaInicio") LocalDate fechaInicio,
                                    @Param("fechaFin") LocalDate fechaFin);
}
