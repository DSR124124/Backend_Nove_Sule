package com.nove.sule.backend_nove_sule.dto.catalogo;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoIGV;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para Producto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String codigo;
    private String codigoBarras;
    private BigDecimal precio;
    private BigDecimal precioCompra;
    private BigDecimal margen;
    private Integer stock;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private String unidad;
    private BigDecimal peso;
    private BigDecimal largo;
    private BigDecimal ancho;
    private BigDecimal alto;
    private Boolean afectoIgv;
    private TipoIGV tipoIgv;
    private String ubicacion;
    private String lote;
    private LocalDate fechaVencimiento;
    private List<String> tags;
    private String imagen;
    private List<String> imagenes;
    private String observaciones;
    private Estado estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    // Información de relaciones
    private CategoriaBasicaDTO categoria;
    private MarcaBasicaDTO marca;
    private ProveedorBasicoDTO proveedor;

    // Campos calculados
    private BigDecimal precioConIgv;
    private BigDecimal margenGanancia;
    private Integer stockDisponible;
    private Boolean stockBajo;
}
