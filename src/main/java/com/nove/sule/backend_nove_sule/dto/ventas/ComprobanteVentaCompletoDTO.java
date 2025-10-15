package com.nove.sule.backend_nove_sule.dto.ventas;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.MedioPago;
import com.nove.sule.backend_nove_sule.entity.enums.Moneda;
import com.nove.sule.backend_nove_sule.entity.enums.TipoComprobante;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO completo para Comprobante de Venta con todos los detalles
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobanteVentaCompletoDTO {

    private Long id;
    private TipoComprobante tipoComprobante;
    private String serie;
    private String numero;
    private String numeroCompleto;
    private ClienteBasicoDTO cliente;
    private LocalDateTime fechaEmision;
    private Moneda moneda;
    private BigDecimal tipoCambio;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal igv;
    private BigDecimal total;
    private Estado estado;
    private MedioPago medioPago;
    private String observaciones;
    private String usuario;
    private String caja;
    private List<DetalleComprobanteDTO> detalles;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
