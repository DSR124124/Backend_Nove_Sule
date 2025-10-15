package com.nove.sule.backend_nove_sule.mapper;

import com.nove.sule.backend_nove_sule.dto.cliente.*;
import com.nove.sule.backend_nove_sule.entity.Cliente;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para conversiones de Cliente
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper {

    // ===== CLIENTE DTO =====

    ClienteDTO toDTO(Cliente cliente);

    @Mapping(target = "estado", constant = "ACTIVO")
    @Mapping(target = "departamento", ignore = true)
    @Mapping(target = "distrito", ignore = true)
    @Mapping(target = "provincia", ignore = true)
    Cliente toEntity(ClienteRequestDTO request);

    ClienteResponseDTO toResponseDTO(Cliente cliente);

    // ===== MÉTODOS AUXILIARES =====

    default String obtenerNombreCompleto(Cliente cliente) {
        if (cliente == null) return null;
        
        if (cliente.getTipoDocumento().name().equals("RUC")) {
            return cliente.getRazonSocial() != null ? cliente.getRazonSocial() : "Sin razón social";
        } else {
            StringBuilder nombre = new StringBuilder();
            if (cliente.getNombres() != null) nombre.append(cliente.getNombres());
            if (cliente.getApellidos() != null) {
                if (nombre.length() > 0) nombre.append(" ");
                nombre.append(cliente.getApellidos());
            }
            return nombre.length() > 0 ? nombre.toString() : "Sin nombre";
        }
    }

    default String obtenerNombreCompletoFromRequest(ClienteRequestDTO request) {
        if (request == null) return null;
        
        if (request.getTipoDocumento().name().equals("RUC")) {
            return request.getRazonSocial() != null ? request.getRazonSocial() : "Sin razón social";
        } else {
            StringBuilder nombre = new StringBuilder();
            if (request.getNombres() != null) nombre.append(request.getNombres());
            if (request.getApellidos() != null) {
                if (nombre.length() > 0) nombre.append(" ");
                nombre.append(request.getApellidos());
            }
            return nombre.length() > 0 ? nombre.toString() : "Sin nombre";
        }
    }

    // ===== LISTAS =====

    List<ClienteDTO> toDTOList(List<Cliente> clientes);
    List<ClienteResponseDTO> toResponseDTOList(List<Cliente> clientes);
}
