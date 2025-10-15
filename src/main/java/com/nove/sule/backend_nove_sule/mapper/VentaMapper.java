package com.nove.sule.backend_nove_sule.mapper;

import com.nove.sule.backend_nove_sule.dto.ventas.*;
import com.nove.sule.backend_nove_sule.entity.Cliente;
import com.nove.sule.backend_nove_sule.entity.ComprobanteVenta;
import com.nove.sule.backend_nove_sule.entity.DetalleComprobante;
import com.nove.sule.backend_nove_sule.entity.Producto;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper para conversiones de Ventas
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VentaMapper {

    // ===== COMPROBANTE VENTA =====

    @Mapping(target = "cliente", source = "cliente")
    @Mapping(target = "detalles", source = "detalles")
    @Mapping(target = "numeroCompleto", expression = "java(comprobante.getSerie() + \"-\" + comprobante.getNumero())")
    @Mapping(target = "usuario", expression = "java(obtenerNombreCompletoUsuario(comprobante.getUsuario()))")
    @Mapping(target = "caja", source = "caja.nombre")
    ComprobanteVentaDTO toDTO(ComprobanteVenta comprobante);

    @Mapping(target = "estado", constant = "ACTIVO")
    @Mapping(target = "cliente", source = "clienteId", qualifiedByName = "mapClienteFromId")
    @Mapping(target = "detalles", ignore = true)
    @Mapping(target = "subtotal", ignore = true)
    @Mapping(target = "igv", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "caja", ignore = true)
    @Mapping(target = "serie", ignore = true)
    @Mapping(target = "numero", ignore = true)
    ComprobanteVenta toEntity(ComprobanteVentaRequestDTO request);

    @Mapping(target = "clienteNombre", expression = "java(obtenerNombreCompleto(comprobante.getCliente()))")
    @Mapping(target = "clienteDocumento", source = "cliente.numeroDocumento")
    @Mapping(target = "numeroCompleto", expression = "java(comprobante.getSerie() + \"-\" + comprobante.getNumero())")
    @Mapping(target = "usuario", expression = "java(obtenerNombreCompletoUsuario(comprobante.getUsuario()))")
    @Mapping(target = "caja", source = "caja.nombre")
    ComprobanteVentaResponseDTO toResponseDTO(ComprobanteVenta comprobante);

    // ===== DETALLE COMPROBANTE =====

    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "productoCodigo", source = "producto.codigo")
    @Mapping(target = "productoNombre", source = "producto.nombre")
    @Mapping(target = "subtotal", expression = "java(calcularSubtotal(detalle))")
    ComprobanteVentaDTO.DetalleComprobanteDTO toDetalleDTO(DetalleComprobante detalle);

    @Mapping(target = "comprobante", ignore = true)
    @Mapping(target = "producto", source = "productoId", qualifiedByName = "mapProductoFromId")
    @Mapping(target = "subtotal", ignore = true)
    DetalleComprobante toDetalleEntity(ComprobanteVentaRequestDTO.DetalleComprobanteRequestDTO request);

    // ===== CLIENTE BÁSICO =====

    @Mapping(target = "nombreCompleto", expression = "java(obtenerNombreCompleto(cliente))")
    ClienteBasicoDTO toClienteBasicoDTO(Cliente cliente);

    // ===== MÉTODOS AUXILIARES =====

    @Named("mapClienteFromId")
    default Cliente mapClienteFromId(Long clienteId) {
        if (clienteId == null) return null;
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        return cliente;
    }

    @Named("mapProductoFromId")
    default Producto mapProductoFromId(Long productoId) {
        if (productoId == null) return null;
        Producto producto = new Producto();
        producto.setId(productoId);
        return producto;
    }

    @Named("obtenerNombreCompleto")
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

    default String obtenerNombreCompletoUsuario(com.nove.sule.backend_nove_sule.entity.Usuario usuario) {
        if (usuario == null || usuario.getEmpleado() == null) return null;
        return usuario.getEmpleado().getNombreCompleto();
    }

    default java.math.BigDecimal calcularSubtotal(DetalleComprobante detalle) {
        if (detalle == null || detalle.getCantidad() == null || detalle.getPrecioUnitario() == null) {
            return java.math.BigDecimal.ZERO;
        }
        
        java.math.BigDecimal subtotal = detalle.getCantidad().multiply(detalle.getPrecioUnitario());
        
        if (detalle.getDescuento() != null) {
            subtotal = subtotal.subtract(detalle.getDescuento());
        }
        
        return subtotal.max(java.math.BigDecimal.ZERO);
    }

    // ===== LISTAS =====

    List<ComprobanteVentaDTO> toDTOList(List<ComprobanteVenta> comprobantes);
    List<ComprobanteVentaResponseDTO> toResponseDTOList(List<ComprobanteVenta> comprobantes);
    List<ComprobanteVentaDTO.DetalleComprobanteDTO> toDetalleDTOList(List<DetalleComprobante> detalles);
}