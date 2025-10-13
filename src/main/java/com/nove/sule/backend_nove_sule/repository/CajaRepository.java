package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.Caja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Caja
 */
@Repository
public interface CajaRepository extends JpaRepository<Caja, Long> {

    Optional<Caja> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Caja> findByEstadoOrderByNombreAsc(Caja.EstadoCaja estado);

    Page<Caja> findByEstado(Caja.EstadoCaja estado, Pageable pageable);

    @Query("SELECT c FROM Caja c LEFT JOIN FETCH c.responsable WHERE c.id = :id")
    Optional<Caja> findByIdWithResponsable(@Param("id") Long id);

    @Query("SELECT c FROM Caja c WHERE " +
           "(:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:estado IS NULL OR c.estado = :estado)")
    Page<Caja> findByFilters(@Param("nombre") String nombre,
                            @Param("estado") Caja.EstadoCaja estado,
                            Pageable pageable);

    @Query("SELECT c FROM Caja c WHERE c.responsable.id = :responsableId")
    List<Caja> findByResponsableId(@Param("responsableId") Long responsableId);

    @Query("SELECT c FROM Caja c WHERE c.estado = 'ABIERTA' ORDER BY c.nombre ASC")
    List<Caja> findCajasAbiertas();
}
