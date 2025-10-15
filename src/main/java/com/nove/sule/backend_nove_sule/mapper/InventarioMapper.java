package com.nove.sule.backend_nove_sule.mapper;

import com.nove.sule.backend_nove_sule.dto.inventario.*;
import com.nove.sule.backend_nove_sule.entity.*;
import org.mapstruct.*;

/**
 * Mapper para conversiones de Inventario
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventarioMapper {

    // ===== MOVIMIENTO INVENTARIO =====

    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "productoCodigo", source = "producto.codigo")
    @Mapping(target = "productoNombre", source = "producto.nombre")
    @Mapping(target = "usuarioNombre", expression = "java(obtenerNombreCompletoUsuario(movimiento.getUsuario()))")
    @Mapping(target = "ordenCompraId", source = "ordenCompra.id")
    @Mapping(target = "ordenCompraNumero", source = "ordenCompra.numero")
    @Mapping(target = "comprobanteVentaId", source = "comprobanteVenta.id")
    @Mapping(target = "comprobanteVentaNumero", expression = "java(obtenerNumeroComprobante(movimiento.getComprobanteVenta()))")
    MovimientoInventarioDTO toDTO(MovimientoInventario movimiento);

    // ===== RESÚMENES =====

    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "productoNombre", source = "producto.nombre")
    @Mapping(target = "productoCodigo", source = "producto.codigo")
    @Mapping(target = "stockActual", source = "stock")
    @Mapping(target = "valorInventario", expression = "java(calcularValorInventario(producto))")
    @Mapping(target = "estadoStock", expression = "java(determinarEstadoStock(producto))")
    @Mapping(target = "fechaUltimoMovimiento", ignore = true)
    @Mapping(target = "movimientosMes", ignore = true)
    @Mapping(target = "precioPromedio", ignore = true)
    @Mapping(target = "totalEntradas", ignore = true)
    @Mapping(target = "totalSalidas", ignore = true)
    @Mapping(target = "ultimoMovimiento", ignore = true)
    ResumenInventarioDTO toResumenDTO(Producto producto);

    @Mapping(target = "productoId", source = "producto.id")
    @Mapping(target = "productoNombre", source = "producto.nombre")
    @Mapping(target = "productoCodigo", source = "producto.codigo")
    @Mapping(target = "stockActual", source = "stock")
    @Mapping(target = "diferencia", expression = "java(producto.getStockMinimo() - producto.getStock())")
    @Mapping(target = "valorReposicion", expression = "java(calcularValorReposicion(producto))")
    @Mapping(target = "proveedorNombre", source = "producto.proveedor.nombre")
    @Mapping(target = "categoriaNombre", source = "producto.categoria.nombre")
    StockBajoDTO toStockBajoDTO(Producto producto);

    // ===== MÉTODOS AUXILIARES =====

    default String obtenerNombreCompleto(Empleado empleado) {
        if (empleado == null) return null;
        return empleado.getNombreCompleto();
    }

    default String obtenerNombreCompletoUsuario(com.nove.sule.backend_nove_sule.entity.Usuario usuario) {
        if (usuario == null || usuario.getEmpleado() == null) return null;
        return usuario.getEmpleado().getNombreCompleto();
    }

    default String obtenerNumeroComprobante(com.nove.sule.backend_nove_sule.entity.ComprobanteVenta comprobante) {
        if (comprobante == null) return null;
        return comprobante.getSerie() + "-" + comprobante.getNumero();
    }

    default java.math.BigDecimal calcularValorInventario(Producto producto) {
        if (producto == null || producto.getPrecioCompra() == null) {
            return java.math.BigDecimal.ZERO;
        }
        return producto.getPrecioCompra().multiply(new java.math.BigDecimal(producto.getStock()));
    }

    default String determinarEstadoStock(Producto producto) {
        if (producto == null) return "NORMAL";
        
        if (producto.getStock() <= producto.getStockMinimo()) {
            return "BAJO";
        } else if (producto.getStockMaximo() != null && producto.getStock() >= producto.getStockMaximo()) {
            return "SOBRE_STOCK";
        }
        return "NORMAL";
    }

    default java.math.BigDecimal calcularValorReposicion(Producto producto) {
        if (producto == null || producto.getPrecioCompra() == null) {
            return java.math.BigDecimal.ZERO;
        }
        
        int cantidadNecesaria = producto.getStockMinimo() - producto.getStock();
        if (cantidadNecesaria <= 0) {
            return java.math.BigDecimal.ZERO;
        }
        
        return producto.getPrecioCompra().multiply(new java.math.BigDecimal(cantidadNecesaria));
    }
}
