package com.nove.sule.backend_nove_sule.service.impl;

import com.nove.sule.backend_nove_sule.dto.cliente.ClienteDTO;
import com.nove.sule.backend_nove_sule.dto.cliente.ClienteRequestDTO;
import com.nove.sule.backend_nove_sule.dto.cliente.ClienteResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Cliente;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoDocumento;
import com.nove.sule.backend_nove_sule.mapper.ClienteMapper;
import com.nove.sule.backend_nove_sule.repository.ClienteRepository;
import com.nove.sule.backend_nove_sule.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Cliente
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    @Transactional
    public ClienteDTO crear(ClienteRequestDTO clienteRequest) {
        log.info("Creando cliente: {} {}", clienteRequest.getTipoDocumento(), clienteRequest.getNumeroDocumento());

        // Validar que no exista el cliente con el mismo tipo y número de documento
        if (existePorTipoYNumeroDocumento(clienteRequest.getTipoDocumento(), clienteRequest.getNumeroDocumento())) {
            throw new RuntimeException("Ya existe un cliente con ese tipo y número de documento");
        }

        // Validar email único si se proporciona
        if (clienteRequest.getEmail() != null && !clienteRequest.getEmail().isEmpty() && 
            existePorEmail(clienteRequest.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con ese email");
        }

        Cliente cliente = clienteMapper.toEntity(clienteRequest);
        Cliente nuevoCliente = clienteRepository.save(cliente);
        
        log.info("Cliente creado con ID: {}", nuevoCliente.getId());
        return clienteMapper.toDTO(nuevoCliente);
    }

    @Override
    @Transactional
    public ClienteDTO actualizar(Long id, ClienteRequestDTO clienteRequest) {
        log.info("Actualizando cliente ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        // Validar tipo y número de documento únicos si cambiaron
        if (!cliente.getTipoDocumento().equals(clienteRequest.getTipoDocumento()) || 
            !cliente.getNumeroDocumento().equals(clienteRequest.getNumeroDocumento())) {
            if (existePorTipoYNumeroDocumento(clienteRequest.getTipoDocumento(), clienteRequest.getNumeroDocumento())) {
                throw new RuntimeException("Ya existe un cliente con ese tipo y número de documento");
            }
        }

        // Validar email único si cambió
        if (clienteRequest.getEmail() != null && !clienteRequest.getEmail().isEmpty() && 
            !clienteRequest.getEmail().equals(cliente.getEmail()) && 
            existePorEmail(clienteRequest.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con ese email");
        }

        // Actualizar campos
        cliente.setTipoDocumento(clienteRequest.getTipoDocumento());
        cliente.setNumeroDocumento(clienteRequest.getNumeroDocumento());
        cliente.setNombres(clienteRequest.getNombres());
        cliente.setApellidos(clienteRequest.getApellidos());
        cliente.setRazonSocial(clienteRequest.getRazonSocial());
        cliente.setEmail(clienteRequest.getEmail());
        cliente.setTelefono(clienteRequest.getTelefono());
        cliente.setDireccion(clienteRequest.getDireccion());
        cliente.setFechaNacimiento(clienteRequest.getFechaNacimiento());
        cliente.setLimiteCredito(clienteRequest.getLimiteCredito());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        log.info("Cliente actualizado: {}", clienteActualizado.getId());

        return clienteMapper.toDTO(clienteActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> buscarPorId(Long id) {
        log.debug("Buscando cliente por ID: {}", id);
        
        return clienteRepository.findById(id)
            .map(clienteMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorTipoYNumeroDocumento(TipoDocumento tipoDocumento, String numeroDocumento) {
        log.debug("Buscando cliente por tipo y número: {} {}", tipoDocumento, numeroDocumento);
        
        return clienteRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorEmail(String email) {
        log.debug("Buscando cliente por email: {}", email);
        
        return clienteRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<ClienteDTO> listarTodos(Pageable pageable) {
        log.debug("Listando todos los clientes con paginación");
        
        Page<Cliente> pageClientes = clienteRepository.findAll(pageable);
        List<ClienteDTO> clientes = pageClientes.getContent().stream()
            .map(clienteMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<ClienteDTO>builder()
            .content(clientes)
            .page(pageClientes.getNumber())
            .size(pageClientes.getSize())
            .totalElements(pageClientes.getTotalElements())
            .totalPages(pageClientes.getTotalPages())
            .first(pageClientes.isFirst())
            .last(pageClientes.isLast())
            .empty(pageClientes.isEmpty())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarPorEstado(Estado estado) {
        log.debug("Listando clientes por estado: {}", estado);
        
        return clienteRepository.findByEstadoOrderByNombresAsc(estado).stream()
            .map(clienteMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarPorTipoDocumento(TipoDocumento tipoDocumento) {
        log.debug("Listando clientes por tipo de documento: {}", tipoDocumento);
        
        return clienteRepository.findByTipoDocumentoOrderByNombresAsc(tipoDocumento).stream()
            .map(clienteMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<ClienteDTO> buscarConFiltros(String nombres, String apellidos, 
                                                             String razonSocial, String email,
                                                             TipoDocumento tipoDocumento, Estado estado, 
                                                             Pageable pageable) {
        log.debug("Buscando clientes con filtros - nombres: {}, apellidos: {}, razonSocial: {}, email: {}, tipo: {}, estado: {}", 
                 nombres, apellidos, razonSocial, email, tipoDocumento, estado);
        
        Page<Cliente> pageClientes = clienteRepository.findByFilters(
            nombres, apellidos, razonSocial, email, tipoDocumento, estado, pageable);
        
        List<ClienteDTO> clientes = pageClientes.getContent().stream()
            .map(clienteMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<ClienteDTO>builder()
            .content(clientes)
            .page(pageClientes.getNumber())
            .size(pageClientes.getSize())
            .totalElements(pageClientes.getTotalElements())
            .totalPages(pageClientes.getTotalPages())
            .first(pageClientes.isFirst())
            .last(pageClientes.isLast())
            .empty(pageClientes.isEmpty())
            .build();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando cliente ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        // Soft delete - cambiar estado a INACTIVO
        cliente.setEstado(Estado.INACTIVO);
        clienteRepository.save(cliente);
        
        log.info("Cliente eliminado (soft delete): {}", id);
    }

    @Override
    @Transactional
    public ClienteDTO cambiarEstado(Long id, Estado estado) {
        log.info("Cambiando estado de cliente ID: {} a: {}", id, estado);

        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        cliente.setEstado(estado);
        Cliente clienteActualizado = clienteRepository.save(cliente);
        
        log.info("Estado de cliente actualizado: {} -> {}", id, estado);
        
        return clienteMapper.toDTO(clienteActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorTipoYNumeroDocumento(TipoDocumento tipoDocumento, String numeroDocumento) {
        return clienteRepository.existsByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> buscarPorNombre(String nombre) {
        log.debug("Buscando clientes por nombre: {}", nombre);
        
        return clienteRepository.findByNombreContainingIgnoreCase(nombre).stream()
            .map(clienteMapper::toResponseDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarActivos() {
        log.debug("Listando clientes activos");
        
        return clienteRepository.findByEstadoOrderByNombresAsc(Estado.ACTIVO).stream()
            .map(clienteMapper::toResponseDTO)
            .toList();
    }
}
