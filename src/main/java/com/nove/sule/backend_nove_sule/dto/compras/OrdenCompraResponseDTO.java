package com.nove.sule.backend_nove_sule.dto.compras;

import com.nove.sule.backend_nove_sule.entity.enums.EstadoOrdenCompra;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para Orden de Compra (versión simplificada para listados)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompraResponseDTO {

    private Long id;
    private String numero;
    private String proveedorNombre;
    private String proveedorRuc;
    private LocalDate fechaOrden;
    private LocalDate fechaEntregaEsperada;
    private EstadoOrdenCompra estado;
    private BigDecimal total;
    private String usuario;
    private LocalDateTime fechaCreacion;
}
