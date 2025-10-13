package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.Usuario;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<Usuario> findByEstado(Estado estado);

    List<Usuario> findByRol(Rol rol);

    Page<Usuario> findByEstado(Estado estado, Pageable pageable);

    @Query("SELECT u FROM Usuario u WHERE " +
           "(COALESCE(:username, '') = '' OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
           "(COALESCE(:email, '') = '' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:rol IS NULL OR u.rol = :rol) AND " +
           "(:estado IS NULL OR u.estado = :estado)")
    Page<Usuario> findByFilters(@Param("username") String username,
                               @Param("email") String email,
                               @Param("rol") Rol rol,
                               @Param("estado") Estado estado,
                               Pageable pageable);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.empleado WHERE u.id = :id")
    Optional<Usuario> findByIdWithEmpleado(@Param("id") Long id);
}
