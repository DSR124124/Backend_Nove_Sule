package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.DetalleComprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para DetalleComprobante
 */
@Repository
public interface DetalleComprobanteRepository extends JpaRepository<DetalleComprobante, Long> {

    List<DetalleComprobante> findByComprobanteId(Long comprobanteId);

    @Query("SELECT d FROM DetalleComprobante d LEFT JOIN FETCH d.producto WHERE d.comprobante.id = :comprobanteId")
    List<DetalleComprobante> findByComprobanteIdWithProducto(@Param("comprobanteId") Long comprobanteId);

    @Query("SELECT d FROM DetalleComprobante d LEFT JOIN FETCH d.comprobante WHERE d.producto.id = :productoId")
    List<DetalleComprobante> findByProductoIdWithComprobante(@Param("productoId") Long productoId);
}
