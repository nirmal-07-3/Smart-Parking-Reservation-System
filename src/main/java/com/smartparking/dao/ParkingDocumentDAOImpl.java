package com.smartparking.dao;

import com.smartparking.config.JPAUtil;
import com.smartparking.entity.ParkingDocument;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ParkingDocumentDAOImpl implements ParkingDocumentDAO {

    @Override
    public ParkingDocument save(ParkingDocument document) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(document);
            tx.commit();
            return document;
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
    public Optional<ParkingDocument> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(ParkingDocument.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<ParkingDocument> findByApplicationId(Long applicationId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT d FROM ParkingDocument d WHERE d.applicationId = :applicationId ORDER BY d.uploadedAt DESC",
                    ParkingDocument.class
            ).setParameter("applicationId", applicationId).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<ParkingDocument> findByStatus(ParkingDocument.Status status) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT d FROM ParkingDocument d WHERE d.status = :status ORDER BY d.uploadedAt",
                    ParkingDocument.class
            ).setParameter("status", status).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public ParkingDocument update(ParkingDocument document) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ParkingDocument updated = em.merge(document);
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