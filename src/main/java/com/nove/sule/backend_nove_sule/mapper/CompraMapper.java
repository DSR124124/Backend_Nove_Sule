package com.nove.sule.backend_nove_sule.mapper;

import com.nove.sule.backend_nove_sule.dto.compras.*;
import com.nove.sule.backend_nove_sule.entity.*;
import org.mapstruct.*;


/**
 * Mapper para conversiones de Compras
 */
@Mapper(componentModel = "spring", uses = {ProductoMapper.class})
public interface CompraMapper {

    // ===== ORDEN COMPRA =====

    @Mapping(target = "proveedor", source = "proveedor")
    @Mapping(target = "detalles", source = "detalles")
    @Mapping(target = "usuario", expression = "java(obtenerNombreCompleto(ordenCompra.getUsuario()))")
    OrdenCompraDTO toDTO(OrdenCompra ordenCompra);

    @Mapping(target = "proveedorNombre", source = "proveedor.nombre")
    @Mapping(target = "proveedorRuc", source = "proveedor.ruc")
    @Mapping(target = "usuario", expression = "java(obtenerNombreCompleto(ordenCompra.getUsuario()))")
    OrdenCompraResponseDTO toResponseDTO(OrdenCompra ordenCompra);

    // ===== DETALLE ORDEN COMPRA =====

    @Mapping(target = "producto", source = "producto")
    DetalleOrdenCompraDTO toDetalleDTO(DetalleOrdenCompra detalle);

    // ===== MÉTODOS AUXILIARES =====

    default String obtenerNombreCompleto(Usuario usuario) {
        if (usuario == null) return null;
        
        if (usuario.getEmpleado() != null) {
            return usuario.getEmpleado().getNombreCompleto();
        }
        
        return usuario.getUsername();
    }

    default String obtenerNombreCompleto(Proveedor proveedor) {
        if (proveedor == null) return null;
        return proveedor.getNombre();
    }

    default String obtenerRuc(Proveedor proveedor) {
        if (proveedor == null) return null;
        return proveedor.getRuc();
    }
}
