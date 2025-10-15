package com.nove.sule.backend_nove_sule.service.impl;

import com.nove.sule.backend_nove_sule.dto.catalogo.CategoriaBasicaDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Categoria;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.mapper.CategoriaMapper;
import com.nove.sule.backend_nove_sule.repository.CategoriaRepository;
import com.nove.sule.backend_nove_sule.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Categoria
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    @Transactional
    public CategoriaBasicaDTO crear(CategoriaBasicaDTO categoriaDTO) {
        log.info("Creando categoría: {}", categoriaDTO.getNombre());

        // Validar nombre único
        if (existeNombre(categoriaDTO.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }

        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        
        // Si no se especifica orden, asignar el siguiente disponible
        if (categoria.getOrden() == null) {
            Integer maxOrden = categoriaRepository.findMaxOrden();
            categoria.setOrden(maxOrden != null ? maxOrden + 1 : 1);
        }
        
        // Por defecto, estado activo
        if (categoria.getEstado() == null) {
            categoria.setEstado(Estado.ACTIVO);
        }

        Categoria nuevaCategoria = categoriaRepository.save(categoria);
        log.info("Categoría creada con ID: {}", nuevaCategoria.getId());
        
        return categoriaMapper.toDTO(nuevaCategoria);
    }

    @Override
    @Transactional
    public CategoriaBasicaDTO actualizar(Long id, CategoriaBasicaDTO categoriaDTO) {
        log.info("Actualizando categoría ID: {}", id);

        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        // Validar nombre único si cambió
        if (!categoria.getNombre().equals(categoriaDTO.getNombre()) && 
            existeNombre(categoriaDTO.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }

        // Actualizar campos
        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        categoria.setColor(categoriaDTO.getColor());
        categoria.setImagen(categoriaDTO.getImagen());
        
        if (categoriaDTO.getOrden() != null) {
            categoria.setOrden(categoriaDTO.getOrden());
        }
        
        if (categoriaDTO.getEstado() != null) {
            categoria.setEstado(categoriaDTO.getEstado());
        }

        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        log.info("Categoría actualizada: {}", categoriaActualizada.getId());

        return categoriaMapper.toDTO(categoriaActualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaBasicaDTO> buscarPorId(Long id) {
        log.debug("Buscando categoría por ID: {}", id);
        
        return categoriaRepository.findById(id)
            .map(categoriaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorNombre(String nombre) {
        log.debug("Buscando categoría por nombre: {}", nombre);
        
        return categoriaRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<CategoriaBasicaDTO> listarTodos(Pageable pageable) {
        log.debug("Listando todas las categorías con paginación");
        
        Page<Categoria> pageCategorias = categoriaRepository.findAll(pageable);
        List<CategoriaBasicaDTO> categorias = pageCategorias.getContent().stream()
            .map(categoriaMapper::toDTO)
            .collect(Collectors.toList());

        return PaginatedResponseDTO.<CategoriaBasicaDTO>builder()
            .content(categorias)
            .page(pageCategorias.getNumber())
            .size(pageCategorias.getSize())
            .totalElements(pageCategorias.getTotalElements())
            .totalPages(pageCategorias.getTotalPages())
            .first(pageCategorias.isFirst())
            .last(pageCategorias.isLast())
            .empty(pageCategorias.isEmpty())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaBasicaDTO> listarPorEstado(Estado estado) {
        log.debug("Listando categorías por estado: {}", estado);
        
        return categoriaRepository.findByEstadoOrderByOrdenAsc(estado).stream()
            .map(categoriaMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaBasicaDTO> listarActivas() {
        log.debug("Listando categorías activas");
        
        return categoriaRepository.findActiveCategoriesOrdered(Estado.ACTIVO).stream()
            .map(categoriaMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<CategoriaBasicaDTO> buscarConFiltros(String nombre, Estado estado, Pageable pageable) {
        log.debug("Buscando categorías con filtros - nombre: {}, estado: {}", nombre, estado);
        
        Page<Categoria> pageCategorias = categoriaRepository.findByFilters(nombre, estado, pageable);
        List<CategoriaBasicaDTO> categorias = pageCategorias.getContent().stream()
            .map(categoriaMapper::toDTO)
            .collect(Collectors.toList());

        return PaginatedResponseDTO.<CategoriaBasicaDTO>builder()
            .content(categorias)
            .page(pageCategorias.getNumber())
            .size(pageCategorias.getSize())
            .totalElements(pageCategorias.getTotalElements())
            .totalPages(pageCategorias.getTotalPages())
            .first(pageCategorias.isFirst())
            .last(pageCategorias.isLast())
            .empty(pageCategorias.isEmpty())
            .build();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando categoría ID: {}", id);

        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        // Soft delete - cambiar estado a INACTIVO
        categoria.setEstado(Estado.INACTIVO);
        categoriaRepository.save(categoria);
        
        log.info("Categoría eliminada (soft delete): {}", id);
    }

    @Override
    @Transactional
    public CategoriaBasicaDTO cambiarEstado(Long id, Estado estado) {
        log.info("Cambiando estado de categoría ID: {} a: {}", id, estado);

        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        categoria.setEstado(estado);
        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        
        log.info("Estado de categoría actualizado: {} -> {}", id, estado);
        
        return categoriaMapper.toDTO(categoriaActualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

    @Override
    @Transactional
    public CategoriaBasicaDTO actualizarOrden(Long id, Integer nuevoOrden) {
        log.info("Actualizando orden de categoría ID: {} a: {}", id, nuevoOrden);

        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        categoria.setOrden(nuevoOrden);
        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        
        log.info("Orden de categoría actualizado: {} -> {}", id, nuevoOrden);
        
        return categoriaMapper.toDTO(categoriaActualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaBasicaDTO> buscarPorTexto(String texto) {
        return categoriaRepository.findByTextoContaining(texto)
            .stream()
            .map(categoriaMapper::toDTO)
            .toList();
    }
}
