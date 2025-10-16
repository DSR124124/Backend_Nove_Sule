package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.catalogo.ProveedorBasicoDTO;
import com.nove.sule.backend_nove_sule.dto.catalogo.ProveedorRequestDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Proveedor;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de Proveedor
 */
public interface ProveedorService {

    /**
     * Crea un nuevo proveedor
     */
    ProveedorBasicoDTO crear(ProveedorRequestDTO proveedorRequest);

    /**
     * Actualiza un proveedor existente
     */
    ProveedorBasicoDTO actualizar(Long id, ProveedorRequestDTO proveedorRequest);

    /**
     * Busca un proveedor por ID
     */
    Optional<ProveedorBasicoDTO> buscarPorId(Long id);

    /**
     * Busca un proveedor por RUC
     */
    Optional<Proveedor> buscarPorRuc(String ruc);

    /**
     * Lista todos los proveedores con paginación
     */
    PaginatedResponseDTO<ProveedorBasicoDTO> listarTodos(Pageable pageable);

    /**
     * Lista proveedores por estado
     */
    List<ProveedorBasicoDTO> listarPorEstado(Estado estado);

    /**
     * Lista proveedores activos
     */
    List<ProveedorBasicoDTO> listarActivos();

    /**
     * Busca proveedores con filtros
     */
    PaginatedResponseDTO<ProveedorBasicoDTO> buscarConFiltros(String nombre, String ruc, String email, Estado estado, Pageable pageable);

    /**
     * Elimina un proveedor (soft delete)
     */
    void eliminar(Long id);

    /**
     * Cambia el estado de un proveedor
     */
    ProveedorBasicoDTO cambiarEstado(Long id, Estado estado);

    /**
     * Verifica si existe un RUC
     */
    boolean existeRuc(String ruc);

    /**
     * Verifica si existe un email
     */
    boolean existeEmail(String email);
}
