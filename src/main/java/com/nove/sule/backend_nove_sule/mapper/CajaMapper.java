package com.nove.sule.backend_nove_sule.mapper;

import com.nove.sule.backend_nove_sule.dto.caja.*;
import com.nove.sule.backend_nove_sule.entity.Caja;
import com.nove.sule.backend_nove_sule.entity.Empleado;
import org.mapstruct.*;

/**
 * Mapper para conversiones de Caja
 */
@Mapper(componentModel = "spring")
public interface CajaMapper {

    // ===== CAJA =====

    @Mapping(target = "responsable", source = "responsable")
    CajaDTO toDTO(Caja caja);

    @Mapping(target = "responsableNombre", source = "responsable.nombreCompleto")
    CajaResponseDTO toResponseDTO(Caja caja);

    // ===== MÉTODOS AUXILIARES =====

    default String obtenerNombreCompleto(Empleado empleado) {
        if (empleado == null) return null;
        return empleado.getNombreCompleto();
    }
}
