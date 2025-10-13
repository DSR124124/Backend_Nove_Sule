package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.catalogo.CategoriaBasicaDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Categoria;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de Categoria
 */
public interface CategoriaService {

    /**
     * Crea una nueva categoría
     */
    CategoriaBasicaDTO crear(CategoriaBasicaDTO categoriaDTO);

    /**
     * Actualiza una categoría existente
     */
    CategoriaBasicaDTO actualizar(Long id, CategoriaBasicaDTO categoriaDTO);

    /**
     * Busca una categoría por ID
     */
    Optional<CategoriaBasicaDTO> buscarPorId(Long id);

    /**
     * Busca una categoría por nombre
     */
    Optional<Categoria> buscarPorNombre(String nombre);

    /**
     * Lista todas las categorías con paginación
     */
    PaginatedResponseDTO<CategoriaBasicaDTO> listarTodos(Pageable pageable);

    /**
     * Lista categorías por estado ordenadas
     */
    List<CategoriaBasicaDTO> listarPorEstado(Estado estado);

    /**
     * Lista categorías activas ordenadas
     */
    List<CategoriaBasicaDTO> listarActivas();

    /**
     * Busca categorías con filtros
     */
    PaginatedResponseDTO<CategoriaBasicaDTO> buscarConFiltros(String nombre, Estado estado, Pageable pageable);

    /**
     * Elimina una categoría (soft delete)
     */
    void eliminar(Long id);

    /**
     * Cambia el estado de una categoría
     */
    CategoriaBasicaDTO cambiarEstado(Long id, Estado estado);

    /**
     * Verifica si existe un nombre
     */
    boolean existeNombre(String nombre);

    /**
     * Actualiza el orden de una categoría
     */
    CategoriaBasicaDTO actualizarOrden(Long id, Integer nuevoOrden);
}
