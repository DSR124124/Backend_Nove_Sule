package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.DetalleOrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para DetalleOrdenCompra
 */
@Repository
public interface DetalleOrdenCompraRepository extends JpaRepository<DetalleOrdenCompra, Long> {

    List<DetalleOrdenCompra> findByOrdenCompraId(Long ordenCompraId);

    @Query("SELECT d FROM DetalleOrdenCompra d LEFT JOIN FETCH d.producto WHERE d.ordenCompra.id = :ordenCompraId")
    List<DetalleOrdenCompra> findByOrdenCompraIdWithProducto(@Param("ordenCompraId") Long ordenCompraId);

    @Query("SELECT d FROM DetalleOrdenCompra d LEFT JOIN FETCH d.ordenCompra WHERE d.producto.id = :productoId")
    List<DetalleOrdenCompra> findByProductoIdWithOrdenCompra(@Param("productoId") Long productoId);
}
