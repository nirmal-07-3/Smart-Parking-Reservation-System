package com.smartparking.dao;

import com.smartparking.config.JPAUtil;
import com.smartparking.entity.ProviderApplication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ProviderApplicationDAOImpl implements ProviderApplicationDAO {

    @Override
    public ProviderApplication save(ProviderApplication application) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(application);
            tx.commit();
            return application;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<ProviderApplication> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(ProviderApplication.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<ProviderApplication> findByProviderId(Long providerId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT a FROM ProviderApplication a WHERE a.providerId = :providerId ORDER BY a.submittedAt DESC",
                    ProviderApplication.class
            ).setParameter("providerId", providerId).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<ProviderApplication> findByStatus(ProviderApplication.Status status) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT a FROM ProviderApplication a WHERE a.status = :status ORDER BY a.submittedAt",
                    ProviderApplication.class
            ).setParameter("status", status).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public ProviderApplication update(ProviderApplication application) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ProviderApplication updated = em.merge(application);
            tx.commit();
            return updated;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}