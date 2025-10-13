package com.nove.sule.backend_nove_sule.mapper;

import com.nove.sule.backend_nove_sule.dto.usuario.EmpleadoBasicoDTO;
import com.nove.sule.backend_nove_sule.dto.usuario.UsuarioDTO;
import com.nove.sule.backend_nove_sule.entity.Empleado;
import com.nove.sule.backend_nove_sule.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para Usuario
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {

    @Mapping(target = "empleado", source = "empleado")
    UsuarioDTO toDTO(Usuario usuario);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "empleado", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);

    @Mapping(target = "nombreCompleto", expression = "java(empleado.getNombreCompleto())")
    EmpleadoBasicoDTO toEmpleadoBasicoDTO(Empleado empleado);
}
