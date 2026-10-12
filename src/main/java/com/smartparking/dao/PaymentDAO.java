package com.smartparking.dao;

import com.smartparking.entity.Payment;
import java.util.Optional;

public interface PaymentDAO {

    Payment save(Payment payment);

    Optional<Payment> findByReservationId(Long reservationId);

    Payment update(Payment payment);
}