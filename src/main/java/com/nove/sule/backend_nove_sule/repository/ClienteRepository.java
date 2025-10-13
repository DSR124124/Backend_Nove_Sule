package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.Cliente;
import com.nove.sule.backend_nove_sule.entity.enums.Estado;
import com.nove.sule.backend_nove_sule.entity.enums.TipoDocumento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Cliente
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByTipoDocumentoAndNumeroDocumento(TipoDocumento tipoDocumento, String numeroDocumento);

    boolean existsByTipoDocumentoAndNumeroDocumento(TipoDocumento tipoDocumento, String numeroDocumento);

    List<Cliente> findByEstadoOrderByNombresAsc(Estado estado);

    Page<Cliente> findByEstado(Estado estado, Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE " +
           "(:nombres IS NULL OR LOWER(c.nombres) LIKE LOWER(CONCAT('%', :nombres, '%'))) AND " +
           "(:apellidos IS NULL OR LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :apellidos, '%'))) AND " +
           "(:razonSocial IS NULL OR LOWER(c.razonSocial) LIKE LOWER(CONCAT('%', :razonSocial, '%'))) AND " +
           "(:numeroDocumento IS NULL OR c.numeroDocumento LIKE CONCAT('%', :numeroDocumento, '%')) AND " +
           "(:tipoDocumento IS NULL OR c.tipoDocumento = :tipoDocumento) AND " +
           "(:estado IS NULL OR c.estado = :estado)")
    Page<Cliente> findByFilters(@Param("nombres") String nombres,
                               @Param("apellidos") String apellidos,
                               @Param("razonSocial") String razonSocial,
                               @Param("numeroDocumento") String numeroDocumento,
                               @Param("tipoDocumento") TipoDocumento tipoDocumento,
                               @Param("estado") Estado estado,
                               Pageable pageable);

    List<Cliente> findByTipoDocumento(TipoDocumento tipoDocumento);

    @Query("SELECT c FROM Cliente c WHERE c.estado = 'ACTIVO' ORDER BY c.nombres ASC, c.razonSocial ASC")
    List<Cliente> findAllActiveOrderByName();
}
