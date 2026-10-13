
package com.smartparking.service;

import com.smartparking.dao.PaymentDAO;
import com.smartparking.dao.PaymentDAOImpl;
import com.smartparking.dao.ReservationDAO;
import com.smartparking.dao.ReservationDAOImpl;
import com.smartparking.entity.Payment;
import com.smartparking.entity.Reservation;

import java.util.UUID;

public class PaymentService {

    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();

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

        Reservation reservation = reservationDAO.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reservation not found."));

        if (!customerId.equals(reservation.getCustomerId())) {
            throw new IllegalArgumentException(
                    "You are not authorized to pay for this reservation.");
        }

        if (reservation.getStatus() != Reservation.Status.PENDING) {
            throw new IllegalArgumentException(
                    "This reservation is not awaiting payment.");
        }

        Payment payment = paymentDAO.findByReservationId(reservationId)
                .orElse(null);

        if (payment != null && payment.getStatus() == Payment.Status.SUCCESS) {
            throw new IllegalArgumentException(
                    "Payment has already succeeded for this reservation.");
        }

        if (payment != null && payment.getStatus() == Payment.Status.REFUNDED) {
            throw new IllegalArgumentException(
                    "A refunded payment cannot be reused.");
        }

        if (payment == null) {
            payment = new Payment();
            payment.setReservationId(reservationId);
            payment.setAmount(reservation.getTotalAmount());
            payment.setMethod(method);
            payment.setStatus(Payment.Status.PENDING);
            payment = paymentDAO.save(payment);
        } else {
            payment.setAmount(reservation.getTotalAmount());
            payment.setMethod(method);
        }

        if (simulateSuccess) {
            payment.setStatus(Payment.Status.SUCCESS);
            payment.setTransactionReference(
                    "SIM-" + UUID.randomUUID());

            reservation.setStatus(Reservation.Status.CONFIRMED);
            reservationDAO.update(reservation);
        } else {
            payment.setStatus(Payment.Status.FAILED);
            payment.setTransactionReference(
                    "SIM-FAILED-" + UUID.randomUUID());
        }

        return paymentDAO.update(payment);
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
