package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.Producto;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Producto
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(String codigo);

    Optional<Producto> findByCodigoBarras(String codigoBarras);

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoBarras(String codigoBarras);

    List<Producto> findByEstadoOrderByNombreAsc(Estado estado);

    Page<Producto> findByEstado(Estado estado, Pageable pageable);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.categoria LEFT JOIN FETCH p.marca LEFT JOIN FETCH p.proveedor WHERE p.id = :id")
    Optional<Producto> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT p FROM Producto p WHERE " +
           "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:codigo IS NULL OR LOWER(p.codigo) LIKE LOWER(CONCAT('%', :codigo, '%'))) AND " +
           "(:categoriaId IS NULL OR p.categoria.id = :categoriaId) AND " +
           "(:marcaId IS NULL OR p.marca.id = :marcaId) AND " +
           "(:proveedorId IS NULL OR p.proveedor.id = :proveedorId) AND " +
           "(:estado IS NULL OR p.estado = :estado)")
    Page<Producto> findByFilters(@Param("nombre") String nombre,
                                @Param("codigo") String codigo,
                                @Param("categoriaId") Long categoriaId,
                                @Param("marcaId") Long marcaId,
                                @Param("proveedorId") Long proveedorId,
                                @Param("estado") Estado estado,
                                Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stockMinimo AND p.estado = 'ACTIVO'")
    List<Producto> findProductosConStockBajo();

    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId AND p.estado = 'ACTIVO'")
    List<Producto> findByCategoriaId(@Param("categoriaId") Long categoriaId);

    @Query("SELECT p FROM Producto p WHERE p.marca.id = :marcaId AND p.estado = 'ACTIVO'")
    List<Producto> findByMarcaId(@Param("marcaId") Long marcaId);

    @Query("SELECT p FROM Producto p WHERE p.proveedor.id = :proveedorId AND p.estado = 'ACTIVO'")
    List<Producto> findByProveedorId(@Param("proveedorId") Long proveedorId);

    @Query("SELECT p FROM Producto p WHERE " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))) AND " +
           "p.estado = 'ACTIVO'")
    List<Producto> findByTextoContaining(@Param("texto") String texto);

    @Query("SELECT p FROM Producto p WHERE " +
           "(:precioMin IS NULL OR p.precio >= :precioMin) AND " +
           "(:precioMax IS NULL OR p.precio <= :precioMax) AND " +
           "p.estado = 'ACTIVO'")
    List<Producto> findByRangoPrecio(@Param("precioMin") Double precioMin, 
                                     @Param("precioMax") Double precioMax);

    @Query("SELECT p FROM Producto p WHERE p.estado = 'ACTIVO' " +
           "ORDER BY p.nombre ASC")
    List<Producto> findMasVendidos(Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.stock <= p.stockMinimo")
    List<Producto> findByStockLessThanEqualStockMinimo();

    @Query("SELECT p FROM Producto p WHERE p.fechaVencimiento < :fechaLimite AND p.estado = :estado")
    List<Producto> findByFechaVencimientoBeforeAndEstado(@Param("fechaLimite") LocalDate fechaLimite, 
                                                         @Param("estado") Estado estado);
}
