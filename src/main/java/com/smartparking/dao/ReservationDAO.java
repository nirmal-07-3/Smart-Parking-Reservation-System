package com.smartparking.dao;

import com.smartparking.entity.Reservation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationDAO {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    List<Reservation> findByCustomerId(Long customerId);


    List<Reservation> findAll();


    boolean hasOverlappingReservation(
            Long parkingSlotId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    Reservation update(Reservation reservation);
}