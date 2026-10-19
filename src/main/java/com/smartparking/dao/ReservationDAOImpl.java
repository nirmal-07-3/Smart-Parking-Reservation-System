package com.smartparking.dao;

import com.smartparking.config.JPAUtil;
import com.smartparking.entity.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public Reservation save(Reservation reservation) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(reservation);
            tx.commit();
            return reservation;
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
    public Optional<Reservation> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return Optional.ofNullable(em.find(Reservation.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<Reservation> findByCustomerId(Long customerId) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT r FROM Reservation r WHERE r.customerId = :customerId ORDER BY r.createdAt DESC",
                            Reservation.class
                    ).setParameter("customerId", customerId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean hasOverlappingReservation(
            Long parkingSlotId,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            Long count = em.createQuery(
                            "SELECT COUNT(r) FROM Reservation r " +
                                    "WHERE r.parkingSlotId = :slotId " +
                                    "AND r.status <> :cancelled " +
                                    "AND r.startTime < :endTime " +
                                    "AND r.endTime > :startTime",
                            Long.class
                    ).setParameter("slotId", parkingSlotId)
                    .setParameter("cancelled", Reservation.Status.CANCELLED)
                    .setParameter("startTime", startTime)
                    .setParameter("endTime", endTime)
                    .getSingleResult();

            return count > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public Reservation update(Reservation reservation) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Reservation updated = em.merge(reservation);
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


    @Override
    public List<Reservation> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT r FROM Reservation r ORDER BY r.createdAt DESC",
                    Reservation.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

}