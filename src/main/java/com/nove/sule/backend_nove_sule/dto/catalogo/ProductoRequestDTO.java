package com.nove.sule.backend_nove_sule.dto.catalogo;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoIGV;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para request de Producto (crear/actualizar)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    private String descripcion;

    @NotBlank(message = "El código es requerido")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    private String codigo;

    @Size(max = 50, message = "El código de barras no puede exceder 50 caracteres")
    private String codigoBarras;

    @NotNull(message = "El precio es requerido")
    @PositiveOrZero(message = "El precio debe ser positivo o cero")
    private BigDecimal precio;

    @PositiveOrZero(message = "El precio de compra debe ser positivo o cero")
    private BigDecimal precioCompra;

    @PositiveOrZero(message = "El margen debe ser positivo o cero")
    private BigDecimal margen;

    @NotNull(message = "El stock es requerido")
    @PositiveOrZero(message = "El stock debe ser positivo o cero")
    private Integer stock;

    @NotNull(message = "El stock mínimo es requerido")
    @PositiveOrZero(message = "El stock mínimo debe ser positivo o cero")
    private Integer stockMinimo;

    @PositiveOrZero(message = "El stock máximo debe ser positivo o cero")
    private Integer stockMaximo;

    @NotNull(message = "La categoría es requerida")
    private Long categoriaId;

    private Long marcaId;

    private Long proveedorId;

    @NotBlank(message = "La unidad es requerida")
    @Size(max = 20, message = "La unidad no puede exceder 20 caracteres")
    private String unidad;

    @PositiveOrZero(message = "El peso debe ser positivo o cero")
    private BigDecimal peso;

    @PositiveOrZero(message = "El largo debe ser positivo o cero")
    private BigDecimal largo;

    @PositiveOrZero(message = "El ancho debe ser positivo o cero")
    private BigDecimal ancho;

    @PositiveOrZero(message = "El alto debe ser positivo o cero")
    private BigDecimal alto;

    private Boolean afectoIgv;

    private TipoIGV tipoIgv;

    @Size(max = 50, message = "La ubicación no puede exceder 50 caracteres")
    private String ubicacion;

    @Size(max = 50, message = "El lote no puede exceder 50 caracteres")
    private String lote;

    private LocalDate fechaVencimiento;

    private List<String> tags;

    @Size(max = 255, message = "La URL de imagen no puede exceder 255 caracteres")
    private String imagen;

    private List<String> imagenes;

    private String observaciones;

    private Estado estado;
}
