package com.nove.sule.backend_nove_sule.controller;

import com.nove.sule.backend_nove_sule.dto.catalogo.ProductoDTO;
import com.nove.sule.backend_nove_sule.dto.catalogo.ProductoRequestDTO;
import com.nove.sule.backend_nove_sule.dto.common.ApiResponseDTO;
import com.nove.sule.backend_nove_sule.dto.common.PaginatedResponseDTO;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.service.ProductoService;
import com.nove.sule.backend_nove_sule.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para gestión de productos
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_BASE_PATH + "/productos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Productos", description = "Endpoints para gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Crear producto", description = "Crea un nuevo producto en el catálogo")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> crear(@Valid @RequestBody ProductoRequestDTO productoRequest) {
        try {
            ProductoDTO nuevoProducto = productoService.crear(productoRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Producto creado exitosamente", nuevoProducto));
                
        } catch (Exception e) {
            log.error("Error creando producto: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO productoRequest) {
        try {
            ProductoDTO productoActualizado = productoService.actualizar(id, productoRequest);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Producto actualizado exitosamente", productoActualizado));
                
        } catch (Exception e) {
            log.error("Error actualizando producto {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Buscar producto por ID", description = "Obtiene un producto por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id)
            .map(producto -> ResponseEntity.ok(ApiResponseDTO.success(producto)))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar por código", description = "Busca un producto por su código SKU")
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> buscarPorCodigo(@PathVariable String codigo) {
        return productoService.buscarPorCodigo(codigo)
            .map(producto -> ResponseEntity.ok(ApiResponseDTO.success(producto)))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar por código de barras", description = "Busca un producto por su código de barras")
    @GetMapping("/codigo-barras/{codigoBarras}")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> buscarPorCodigoBarras(@PathVariable String codigoBarras) {
        return productoService.buscarPorCodigoBarras(codigoBarras)
            .map(producto -> ResponseEntity.ok(ApiResponseDTO.success(producto)))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar productos", description = "Lista productos con paginación y filtros")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PaginatedResponseDTO<ProductoDTO>>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) Long proveedorId,
            @RequestParam(required = false) Estado estado,
            Pageable pageable) {
        
        PaginatedResponseDTO<ProductoDTO> productos = productoService.buscarConFiltros(
            nombre, codigo, categoriaId, marcaId, proveedorId, estado, pageable);
        
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @Operation(summary = "Productos con stock bajo", description = "Lista productos con stock por debajo del mínimo")
    @GetMapping("/stock-bajo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> listarConStockBajo() {
        List<ProductoDTO> productos = productoService.listarConStockBajo();
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @Operation(summary = "Productos por categoría", description = "Lista productos de una categoría específica")
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> listarPorCategoria(@PathVariable Long categoriaId) {
        List<ProductoDTO> productos = productoService.listarPorCategoria(categoriaId);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @Operation(summary = "Productos por marca", description = "Lista productos de una marca específica")
    @GetMapping("/marca/{marcaId}")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> listarPorMarca(@PathVariable Long marcaId) {
        List<ProductoDTO> productos = productoService.listarPorMarca(marcaId);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @Operation(summary = "Actualizar stock", description = "Actualiza el stock de un producto")
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> actualizarStock(
            @PathVariable Long id,
            @RequestParam Integer nuevoStock) {
        try {
            ProductoDTO producto = productoService.actualizarStock(id, nuevoStock);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Stock actualizado exitosamente", producto));
                
        } catch (Exception e) {
            log.error("Error actualizando stock del producto {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Cambiar estado", description = "Cambia el estado de un producto")
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<ProductoDTO>> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Estado estado) {
        try {
            ProductoDTO producto = productoService.cambiarEstado(id, estado);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Estado cambiado exitosamente", producto));
                
        } catch (Exception e) {
            log.error("Error cambiando estado del producto {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto (soft delete)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE')")
    public ResponseEntity<ApiResponseDTO<String>> eliminar(@PathVariable Long id) {
        try {
            productoService.eliminar(id);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Producto eliminado exitosamente"));
                
        } catch (Exception e) {
            log.error("Error eliminando producto {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @Operation(summary = "Verificar código", description = "Verifica si un código ya existe")
    @GetMapping("/verificar-codigo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarCodigo(@RequestParam String codigo) {
        boolean existe = productoService.existeCodigo(codigo);
        return ResponseEntity.ok(ApiResponseDTO.success(existe));
    }

    @Operation(summary = "Verificar código de barras", description = "Verifica si un código de barras ya existe")
    @GetMapping("/verificar-codigo-barras")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE') or hasRole('ALMACENERO')")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarCodigoBarras(@RequestParam String codigoBarras) {
        boolean existe = productoService.existeCodigoBarras(codigoBarras);
        return ResponseEntity.ok(ApiResponseDTO.success(existe));
    }

    @Operation(summary = "Buscar productos por texto", description = "Busca productos por texto en nombre o descripción")
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> buscarPorTexto(@RequestParam String texto) {
        List<ProductoDTO> productos = productoService.buscarPorTexto(texto);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @Operation(summary = "Buscar productos por rango de precio", description = "Busca productos dentro de un rango de precios")
    @GetMapping("/precio-rango")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> buscarPorRangoPrecio(
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax) {
        List<ProductoDTO> productos = productoService.buscarPorRangoPrecio(precioMin, precioMax);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @Operation(summary = "Productos por proveedor", description = "Lista productos de un proveedor específico")
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> listarPorProveedor(@PathVariable Long proveedorId) {
        List<ProductoDTO> productos = productoService.listarPorProveedor(proveedorId);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }

    @Operation(summary = "Productos más vendidos", description = "Lista los productos más vendidos")
    @GetMapping("/mas-vendidos")
    public ResponseEntity<ApiResponseDTO<List<ProductoDTO>>> listarMasVendidos(
            @RequestParam(defaultValue = "10") int limite) {
        List<ProductoDTO> productos = productoService.listarMasVendidos(limite);
        return ResponseEntity.ok(ApiResponseDTO.success(productos));
    }
}
