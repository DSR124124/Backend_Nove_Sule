package com.nove.sule.backend_nove_sule.mapper;

import com.nove.sule.backend_nove_sule.dto.catalogo.CategoriaBasicaDTO;
import com.nove.sule.backend_nove_sule.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para Categoria
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoriaMapper {

    CategoriaBasicaDTO toDTO(Categoria categoria);

    @Mapping(target = "productos", ignore = true)
    Categoria toEntity(CategoriaBasicaDTO categoriaDTO);

    @Mapping(target = "productos", ignore = true)
    void updateEntityFromRequest(CategoriaBasicaDTO categoriaDTO, @MappingTarget Categoria categoria);
}
