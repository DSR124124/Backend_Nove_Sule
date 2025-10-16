package com.nove.sule.backend_nove_sule.service.impl;

import com.nove.sule.backend_nove_sule.dto.catalogo.ProveedorBasicoDTO;
import com.nove.sule.backend_nove_sule.dto.catalogo.ProveedorRequestDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Proveedor;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.mapper.ProveedorMapper;
import com.nove.sule.backend_nove_sule.repository.ProveedorRepository;
import com.nove.sule.backend_nove_sule.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Proveedor
 */
@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final ProveedorMapper proveedorMapper;

    @Override
    @Transactional
    public ProveedorBasicoDTO crear(ProveedorRequestDTO proveedorRequest) {
        // Validar RUC único
        if (existeRuc(proveedorRequest.getRuc())) {
            throw new RuntimeException("Ya existe un proveedor con ese RUC");
        }

        // Validar email único
        if (existeEmail(proveedorRequest.getEmail())) {
            throw new RuntimeException("Ya existe un proveedor con ese email");
        }

        Proveedor proveedor = proveedorMapper.toEntity(proveedorRequest);
        proveedor = proveedorRepository.save(proveedor);
        
        return proveedorMapper.toDTO(proveedor);
    }

    @Override
    @Transactional
    public ProveedorBasicoDTO actualizar(Long id, ProveedorRequestDTO proveedorRequest) {
        Proveedor proveedor = proveedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));

        // Validar RUC único si cambió
        if (!proveedor.getRuc().equals(proveedorRequest.getRuc()) && 
            existeRuc(proveedorRequest.getRuc())) {
            throw new RuntimeException("Ya existe un proveedor con ese RUC");
        }

        // Validar email único si cambió
        if (!proveedor.getEmail().equals(proveedorRequest.getEmail()) && 
            existeEmail(proveedorRequest.getEmail())) {
            throw new RuntimeException("Ya existe un proveedor con ese email");
        }

        proveedorMapper.updateEntity(proveedor, proveedorRequest);
        proveedor = proveedorRepository.save(proveedor);
        
        return proveedorMapper.toDTO(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProveedorBasicoDTO> buscarPorId(Long id) {
        return proveedorRepository.findById(id)
            .map(proveedorMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<ProveedorBasicoDTO> listarTodos(Pageable pageable) {
        Page<Proveedor> pageProveedores = proveedorRepository.findAll(pageable);
        List<ProveedorBasicoDTO> proveedores = pageProveedores.getContent().stream()
            .map(proveedorMapper::toDTO)
            .collect(Collectors.toList());

        return PaginatedResponseDTO.<ProveedorBasicoDTO>builder()
            .content(proveedores)
            .page(pageProveedores.getNumber())
            .size(pageProveedores.getSize())
            .totalElements(pageProveedores.getTotalElements())
            .totalPages(pageProveedores.getTotalPages())
            .first(pageProveedores.isFirst())
            .last(pageProveedores.isLast())
            .empty(pageProveedores.isEmpty())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorBasicoDTO> listarPorEstado(Estado estado) {
        return proveedorRepository.findByEstadoOrderByNombreAsc(estado).stream()
            .map(proveedorMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorBasicoDTO> listarActivos() {
        return proveedorRepository.findAllActiveOrderByNombre().stream()
            .map(proveedorMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<ProveedorBasicoDTO> buscarConFiltros(String nombre, String ruc, String email, Estado estado, Pageable pageable) {
        String estadoString = estado != null ? estado.name() : null;
        Page<Proveedor> pageProveedores = proveedorRepository.findByFilters(nombre, ruc, email, estadoString, pageable);
        List<ProveedorBasicoDTO> proveedores = pageProveedores.getContent().stream()
            .map(proveedorMapper::toDTO)
            .collect(Collectors.toList());

        return PaginatedResponseDTO.<ProveedorBasicoDTO>builder()
            .content(proveedores)
            .page(pageProveedores.getNumber())
            .size(pageProveedores.getSize())
            .totalElements(pageProveedores.getTotalElements())
            .totalPages(pageProveedores.getTotalPages())
            .first(pageProveedores.isFirst())
            .last(pageProveedores.isLast())
            .empty(pageProveedores.isEmpty())
            .build();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));

        // Soft delete - cambiar estado a INACTIVO
        proveedor.setEstado(Estado.INACTIVO);
        proveedorRepository.save(proveedor);
    }

    @Override
    @Transactional
    public ProveedorBasicoDTO cambiarEstado(Long id, Estado estado) {
        Proveedor proveedor = proveedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));

        proveedor.setEstado(estado);
        proveedor = proveedorRepository.save(proveedor);
        
        return proveedorMapper.toDTO(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeRuc(String ruc) {
        return proveedorRepository.existsByRuc(ruc);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeEmail(String email) {
        return proveedorRepository.existsByEmail(email);
    }
}
