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

/**
 * DTO de respuesta para Comprobante de Venta (versión simplificada para listados)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComprobanteVentaResponseDTO {

    private Long id;
    private TipoComprobante tipoComprobante;
    private String serie;
    private String numero;
    private String numeroCompleto;
    private String clienteNombre;
    private String clienteDocumento;
    private LocalDateTime fechaEmision;
    private Moneda moneda;
    private BigDecimal total;
    private Estado estado;
    private MedioPago medioPago;
    private String usuario;
    private String caja;
}
