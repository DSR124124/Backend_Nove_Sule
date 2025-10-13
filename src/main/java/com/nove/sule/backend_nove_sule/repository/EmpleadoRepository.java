package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.Empleado;
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
 * Repositorio para Empleado
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    Optional<Empleado> findByDni(String dni);

    boolean existsByDni(String dni);

    List<Empleado> findByEstado(Estado estado);

    Page<Empleado> findByEstado(Estado estado, Pageable pageable);

    @Query("SELECT e FROM Empleado e WHERE " +
           "(:nombres IS NULL OR LOWER(e.nombres) LIKE LOWER(CONCAT('%', :nombres, '%'))) AND " +
           "(:apellidos IS NULL OR LOWER(e.apellidos) LIKE LOWER(CONCAT('%', :apellidos, '%'))) AND " +
           "(:dni IS NULL OR e.dni LIKE CONCAT('%', :dni, '%')) AND " +
           "(:cargo IS NULL OR LOWER(e.cargo) LIKE LOWER(CONCAT('%', :cargo, '%'))) AND " +
           "(:estado IS NULL OR e.estado = :estado)")
    Page<Empleado> findByFilters(@Param("nombres") String nombres,
                                @Param("apellidos") String apellidos,
                                @Param("dni") String dni,
                                @Param("cargo") String cargo,
                                @Param("estado") Estado estado,
                                Pageable pageable);

    @Query("SELECT e FROM Empleado e LEFT JOIN FETCH e.usuario WHERE e.id = :id")
    Optional<Empleado> findByIdWithUsuario(@Param("id") Long id);

    List<Empleado> findByCargo(String cargo);
}
