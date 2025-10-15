package com.nove.sule.backend_nove_sule.service.impl;

import com.nove.sule.backend_nove_sule.dto.catalogo.ProductoDTO;
import com.nove.sule.backend_nove_sule.dto.catalogo.ProductoRequestDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.Categoria;
import com.nove.sule.backend_nove_sule.entity.Marca;
import com.nove.sule.backend_nove_sule.entity.Producto;
import com.nove.sule.backend_nove_sule.entity.Proveedor;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.mapper.ProductoMapper;
import com.nove.sule.backend_nove_sule.repository.CategoriaRepository;
import com.nove.sule.backend_nove_sule.repository.MarcaRepository;
import com.nove.sule.backend_nove_sule.repository.ProductoRepository;
import com.nove.sule.backend_nove_sule.repository.ProveedorRepository;
import com.nove.sule.backend_nove_sule.service.ProductoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Producto
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional
    public ProductoDTO crear(ProductoRequestDTO productoRequest) {
        log.info("Creando producto: {}", productoRequest.getNombre());

        // Validar código único
        if (existeCodigo(productoRequest.getCodigo())) {
            throw new RuntimeException("Ya existe un producto con ese código");
        }

        // Validar código de barras único si se proporciona
        if (productoRequest.getCodigoBarras() != null && 
            existeCodigoBarras(productoRequest.getCodigoBarras())) {
            throw new RuntimeException("Ya existe un producto con ese código de barras");
        }

        Producto producto = productoMapper.toEntity(productoRequest);

        // Establecer relaciones
        Categoria categoria = categoriaRepository.findById(productoRequest.getCategoriaId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setCategoria(categoria);

        if (productoRequest.getMarcaId() != null) {
            Marca marca = marcaRepository.findById(productoRequest.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
            producto.setMarca(marca);
        }

        if (productoRequest.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(productoRequest.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            producto.setProveedor(proveedor);
        }

        producto = productoRepository.save(producto);
        
        log.info("Producto creado exitosamente con ID: {}", producto.getId());
        return productoMapper.toDTO(producto);
    }

    @Override
    @Transactional
    public ProductoDTO actualizar(Long id, ProductoRequestDTO productoRequest) {
        log.info("Actualizando producto con ID: {}", id);

        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Validar código único (si cambió)
        if (!producto.getCodigo().equals(productoRequest.getCodigo()) && 
            existeCodigo(productoRequest.getCodigo())) {
            throw new RuntimeException("Ya existe un producto con ese código");
        }

        // Validar código de barras único (si cambió y se proporciona)
        if (productoRequest.getCodigoBarras() != null &&
            !productoRequest.getCodigoBarras().equals(producto.getCodigoBarras()) &&
            existeCodigoBarras(productoRequest.getCodigoBarras())) {
            throw new RuntimeException("Ya existe un producto con ese código de barras");
        }

        // Actualizar campos
        productoMapper.updateEntityFromRequest(productoRequest, producto);

        // Actualizar relaciones
        Categoria categoria = categoriaRepository.findById(productoRequest.getCategoriaId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setCategoria(categoria);

        if (productoRequest.getMarcaId() != null) {
            Marca marca = marcaRepository.findById(productoRequest.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
            producto.setMarca(marca);
        } else {
            producto.setMarca(null);
        }

        if (productoRequest.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(productoRequest.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            producto.setProveedor(proveedor);
        } else {
            producto.setProveedor(null);
        }

        producto = productoRepository.save(producto);
        
        log.info("Producto actualizado exitosamente");
        return productoMapper.toDTO(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoDTO> buscarPorId(Long id) {
        return productoRepository.findByIdWithRelations(id)
            .map(productoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoDTO> buscarPorCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo)
            .map(productoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoDTO> buscarPorCodigoBarras(String codigoBarras) {
        return productoRepository.findByCodigoBarras(codigoBarras)
            .map(productoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<ProductoDTO> listarTodos(Pageable pageable) {
        Page<Producto> productos = productoRepository.findAll(pageable);
        
        List<ProductoDTO> content = productos.getContent()
            .stream()
            .map(productoMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<ProductoDTO>builder()
            .content(content)
            .page(productos.getNumber())
            .size(productos.getSize())
            .totalElements(productos.getTotalElements())
            .totalPages(productos.getTotalPages())
            .first(productos.isFirst())
            .last(productos.isLast())
            .empty(productos.isEmpty())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarPorEstado(Estado estado) {
        return productoRepository.findByEstadoOrderByNombreAsc(estado)
            .stream()
            .map(productoMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<ProductoDTO> buscarConFiltros(String nombre, String codigo,
                                                            Long categoriaId, Long marcaId,
                                                            Long proveedorId, Estado estado,
                                                            Pageable pageable) {
        Page<Producto> productos = productoRepository.findByFilters(
            nombre, codigo, categoriaId, marcaId, proveedorId, estado, pageable);
        
        List<ProductoDTO> content = productos.getContent()
            .stream()
            .map(productoMapper::toDTO)
            .toList();

        return PaginatedResponseDTO.<ProductoDTO>builder()
            .content(content)
            .page(productos.getNumber())
            .size(productos.getSize())
            .totalElements(productos.getTotalElements())
            .totalPages(productos.getTotalPages())
            .first(productos.isFirst())
            .last(productos.isLast())
            .empty(productos.isEmpty())
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarConStockBajo() {
        return productoRepository.findProductosConStockBajo()
            .stream()
            .map(productoMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId)
            .stream()
            .map(productoMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarPorMarca(Long marcaId) {
        return productoRepository.findByMarcaId(marcaId)
            .stream()
            .map(productoMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarPorProveedor(Long proveedorId) {
        return productoRepository.findByProveedorId(proveedorId)
            .stream()
            .map(productoMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando producto con ID: {}", id);
        
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setEstado(Estado.INACTIVO);
        productoRepository.save(producto);
        
        log.info("Producto marcado como inactivo");
    }

    @Override
    @Transactional
    public ProductoDTO cambiarEstado(Long id, Estado estado) {
        log.info("Cambiando estado del producto {} a {}", id, estado);
        
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setEstado(estado);
        producto = productoRepository.save(producto);
        
        return productoMapper.toDTO(producto);
    }

    @Override
    @Transactional
    public ProductoDTO actualizarStock(Long id, Integer nuevoStock) {
        log.info("Actualizando stock del producto {} a {}", id, nuevoStock);
        
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setStock(nuevoStock);
        producto = productoRepository.save(producto);
        
        return productoMapper.toDTO(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCodigo(String codigo) {
        return productoRepository.existsByCodigo(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCodigoBarras(String codigoBarras) {
        return productoRepository.existsByCodigoBarras(codigoBarras);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarPorTexto(String texto) {
        return productoRepository.findByTextoContaining(texto)
            .stream()
            .map(productoMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> buscarPorRangoPrecio(Double precioMin, Double precioMax) {
        return productoRepository.findByRangoPrecio(precioMin, precioMax)
            .stream()
            .map(productoMapper::toDTO)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarMasVendidos(int limite) {
        Pageable pageable = Pageable.ofSize(limite);
        return productoRepository.findMasVendidos(pageable)
            .stream()
            .map(productoMapper::toDTO)
            .toList();
    }
}
