
package com.smartparking.service;

import com.smartparking.config.JPAUtil;
import com.smartparking.dao.PaymentDAO;
import com.smartparking.dao.PaymentDAOImpl;
import com.smartparking.entity.Payment;
import com.smartparking.entity.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import java.util.UUID;

public class PaymentService {

    private final PaymentDAO paymentDAO = new PaymentDAOImpl();

    public Payment processPayment(
            Long reservationId,
            Long customerId,
            Payment.Method method,
            boolean simulateSuccess) {

        if (reservationId == null || reservationId <= 0
                || customerId == null || customerId <= 0) {
            throw new IllegalArgumentException(
                    "Valid reservation and customer IDs are required.");
        }

        if (method == null) {
            throw new IllegalArgumentException(
                    "Select a payment method.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Reservation reservation = em.find(
                    Reservation.class,
                    reservationId,
                    LockModeType.PESSIMISTIC_WRITE);

            if (reservation == null) {
                throw new IllegalArgumentException(
                        "Reservation not found.");
            }

            if (!customerId.equals(reservation.getCustomerId())) {
                throw new IllegalArgumentException(
                        "You are not authorized to pay for this reservation.");
            }

            if (reservation.getStatus() != Reservation.Status.PENDING) {
                throw new IllegalArgumentException(
                        "This reservation is not awaiting payment.");
            }

            Payment payment = em.createQuery(
                            "SELECT p FROM Payment p WHERE p.reservationId = :reservationId",
                            Payment.class)
                    .setParameter("reservationId", reservationId)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (payment != null
                    && payment.getStatus() == Payment.Status.SUCCESS) {
                throw new IllegalArgumentException(
                        "Payment has already succeeded for this reservation.");
            }

            if (payment != null
                    && payment.getStatus() == Payment.Status.REFUNDED) {
                throw new IllegalArgumentException(
                        "A refunded payment cannot be reused.");
            }

            if (payment == null) {
                payment = new Payment();
                payment.setReservationId(reservationId);
                payment.setAmount(reservation.getTotalAmount());
                payment.setMethod(method);
                payment.setStatus(Payment.Status.PENDING);
                em.persist(payment);
            } else {
                payment.setAmount(reservation.getTotalAmount());
                payment.setMethod(method);
            }

            if (simulateSuccess) {
                payment.setStatus(Payment.Status.SUCCESS);
                payment.setTransactionReference(
                        "SIM-" + UUID.randomUUID());

                reservation.setStatus(Reservation.Status.CONFIRMED);
            } else {
                payment.setStatus(Payment.Status.FAILED);
                payment.setTransactionReference(
                        "SIM-FAILED-" + UUID.randomUUID());
            }

            em.flush();
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

    public Payment getPayment(Long reservationId) {
        if (reservationId == null || reservationId <= 0) {
            throw new IllegalArgumentException(
                    "A valid reservation ID is required.");
        }

        return paymentDAO.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment record not found."));
    }
}
