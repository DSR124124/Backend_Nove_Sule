package com.nove.sule.backend_nove_sule.mapper;

import com.nove.sule.backend_nove_sule.dto.catalogo.MarcaBasicaDTO;
import com.nove.sule.backend_nove_sule.dto.catalogo.MarcaRequestDTO;
import com.nove.sule.backend_nove_sule.entity.Marca;
import org.springframework.stereotype.Component;

/**
 * Mapper para Marca
 */
@Component
public class MarcaMapper {

    public MarcaBasicaDTO toDTO(Marca marca) {
        if (marca == null) {
            return null;
        }

        return MarcaBasicaDTO.builder()
                .id(marca.getId())
                .nombre(marca.getNombre())
                .descripcion(marca.getDescripcion())
                .logo(marca.getLogo())
                .build();
    }

    public Marca toEntity(MarcaRequestDTO marcaRequest) {
        if (marcaRequest == null) {
            return null;
        }

        return Marca.builder()
                .nombre(marcaRequest.getNombre())
                .descripcion(marcaRequest.getDescripcion())
                .logo(marcaRequest.getLogo())
                .sitioWeb(marcaRequest.getSitioWeb())
                .contacto(marcaRequest.getContacto())
                .email(marcaRequest.getEmail())
                .telefono(marcaRequest.getTelefono())
                .build();
    }

    public void updateEntity(Marca marca, MarcaRequestDTO marcaRequest) {
        if (marca == null || marcaRequest == null) {
            return;
        }

        marca.setNombre(marcaRequest.getNombre());
        marca.setDescripcion(marcaRequest.getDescripcion());
        marca.setLogo(marcaRequest.getLogo());
        marca.setSitioWeb(marcaRequest.getSitioWeb());
        marca.setContacto(marcaRequest.getContacto());
        marca.setEmail(marcaRequest.getEmail());
        marca.setTelefono(marcaRequest.getTelefono());
    }
}
