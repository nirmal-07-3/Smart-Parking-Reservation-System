package com.smartparking.dao;

import com.smartparking.config.JPAUtil;
import com.smartparking.entity.ParkingSlot;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class ParkingSlotDAOImpl implements ParkingSlotDAO {

    @Override
    public ParkingSlot save(ParkingSlot slot) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(slot);
            tx.commit();
            return slot;
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
    public Optional<ParkingSlot> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(ParkingSlot.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<ParkingSlot> findByParkingLocationId(Long parkingLocationId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT s FROM ParkingSlot s WHERE s.parkingLocationId = :locationId ORDER BY s.slotNumber",
                    ParkingSlot.class
            ).setParameter("locationId", parkingLocationId).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<ParkingSlot> findAvailableSlots(Long parkingLocationId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM ParkingSlot s WHERE s.parkingLocationId = :locationId AND s.status = :status ORDER BY s.slotNumber",
                            ParkingSlot.class
                    ).setParameter("locationId", parkingLocationId)
                    .setParameter("status", ParkingSlot.Status.AVAILABLE)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public ParkingSlot update(ParkingSlot slot) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            ParkingSlot updated = em.merge(slot);
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