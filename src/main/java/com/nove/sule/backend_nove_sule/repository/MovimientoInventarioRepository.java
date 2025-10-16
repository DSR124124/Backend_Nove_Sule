package com.nove.sule.backend_nove_sule.repository;

import com.nove.sule.backend_nove_sule.entity.MovimientoInventario;
import com.nove.sule.backend_nove_sule.entity.enums.TipoMovimiento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Repositorio para MovimientoInventario
 */
@Repository
public class MovimientoInventarioRepository implements JpaRepository<MovimientoInventario, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    // ===== MÉTODOS PERSONALIZADOS =====

    /**
     * Busca movimientos con filtros usando consultas dinámicas
     */
    public Page<MovimientoInventario> findByFiltersCustom(Long productoId, TipoMovimiento tipoMovimiento, 
                                                         LocalDateTime fechaInicio, LocalDateTime fechaFin, 
                                                         Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MovimientoInventario m WHERE 1=1");
        
        if (productoId != null) {
            queryBuilder.append(" AND m.producto.id = :productoId");
        }
        if (tipoMovimiento != null) {
            queryBuilder.append(" AND m.tipoMovimiento = :tipoMovimiento");
        }
        if (fechaInicio != null) {
            queryBuilder.append(" AND m.fechaMovimiento >= :fechaInicio");
        }
        if (fechaFin != null) {
            queryBuilder.append(" AND m.fechaMovimiento <= :fechaFin");
        }
        
        queryBuilder.append(" ORDER BY m.fechaMovimiento DESC");

        TypedQuery<MovimientoInventario> query = entityManager.createQuery(queryBuilder.toString(), MovimientoInventario.class);
        
        if (productoId != null) {
            query.setParameter("productoId", productoId);
        }
        if (tipoMovimiento != null) {
            query.setParameter("tipoMovimiento", tipoMovimiento);
        }
        if (fechaInicio != null) {
            query.setParameter("fechaInicio", fechaInicio);
        }
        if (fechaFin != null) {
            query.setParameter("fechaFin", fechaFin);
        }

        // Apply pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<MovimientoInventario> content = query.getResultList();

        // Count query
        StringBuilder countQueryBuilder = new StringBuilder();
        countQueryBuilder.append("SELECT COUNT(m) FROM MovimientoInventario m WHERE 1=1");
        
        if (productoId != null) {
            countQueryBuilder.append(" AND m.producto.id = :productoId");
        }
        if (tipoMovimiento != null) {
            countQueryBuilder.append(" AND m.tipoMovimiento = :tipoMovimiento");
        }
        if (fechaInicio != null) {
            countQueryBuilder.append(" AND m.fechaMovimiento >= :fechaInicio");
        }
        if (fechaFin != null) {
            countQueryBuilder.append(" AND m.fechaMovimiento <= :fechaFin");
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryBuilder.toString(), Long.class);
        
        if (productoId != null) {
            countQuery.setParameter("productoId", productoId);
        }
        if (tipoMovimiento != null) {
            countQuery.setParameter("tipoMovimiento", tipoMovimiento);
        }
        if (fechaInicio != null) {
            countQuery.setParameter("fechaInicio", fechaInicio);
        }
        if (fechaFin != null) {
            countQuery.setParameter("fechaFin", fechaFin);
        }

        Long total = countQuery.getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

    // ===== MÉTODOS DE CONSULTA ESPECÍFICOS =====

    public List<MovimientoInventario> findByProductoIdOrderByFechaMovimientoDesc(Long productoId) {
        return entityManager.createQuery(
            "SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId ORDER BY m.fechaMovimiento DESC", 
            MovimientoInventario.class)
            .setParameter("productoId", productoId)
            .getResultList();
    }

    public List<MovimientoInventario> findByTipoMovimientoOrderByFechaMovimientoDesc(TipoMovimiento tipoMovimiento) {
        return entityManager.createQuery(
            "SELECT m FROM MovimientoInventario m WHERE m.tipoMovimiento = :tipoMovimiento ORDER BY m.fechaMovimiento DESC", 
            MovimientoInventario.class)
            .setParameter("tipoMovimiento", tipoMovimiento)
            .getResultList();
    }

    public List<MovimientoInventario> findByProductoIdWithDetails(Long productoId) {
        return entityManager.createQuery(
            "SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId ORDER BY m.fechaMovimiento DESC", 
            MovimientoInventario.class)
            .setParameter("productoId", productoId)
            .getResultList();
    }

    public List<MovimientoInventario> findByProductoIdAndFechaRange(Long productoId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return entityManager.createQuery(
            "SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId AND " +
           "m.fechaMovimiento >= :fechaInicio AND m.fechaMovimiento <= :fechaFin " +
            "ORDER BY m.fechaMovimiento DESC", 
            MovimientoInventario.class)
            .setParameter("productoId", productoId)
            .setParameter("fechaInicio", fechaInicio)
            .setParameter("fechaFin", fechaFin)
            .getResultList();
    }

    public List<MovimientoInventario> findByOrdenCompraId(Long ordenCompraId) {
        return entityManager.createQuery(
            "SELECT m FROM MovimientoInventario m WHERE m.ordenCompra.id = :ordenCompraId", 
            MovimientoInventario.class)
            .setParameter("ordenCompraId", ordenCompraId)
            .getResultList();
    }

    public List<MovimientoInventario> findByComprobanteVentaId(Long comprobanteVentaId) {
        return entityManager.createQuery(
            "SELECT m FROM MovimientoInventario m WHERE m.comprobanteVenta.id = :comprobanteVentaId", 
            MovimientoInventario.class)
            .setParameter("comprobanteVentaId", comprobanteVentaId)
            .getResultList();
    }

    public Optional<MovimientoInventario> findUltimoMovimientoByProductoId(Long productoId) {
        List<MovimientoInventario> result = entityManager.createQuery(
            "SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
            "ORDER BY m.fechaMovimiento DESC", 
            MovimientoInventario.class)
            .setParameter("productoId", productoId)
            .setMaxResults(1)
            .getResultList();
        
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public Integer sumCantidadByProductoAndTipoAndFechaRange(Long productoId, TipoMovimiento tipoMovimiento, 
                                                           LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return entityManager.createQuery(
            "SELECT SUM(m.cantidad) FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
           "AND m.tipoMovimiento = :tipoMovimiento AND m.fechaMovimiento >= :fechaInicio " +
            "AND m.fechaMovimiento <= :fechaFin", 
            Integer.class)
            .setParameter("productoId", productoId)
            .setParameter("tipoMovimiento", tipoMovimiento)
            .setParameter("fechaInicio", fechaInicio)
            .setParameter("fechaFin", fechaFin)
            .getSingleResult();
    }

    public Long countMovimientosByProductoAndFechaRange(Long productoId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return entityManager.createQuery(
            "SELECT COUNT(m) FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
            "AND m.fechaMovimiento >= :fechaInicio AND m.fechaMovimiento <= :fechaFin", 
            Long.class)
            .setParameter("productoId", productoId)
            .setParameter("fechaInicio", fechaInicio)
            .setParameter("fechaFin", fechaFin)
            .getSingleResult();
    }

    public List<MovimientoInventario> findByFechaRange(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return entityManager.createQuery(
            "SELECT m FROM MovimientoInventario m WHERE m.fechaMovimiento >= :fechaInicio " +
            "AND m.fechaMovimiento <= :fechaFin ORDER BY m.fechaMovimiento DESC", 
            MovimientoInventario.class)
            .setParameter("fechaInicio", fechaInicio)
            .setParameter("fechaFin", fechaFin)
            .getResultList();
    }

    public List<MovimientoInventario> findByUsuarioId(Long usuarioId) {
        return entityManager.createQuery(
            "SELECT m FROM MovimientoInventario m WHERE m.usuario.id = :usuarioId " +
            "ORDER BY m.fechaMovimiento DESC", 
            MovimientoInventario.class)
            .setParameter("usuarioId", usuarioId)
            .getResultList();
    }

    public List<MovimientoInventario> findByProductoIdAndTipoMovimiento(Long productoId, TipoMovimiento tipoMovimiento) {
        return entityManager.createQuery(
            "SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId " +
            "AND m.tipoMovimiento = :tipoMovimiento ORDER BY m.fechaMovimiento DESC", 
            MovimientoInventario.class)
            .setParameter("productoId", productoId)
            .setParameter("tipoMovimiento", tipoMovimiento)
            .getResultList();
    }

    public List<MovimientoInventario> findMovimientosRecientes(LocalDateTime fecha) {
        return entityManager.createQuery(
            "SELECT m FROM MovimientoInventario m WHERE m.fechaMovimiento >= :fecha " +
            "ORDER BY m.fechaMovimiento DESC", 
            MovimientoInventario.class)
            .setParameter("fecha", fecha)
            .getResultList();
    }

    // ===== IMPLEMENTACIÓN DE JpaRepository =====

    @Override
    public List<MovimientoInventario> findAll() {
        return entityManager.createQuery("SELECT m FROM MovimientoInventario m", MovimientoInventario.class).getResultList();
    }

    @Override
    public List<MovimientoInventario> findAll(Sort sort) {
        // Implementación básica - en un caso real se podría mejorar
        return findAll();
    }

    @Override
    public Page<MovimientoInventario> findAll(Pageable pageable) {
        TypedQuery<MovimientoInventario> query = entityManager.createQuery("SELECT m FROM MovimientoInventario m", MovimientoInventario.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        
        List<MovimientoInventario> content = query.getResultList();
        
        Long total = entityManager.createQuery("SELECT COUNT(m) FROM MovimientoInventario m", Long.class).getSingleResult();
        
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<MovimientoInventario> findAllById(Iterable<Long> ids) {
        // Implementación básica
        return entityManager.createQuery("SELECT m FROM MovimientoInventario m WHERE m.id IN :ids", MovimientoInventario.class)
            .setParameter("ids", ids)
            .getResultList();
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(m) FROM MovimientoInventario m", Long.class).getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        MovimientoInventario entity = findById(id).orElse(null);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    @Override
    public void delete(MovimientoInventario entity) {
        entityManager.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        for (Long id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends MovimientoInventario> entities) {
        for (MovimientoInventario entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM MovimientoInventario").executeUpdate();
    }

    @Override
    public <S extends MovimientoInventario> S save(S entity) {
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
        return entity;
    }

    @Override
    public <S extends MovimientoInventario> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new java.util.ArrayList<>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public Optional<MovimientoInventario> findById(Long id) {
        MovimientoInventario entity = entityManager.find(MovimientoInventario.class, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public <S extends MovimientoInventario> S saveAndFlush(S entity) {
        S result = save(entity);
        flush();
        return result;
    }

    @Override
    public <S extends MovimientoInventario> List<S> saveAllAndFlush(Iterable<S> entities) {
        List<S> result = saveAll(entities);
        flush();
        return result;
    }

    @Override
    public void deleteAllInBatch(Iterable<MovimientoInventario> entities) {
        for (MovimientoInventario entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
        deleteAllById(ids);
    }

    @Override
    public void deleteAllInBatch() {
        deleteAll();
    }

    @Override
    public MovimientoInventario getReferenceById(Long id) {
        return entityManager.getReference(MovimientoInventario.class, id);
    }

    @Override
    @Deprecated
    public MovimientoInventario getById(Long id) {
        return entityManager.find(MovimientoInventario.class, id);
    }

    @Override
    @Deprecated
    public MovimientoInventario getOne(Long id) {
        return entityManager.getReference(MovimientoInventario.class, id);
    }

    // ===== IMPLEMENTACIÓN DE QueryByExampleExecutor =====

    @Override
    public <S extends MovimientoInventario> Optional<S> findOne(Example<S> example) {
        // Implementación básica - en un caso real se podría mejorar
        return Optional.empty();
    }

    @Override
    public <S extends MovimientoInventario> List<S> findAll(Example<S> example) {
        // Implementación básica - en un caso real se podría mejorar
        return List.of();
    }

    @Override
    public <S extends MovimientoInventario> List<S> findAll(Example<S> example, Sort sort) {
        // Implementación básica - en un caso real se podría mejorar
        return List.of();
    }

    @Override
    public <S extends MovimientoInventario> Page<S> findAll(Example<S> example, Pageable pageable) {
        // Implementación básica - en un caso real se podría mejorar
        return new PageImpl<>(List.of(), pageable, 0);
    }

    @Override
    public <S extends MovimientoInventario> long count(Example<S> example) {
        // Implementación básica - en un caso real se podría mejorar
        return 0;
    }

    @Override
    public <S extends MovimientoInventario> boolean exists(Example<S> example) {
        // Implementación básica - en un caso real se podría mejorar
        return false;
    }

    @Override
    public <S extends MovimientoInventario, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        // Implementación básica - en un caso real se podría mejorar
        return null;
    }
}