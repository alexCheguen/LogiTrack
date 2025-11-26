package com.achegueng.logitrack.repositories;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T, ID>{

    @Inject
    protected EntityManager em;

    protected abstract Class<T> entity();

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(entity(), id));
    }

    public List<T> getAll() {
        String entityName = em.getMetamodel().entity(entity()).getName();

        return em.createQuery(MessageFormat.format("SELECT e FROM {0} e", entityName), entity()).getResultList();
    }

    public Optional<T> save(T entity) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
            if (id == null) {
                em.persist(entity);
            } else {
                em.merge(entity);
            }
            transaction.commit();

            return Optional.ofNullable(entity);
        } catch (Exception ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    public void delete(T entity) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }
}
