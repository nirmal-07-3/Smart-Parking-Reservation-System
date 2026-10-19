
package com.smartparking.dao;

import com.smartparking.config.JPAUtil;
import com.smartparking.entity.ParkingLocation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ParkingLocationDAOImpl implements ParkingLocationDAO {

    @Override
    public ParkingLocation save(ParkingLocation parkingLocation) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(parkingLocation);
            tx.commit();
            return parkingLocation;
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
    public Optional<ParkingLocation> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(
                    em.find(ParkingLocation.class, id)
            );
        } finally {
            em.close();
        }
    }

    @Override
    public List<ParkingLocation> findByProviderId(Long providerId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM ParkingLocation p " +
                            "WHERE p.providerId = :providerId ORDER BY p.id DESC",
                    ParkingLocation.class
            ).setParameter("providerId", providerId).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<ParkingLocation> findActiveLocations() {
        return findByStatus(ParkingLocation.Status.ACTIVE);
    }

    @Override
    public List<ParkingLocation> findByStatus(
            ParkingLocation.Status status) {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM ParkingLocation p " +
                            "WHERE p.status = :status ORDER BY p.createdAt DESC",
                    ParkingLocation.class
            ).setParameter("status", status).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public ParkingLocation update(ParkingLocation parkingLocation) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ParkingLocation updated = em.merge(parkingLocation);
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
