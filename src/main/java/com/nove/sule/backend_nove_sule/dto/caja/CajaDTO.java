package com.nove.sule.backend_nove_sule.dto.caja;

import com.nove.sule.backend_nove_sule.dto.usuario.EmpleadoBasicoDTO;
import com.nove.sule.backend_nove_sule.entity.Caja;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para Caja
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CajaDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private LocalDate fechaApertura;
    private EmpleadoBasicoDTO responsable;
    private Caja.EstadoCaja estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}
