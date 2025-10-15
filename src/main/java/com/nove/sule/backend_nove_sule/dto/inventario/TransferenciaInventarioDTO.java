package com.nove.sule.backend_nove_sule.dto.inventario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferencia de inventario entre ubicaciones
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferenciaInventarioDTO {

    @NotNull(message = "El producto es requerido")
    private Long productoId;

    @NotNull(message = "La cantidad es requerida")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotBlank(message = "La ubicación origen es requerida")
    @Size(max = 50, message = "La ubicación origen no puede exceder 50 caracteres")
    private String ubicacionOrigen;

    @NotBlank(message = "La ubicación destino es requerida")
    @Size(max = 50, message = "La ubicación destino no puede exceder 50 caracteres")
    private String ubicacionDestino;

    @NotBlank(message = "El concepto es requerido")
    @Size(max = 100, message = "El concepto no puede exceder 100 caracteres")
    private String concepto;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    private LocalDateTime fechaTransferencia;
}
