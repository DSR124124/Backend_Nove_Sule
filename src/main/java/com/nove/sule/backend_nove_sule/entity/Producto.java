package com.nove.sule.backend_nove_sule.entity;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoIGV;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Entidad Producto
 */
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto extends BaseEntity {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotBlank(message = "El código es requerido")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    @Column(unique = true, nullable = false, length = 50)
    private String codigo;

    @Size(max = 50, message = "El código de barras no puede exceder 50 caracteres")
    @Column(name = "codigo_barras", length = 50)
    private String codigoBarras;

    @NotNull(message = "El precio es requerido")
    @PositiveOrZero(message = "El precio debe ser positivo o cero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @PositiveOrZero(message = "El precio de compra debe ser positivo o cero")
    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @PositiveOrZero(message = "El margen debe ser positivo o cero")
    @Column(precision = 5, scale = 2)
    private BigDecimal margen;

    @NotNull(message = "El stock es requerido")
    @PositiveOrZero(message = "El stock debe ser positivo o cero")
    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @NotNull(message = "El stock mínimo es requerido")
    @PositiveOrZero(message = "El stock mínimo debe ser positivo o cero")
    @Column(name = "stock_minimo", nullable = false)
    @Builder.Default
    private Integer stockMinimo = 0;

    @PositiveOrZero(message = "El stock máximo debe ser positivo o cero")
    @Column(name = "stock_maximo")
    private Integer stockMaximo;

    // Relación muchos a uno con Categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Relación muchos a uno con Marca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    // Relación muchos a uno con Proveedor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @NotBlank(message = "La unidad es requerida")
    @Size(max = 20, message = "La unidad no puede exceder 20 caracteres")
    @Column(nullable = false, length = 20)
    private String unidad;

    @PositiveOrZero(message = "El peso debe ser positivo o cero")
    @Column(precision = 8, scale = 3)
    private BigDecimal peso;

    @PositiveOrZero(message = "El largo debe ser positivo o cero")
    @Column(precision = 8, scale = 2)
    private BigDecimal largo;

    @PositiveOrZero(message = "El ancho debe ser positivo o cero")
    @Column(precision = 8, scale = 2)
    private BigDecimal ancho;

    @PositiveOrZero(message = "El alto debe ser positivo o cero")
    @Column(precision = 8, scale = 2)
    private BigDecimal alto;

    // Información fiscal
    @Column(name = "afecto_igv", nullable = false)
    @Builder.Default
    private Boolean afectoIgv = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_igv", length = 20)
    @Builder.Default
    private TipoIGV tipoIgv = TipoIGV.GRAVADO;

    // Información de inventario
    @Size(max = 50, message = "La ubicación no puede exceder 50 caracteres")
    @Column(length = 50)
    private String ubicacion;

    @Size(max = 50, message = "El lote no puede exceder 50 caracteres")
    @Column(length = 50)
    private String lote;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    // Campos JSON para tags e imágenes
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> tags;

    @Size(max = 255, message = "La URL de imagen no puede exceder 255 caracteres")
    @Column(length = 255)
    private String imagen;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> imagenes;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Estado estado = Estado.ACTIVO;

    // Métodos de conveniencia para cálculos
    public BigDecimal getPrecioConIgv() {
        if (afectoIgv && tipoIgv == TipoIGV.GRAVADO) {
            return precio.multiply(new BigDecimal("1.18")); // IGV 18%
        }
        return precio;
    }

    public BigDecimal getMargenGanancia() {
        if (precioCompra != null && precioCompra.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ganancia = precio.subtract(precioCompra);
            return ganancia.divide(precioCompra, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }

    public Integer getStockDisponible() {
        return stock;
    }

    public boolean isStockBajo() {
        return stock <= stockMinimo;
    }
}
