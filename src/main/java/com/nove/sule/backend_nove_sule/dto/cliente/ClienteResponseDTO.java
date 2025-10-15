package com.nove.sule.backend_nove_sule.dto.cliente;

import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para Cliente (versión simplificada para listados)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    private Long id;
    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String direccion;
    private BigDecimal limiteCredito;
    private Estado estado;
    private LocalDateTime fechaCreacion;

    // Método para verificar si es persona jurídica
    public boolean esPersonaJuridica() {
        return tipoDocumento == TipoDocumento.RUC;
    }

    // Método para verificar si es persona natural
    public boolean esPersonaNatural() {
        return tipoDocumento == TipoDocumento.DNI;
    }
}
