package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.Marca;
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
 * Repositorio para Marca
 */
@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    Optional<Marca> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Marca> findByEstadoOrderByNombreAsc(Estado estado);

    Page<Marca> findByEstado(Estado estado, Pageable pageable);

    @Query("SELECT m FROM Marca m WHERE " +
           "(:nombre IS NULL OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:estado IS NULL OR m.estado = :estado)")
    Page<Marca> findByFilters(@Param("nombre") String nombre,
                             @Param("estado") Estado estado,
                             Pageable pageable);

    @Query("SELECT m FROM Marca m WHERE m.estado = 'ACTIVO' ORDER BY m.nombre ASC")
    List<Marca> findAllActiveOrderByNombre();
}
