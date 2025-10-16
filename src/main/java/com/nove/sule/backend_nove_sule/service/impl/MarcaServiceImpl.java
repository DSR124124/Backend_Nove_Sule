package com.nove.sule.backend_nove_sule.service.impl;

import com.nove.sule.backend_nove_sule.dto.catalogo.MarcaBasicaDTO;
import com.nove.sule.backend_nove_sule.dto.catalogo.MarcaRequestDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Marca;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.mapper.MarcaMapper;
import com.nove.sule.backend_nove_sule.repository.MarcaRepository;
import com.nove.sule.backend_nove_sule.service.MarcaService;
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
 * Implementación del servicio de Marca
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarcaServiceImpl implements MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    @Override
    @Transactional
    public MarcaBasicaDTO crear(MarcaRequestDTO marcaRequest) {

        // Validar nombre único
        if (existeNombre(marcaRequest.getNombre())) {
            throw new RuntimeException("Ya existe una marca con ese nombre");
        }

        Marca marca = marcaMapper.toEntity(marcaRequest);
        marca = marcaRepository.save(marca);
        
        return marcaMapper.toDTO(marca);
    }

    @Override
    @Transactional
    public MarcaBasicaDTO actualizar(Long id, MarcaRequestDTO marcaRequest) {

        Marca marca = marcaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + id));

        // Validar nombre único si cambió
        if (!marca.getNombre().equals(marcaRequest.getNombre()) && 
            existeNombre(marcaRequest.getNombre())) {
            throw new RuntimeException("Ya existe una marca con ese nombre");
        }

        marcaMapper.updateEntity(marca, marcaRequest);
        marca = marcaRepository.save(marca);
        
        return marcaMapper.toDTO(marca);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarcaBasicaDTO> buscarPorId(Long id) {
        return marcaRepository.findById(id)
            .map(marcaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Marca> buscarPorNombre(String nombre) {
        return marcaRepository.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<MarcaBasicaDTO> listarTodos(Pageable pageable) {
        Page<Marca> pageMarcas = marcaRepository.findAll(pageable);
        List<MarcaBasicaDTO> marcas = pageMarcas.getContent().stream()
            .map(marcaMapper::toDTO)
            .collect(Collectors.toList());

        return PaginatedResponseDTO.<MarcaBasicaDTO>builder()
            .content(marcas)
            .page(pageMarcas.getNumber())
            .size(pageMarcas.getSize())
            .totalElements(pageMarcas.getTotalElements())
            .totalPages(pageMarcas.getTotalPages())
            .first(pageMarcas.isFirst())
            .last(pageMarcas.isLast())
            .empty(pageMarcas.isEmpty())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaBasicaDTO> listarPorEstado(Estado estado) {
        return marcaRepository.findByEstadoOrderByNombreAsc(estado).stream()
            .map(marcaMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaBasicaDTO> listarActivas() {
        return marcaRepository.findAllActiveOrderByNombre().stream()
            .map(marcaMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<MarcaBasicaDTO> buscarConFiltros(String nombre, Estado estado, Pageable pageable) {
        String estadoString = estado != null ? estado.name() : null;
        Page<Marca> pageMarcas = marcaRepository.findByFilters(nombre, estadoString, pageable);
        List<MarcaBasicaDTO> marcas = pageMarcas.getContent().stream()
            .map(marcaMapper::toDTO)
            .collect(Collectors.toList());

        return PaginatedResponseDTO.<MarcaBasicaDTO>builder()
            .content(marcas)
            .page(pageMarcas.getNumber())
            .size(pageMarcas.getSize())
            .totalElements(pageMarcas.getTotalElements())
            .totalPages(pageMarcas.getTotalPages())
            .first(pageMarcas.isFirst())
            .last(pageMarcas.isLast())
            .empty(pageMarcas.isEmpty())
            .build();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Marca marca = marcaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + id));

        // Soft delete - cambiar estado a INACTIVO
        marca.setEstado(Estado.INACTIVO);
        marcaRepository.save(marca);
        
    }

    @Override
    @Transactional
    public MarcaBasicaDTO cambiarEstado(Long id, Estado estado) {

        Marca marca = marcaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Marca no encontrada con ID: " + id));

        marca.setEstado(estado);
        marca = marcaRepository.save(marca);
        
        return marcaMapper.toDTO(marca);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeNombre(String nombre) {
        return marcaRepository.existsByNombre(nombre);
    }
}
