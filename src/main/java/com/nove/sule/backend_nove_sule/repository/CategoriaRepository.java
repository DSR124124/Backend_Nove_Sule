package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.Categoria;
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
 * Repositorio para Categoria
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Categoria> findByEstadoOrderByOrdenAsc(Estado estado);

    Page<Categoria> findByEstado(Estado estado, Pageable pageable);

    @Query("SELECT c FROM Categoria c WHERE " +
           "(:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:estado IS NULL OR c.estado = :estado)")
    Page<Categoria> findByFilters(@Param("nombre") String nombre,
                                 @Param("estado") Estado estado,
                                 Pageable pageable);

    @Query("SELECT c FROM Categoria c WHERE c.estado = :estado ORDER BY c.orden ASC, c.nombre ASC")
    List<Categoria> findActiveCategoriesOrdered(@Param("estado") Estado estado);

    @Query("SELECT MAX(c.orden) FROM Categoria c")
    Integer findMaxOrden();
}
