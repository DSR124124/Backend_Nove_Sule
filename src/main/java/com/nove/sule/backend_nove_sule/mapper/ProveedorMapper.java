package com.nove.sule.backend_nove_sule.mapper;

import com.nove.sule.backend_nove_sule.dto.catalogo.ProveedorBasicoDTO;
import com.nove.sule.backend_nove_sule.dto.catalogo.ProveedorRequestDTO;
import com.nove.sule.backend_nove_sule.entity.Proveedor;
import com.nove.sule.backend_nove_sule.entity.enums.TipoCuenta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper para Proveedor
 */
@Component
public class ProveedorMapper {

    public ProveedorBasicoDTO toDTO(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }

        return ProveedorBasicoDTO.builder()
                .id(proveedor.getId())
                .nombre(proveedor.getNombre())
                .razonSocial(proveedor.getRazonSocial())
                .ruc(proveedor.getRuc())
                .telefono(proveedor.getTelefono())
                .email(proveedor.getEmail())
                .build();
    }

    public Proveedor toEntity(ProveedorRequestDTO proveedorRequest) {
        if (proveedorRequest == null) {
            return null;
        }

        return Proveedor.builder()
                .nombre(proveedorRequest.getNombre())
                .razonSocial(proveedorRequest.getRazonSocial())
                .ruc(proveedorRequest.getRuc())
                .direccion(proveedorRequest.getDireccion())
                .distrito(proveedorRequest.getDistrito())
                .provincia(proveedorRequest.getProvincia())
                .departamento(proveedorRequest.getDepartamento())
                .codigoPostal(proveedorRequest.getCodigoPostal())
                .telefono(proveedorRequest.getTelefono())
                .email(proveedorRequest.getEmail())
                .sitioWeb(proveedorRequest.getSitioWeb())
                .contacto(proveedorRequest.getContacto())
                .cargo(proveedorRequest.getCargo())
                .banco(proveedorRequest.getBanco())
                .numeroCuenta(proveedorRequest.getNumeroCuenta())
                .tipoCuenta(proveedorRequest.getTipoCuenta() != null ? 
                    TipoCuenta.valueOf(proveedorRequest.getTipoCuenta()) : null)
                .plazoPago(proveedorRequest.getPlazoPago() != null ? 
                    proveedorRequest.getPlazoPago() : 30)
                .descuento(proveedorRequest.getDescuento() != null ? 
                    proveedorRequest.getDescuento() : BigDecimal.ZERO)
                .limiteCredito(proveedorRequest.getLimiteCredito() != null ? 
                    proveedorRequest.getLimiteCredito() : BigDecimal.ZERO)
                .observaciones(proveedorRequest.getObservaciones())
                .build();
    }

    public void updateEntity(Proveedor proveedor, ProveedorRequestDTO proveedorRequest) {
        if (proveedor == null || proveedorRequest == null) {
            return;
        }

        proveedor.setNombre(proveedorRequest.getNombre());
        proveedor.setRazonSocial(proveedorRequest.getRazonSocial());
        proveedor.setRuc(proveedorRequest.getRuc());
        proveedor.setDireccion(proveedorRequest.getDireccion());
        proveedor.setDistrito(proveedorRequest.getDistrito());
        proveedor.setProvincia(proveedorRequest.getProvincia());
        proveedor.setDepartamento(proveedorRequest.getDepartamento());
        proveedor.setCodigoPostal(proveedorRequest.getCodigoPostal());
        proveedor.setTelefono(proveedorRequest.getTelefono());
        proveedor.setEmail(proveedorRequest.getEmail());
        proveedor.setSitioWeb(proveedorRequest.getSitioWeb());
        proveedor.setContacto(proveedorRequest.getContacto());
        proveedor.setCargo(proveedorRequest.getCargo());
        proveedor.setBanco(proveedorRequest.getBanco());
        proveedor.setNumeroCuenta(proveedorRequest.getNumeroCuenta());
        proveedor.setTipoCuenta(proveedorRequest.getTipoCuenta() != null ? 
            TipoCuenta.valueOf(proveedorRequest.getTipoCuenta()) : null);
        proveedor.setPlazoPago(proveedorRequest.getPlazoPago() != null ? 
            proveedorRequest.getPlazoPago() : 30);
        proveedor.setDescuento(proveedorRequest.getDescuento() != null ? 
            proveedorRequest.getDescuento() : BigDecimal.ZERO);
        proveedor.setLimiteCredito(proveedorRequest.getLimiteCredito() != null ? 
            proveedorRequest.getLimiteCredito() : BigDecimal.ZERO);
        proveedor.setObservaciones(proveedorRequest.getObservaciones());
    }
}
