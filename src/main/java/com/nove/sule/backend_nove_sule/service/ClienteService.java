package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.cliente.ClienteDTO;
import com.nove.sule.backend_nove_sule.dto.cliente.ClienteRequestDTO;
import com.nove.sule.backend_nove_sule.dto.cliente.ClienteResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Cliente;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoDocumento;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de Cliente
 */
public interface ClienteService {

    /**
     * Crea un nuevo cliente
     */
    ClienteDTO crear(ClienteRequestDTO clienteRequest);

    /**
     * Actualiza un cliente existente
     */
    ClienteDTO actualizar(Long id, ClienteRequestDTO clienteRequest);

    /**
     * Busca un cliente por ID
     */
    Optional<ClienteDTO> buscarPorId(Long id);

    /**
     * Busca un cliente por tipo y número de documento
     */
    Optional<Cliente> buscarPorTipoYNumeroDocumento(TipoDocumento tipoDocumento, String numeroDocumento);

    /**
     * Busca un cliente por email
     */
    Optional<Cliente> buscarPorEmail(String email);

    /**
     * Lista todos los clientes con paginación
     */
    PaginatedResponseDTO<ClienteDTO> listarTodos(Pageable pageable);

    /**
     * Lista clientes por estado
     */
    List<ClienteDTO> listarPorEstado(Estado estado);

    /**
     * Lista clientes por tipo de documento
     */
    List<ClienteDTO> listarPorTipoDocumento(TipoDocumento tipoDocumento);

    /**
     * Busca clientes con filtros
     */
    PaginatedResponseDTO<ClienteDTO> buscarConFiltros(String nombres, String apellidos, 
                                                     String razonSocial, String email,
                                                     TipoDocumento tipoDocumento, Estado estado, 
                                                     Pageable pageable);

    /**
     * Elimina un cliente (soft delete)
     */
    void eliminar(Long id);

    /**
     * Cambia el estado de un cliente
     */
    ClienteDTO cambiarEstado(Long id, Estado estado);

    /**
     * Verifica si existe un cliente con el mismo tipo y número de documento
     */
    boolean existePorTipoYNumeroDocumento(TipoDocumento tipoDocumento, String numeroDocumento);

    /**
     * Verifica si existe un cliente con el mismo email
     */
    boolean existePorEmail(String email);

    /**
     * Busca clientes por nombre (nombres o razón social)
     */
    List<ClienteResponseDTO> buscarPorNombre(String nombre);

    /**
     * Lista clientes activos para selección
     */
    List<ClienteResponseDTO> listarActivos();
}
