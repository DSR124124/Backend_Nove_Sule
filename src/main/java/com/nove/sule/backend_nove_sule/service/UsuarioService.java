package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.dto.usuario.UsuarioDTO;
import com.nove.sule.backend_nove_sule.entity.Usuario;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.Rol;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de Usuario
 */
public interface UsuarioService {

    /**
     * Crea un nuevo usuario
     */
    UsuarioDTO crear(UsuarioDTO usuarioDTO);

    /**
     * Actualiza un usuario existente
     */
    UsuarioDTO actualizar(Long id, UsuarioDTO usuarioDTO);

    /**
     * Busca un usuario por ID
     */
    Optional<UsuarioDTO> buscarPorId(Long id);

    /**
     * Busca un usuario por username
     */
    Optional<Usuario> buscarPorUsername(String username);

    /**
     * Busca un usuario por email
     */
    Optional<Usuario> buscarPorEmail(String email);

    /**
     * Lista todos los usuarios con paginación
     */
    PaginatedResponseDTO<UsuarioDTO> listarTodos(Pageable pageable);

    /**
     * Lista usuarios por estado
     */
    List<UsuarioDTO> listarPorEstado(Estado estado);

    /**
     * Lista usuarios por rol
     */
    List<UsuarioDTO> listarPorRol(Rol rol);

    /**
     * Busca usuarios con filtros
     */
    PaginatedResponseDTO<UsuarioDTO> buscarConFiltros(String username, String email, 
                                                     Rol rol, Estado estado, Pageable pageable);

    /**
     * Elimina un usuario (soft delete)
     */
    void eliminar(Long id);

    /**
     * Cambia el estado de un usuario
     */
    UsuarioDTO cambiarEstado(Long id, Estado estado);

    /**
     * Verifica si existe un username
     */
    boolean existeUsername(String username);

    /**
     * Verifica si existe un email
     */
    boolean existeEmail(String email);

    /**
     * Cambia la contraseña de un usuario
     */
    void cambiarPassword(Long id, String nuevaPassword);
}
