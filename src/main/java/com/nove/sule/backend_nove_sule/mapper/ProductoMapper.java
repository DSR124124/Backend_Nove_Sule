package com.nove.sule.backend_nove_sule.mapper;

import com.nove.sule.backend_nove_sule.dto.catalogo.*;
import com.nove.sule.backend_nove_sule.dto.catalogo.ProductoRequestDTO;
import com.nove.sule.backend_nove_sule.entity.Categoria;
import com.nove.sule.backend_nove_sule.entity.Marca;
import com.nove.sule.backend_nove_sule.entity.Producto;
import com.nove.sule.backend_nove_sule.entity.Proveedor;
import org.mapstruct.*;

/**
 * Mapper para Producto
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductoMapper {

    @Mapping(target = "categoria", source = "categoria")
    @Mapping(target = "marca", source = "marca")
    @Mapping(target = "proveedor", source = "proveedor")
    @Mapping(target = "precioConIgv", expression = "java(producto.getPrecioConIgv())")
    @Mapping(target = "margenGanancia", expression = "java(producto.getMargenGanancia())")
    @Mapping(target = "stockDisponible", expression = "java(producto.getStockDisponible())")
    @Mapping(target = "stockBajo", expression = "java(producto.isStockBajo())")
    ProductoDTO toDTO(Producto producto);

    @Mapping(target = "categoria", ignore = true)
    @Mapping(target = "marca", ignore = true)
    @Mapping(target = "proveedor", ignore = true)
    Producto toEntity(ProductoRequestDTO productoRequest);

    void updateEntityFromRequest(ProductoRequestDTO productoRequest, @MappingTarget Producto producto);

    CategoriaBasicaDTO toCategoriaBasicaDTO(Categoria categoria);

    MarcaBasicaDTO toMarcaBasicaDTO(Marca marca);

    ProveedorBasicoDTO toProveedorBasicoDTO(Proveedor proveedor);

    ProductoBasicoDTO toProductoBasicoDTO(Producto producto);
}
