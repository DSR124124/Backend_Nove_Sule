package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.Proveedor;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Proveedor
 */
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Optional<Proveedor> findByRuc(String ruc);

    Optional<Proveedor> findByEmail(String email);

    boolean existsByRuc(String ruc);

    boolean existsByEmail(String email);

    List<Proveedor> findByEstadoOrderByNombreAsc(Estado estado);

    Page<Proveedor> findByEstado(Estado estado, Pageable pageable);

    @Query(value = "SELECT * FROM proveedores p WHERE " +
                   "(:nombre IS NULL OR p.nombre::text ILIKE CONCAT('%', :nombre, '%')) AND " +
                   "(:ruc IS NULL OR p.ruc ILIKE CONCAT('%', :ruc, '%')) AND " +
                   "(:email IS NULL OR p.email::text ILIKE CONCAT('%', :email, '%')) AND " +
                   "(:estado IS NULL OR p.estado = CAST(:estado AS VARCHAR))", 
           nativeQuery = true)
    Page<Proveedor> findByFilters(@Param("nombre") String nombre,
                                 @Param("ruc") String ruc,
                                 @Param("email") String email,
                                 @Param("estado") String estado,
                                 Pageable pageable);

    @Query("SELECT p FROM Proveedor p WHERE p.estado = 'ACTIVO' ORDER BY p.nombre ASC")
    List<Proveedor> findAllActiveOrderByNombre();
}
