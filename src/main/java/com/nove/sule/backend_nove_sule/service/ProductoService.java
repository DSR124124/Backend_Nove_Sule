package com.nove.sule.backend_nove_sule.service;

import com.nove.sule.backend_nove_sule.dto.catalogo.ProductoDTO;
import com.nove.sule.backend_nove_sule.dto.catalogo.ProductoRequestDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el servicio de Producto
 */
public interface ProductoService {

    /**
     * Crea un nuevo producto
     */
    ProductoDTO crear(ProductoRequestDTO productoRequest);

    /**
     * Actualiza un producto existente
     */
    ProductoDTO actualizar(Long id, ProductoRequestDTO productoRequest);

    /**
     * Busca un producto por ID
     */
    Optional<ProductoDTO> buscarPorId(Long id);

    /**
     * Busca un producto por código
     */
    Optional<ProductoDTO> buscarPorCodigo(String codigo);

    /**
     * Busca un producto por código de barras
     */
    Optional<ProductoDTO> buscarPorCodigoBarras(String codigoBarras);

    /**
     * Lista todos los productos con paginación
     */
    PaginatedResponseDTO<ProductoDTO> listarTodos(Pageable pageable);

    /**
     * Lista productos por estado
     */
    List<ProductoDTO> listarPorEstado(Estado estado);

    /**
     * Busca productos con filtros
     */
    PaginatedResponseDTO<ProductoDTO> buscarConFiltros(String nombre, String codigo,
                                                      Long categoriaId, Long marcaId,
                                                      Long proveedorId, Estado estado,
                                                      Pageable pageable);

    /**
     * Lista productos con stock bajo
     */
    List<ProductoDTO> listarConStockBajo();

    /**
     * Lista productos por categoría
     */
    List<ProductoDTO> listarPorCategoria(Long categoriaId);

    /**
     * Lista productos por marca
     */
    List<ProductoDTO> listarPorMarca(Long marcaId);

    /**
     * Lista productos por proveedor
     */
    List<ProductoDTO> listarPorProveedor(Long proveedorId);

    /**
     * Elimina un producto (soft delete)
     */
    void eliminar(Long id);

    /**
     * Cambia el estado de un producto
     */
    ProductoDTO cambiarEstado(Long id, Estado estado);

    /**
     * Actualiza el stock de un producto
     */
    ProductoDTO actualizarStock(Long id, Integer nuevoStock);

    /**
     * Verifica si existe un código
     */
    boolean existeCodigo(String codigo);

    /**
     * Verifica si existe un código de barras
     */
    boolean existeCodigoBarras(String codigoBarras);
}
