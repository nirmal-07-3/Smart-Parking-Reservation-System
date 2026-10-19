package com.smartparking.dao;

import com.smartparking.config.JPAUtil;
import com.smartparking.entity.Payment;
import com.smartparking.entity.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public Payment save(Payment payment) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(payment);
            tx.commit();
            return payment;
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
    public Optional<Payment> findByReservationId(Long reservationId) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT p FROM Payment p " +
                                    "WHERE p.reservationId = :reservationId",
                            Payment.class)
                    .setParameter("reservationId", reservationId)
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public Payment update(Payment payment) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Payment updated = em.merge(payment);
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
    public Payment saveOrUpdateWithReservation(
            Payment payment,
            Reservation reservation) {

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Payment managedPayment = payment.getId() == null
                    ? persistPayment(em, payment)
                    : em.merge(payment);

            if (reservation != null) {
                em.merge(reservation);
            }

            tx.commit();
            return managedPayment;

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    private Payment persistPayment(
            EntityManager em,
            Payment payment) {
        em.persist(payment);
        return payment;
    }

    @Override
    public List<Payment> findAll() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT p FROM Payment p ORDER BY p.createdAt DESC",
                            Payment.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}