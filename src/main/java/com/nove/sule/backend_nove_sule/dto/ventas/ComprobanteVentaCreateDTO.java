package com.nove.sule.backend_nove_sule.dto.ventas;

import com.nove.sule.backend_nove_sule.entity.enums.MedioPago;
import com.nove.sule.backend_nove_sule.entity.enums.Moneda;
import com.nove.sule.backend_nove_sule.entity.enums.TipoComprobante;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para crear Comprobante de Venta (versión simplificada)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobanteVentaCreateDTO {

    @NotNull(message = "El tipo de comprobante es requerido")
    private TipoComprobante tipoComprobante;

    @NotNull(message = "El cliente es requerido")
    private Long clienteId;

    private LocalDateTime fechaEmision;

    @NotNull(message = "La moneda es requerida")
    private Moneda moneda;

    @DecimalMin(value = "0.0", message = "El tipo de cambio debe ser mayor o igual a 0")
    private BigDecimal tipoCambio;

    @DecimalMin(value = "0.0", message = "El descuento debe ser mayor o igual a 0")
    private BigDecimal descuento;

    @NotNull(message = "El medio de pago es requerido")
    private MedioPago medioPago;

    private String observaciones;

    @NotEmpty(message = "Debe incluir al menos un detalle")
    @Valid
    private List<DetalleComprobanteCreateDTO> detalles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetalleComprobanteCreateDTO {
        
        @NotNull(message = "El producto es requerido")
        private Long productoId;

        @NotNull(message = "La cantidad es requerida")
        @Positive(message = "La cantidad debe ser mayor a 0")
        private BigDecimal cantidad;

        @NotNull(message = "El precio unitario es requerido")
        @DecimalMin(value = "0.0", message = "El precio unitario debe ser mayor o igual a 0")
        private BigDecimal precioUnitario;

        @DecimalMin(value = "0.0", message = "El descuento debe ser mayor o igual a 0")
        private BigDecimal descuento;

        private String observaciones;
    }
}
