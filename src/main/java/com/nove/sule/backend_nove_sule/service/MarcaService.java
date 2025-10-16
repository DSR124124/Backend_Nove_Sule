package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.catalogo.MarcaBasicaDTO;
import com.nove.sule.backend_nove_sule.dto.catalogo.MarcaRequestDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Marca;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de Marca
 */
public interface MarcaService {

    /**
     * Crea una nueva marca
     */
    MarcaBasicaDTO crear(MarcaRequestDTO marcaRequest);

    /**
     * Actualiza una marca existente
     */
    MarcaBasicaDTO actualizar(Long id, MarcaRequestDTO marcaRequest);

    /**
     * Busca una marca por ID
     */
    Optional<MarcaBasicaDTO> buscarPorId(Long id);

    /**
     * Busca una marca por nombre
     */
    Optional<Marca> buscarPorNombre(String nombre);

    /**
     * Lista todas las marcas con paginación
     */
    PaginatedResponseDTO<MarcaBasicaDTO> listarTodos(Pageable pageable);

    /**
     * Lista marcas por estado
     */
    List<MarcaBasicaDTO> listarPorEstado(Estado estado);

    /**
     * Lista marcas activas
     */
    List<MarcaBasicaDTO> listarActivas();

    /**
     * Busca marcas con filtros
     */
    PaginatedResponseDTO<MarcaBasicaDTO> buscarConFiltros(String nombre, Estado estado, Pageable pageable);

    /**
     * Elimina una marca (soft delete)
     */
    void eliminar(Long id);

    /**
     * Cambia el estado de una marca
     */
    MarcaBasicaDTO cambiarEstado(Long id, Estado estado);

    /**
     * Verifica si existe un nombre
     */
    boolean existeNombre(String nombre);
}
