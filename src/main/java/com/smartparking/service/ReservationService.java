
package com.smartparking.service;

import com.smartparking.dao.ReservationDAO;
import com.smartparking.dao.ReservationDAOImpl;
import com.smartparking.entity.Reservation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAOImpl();

    public Reservation createReservation(
            Long customerId,
            Long parkingSlotId,
            Long vehicleId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            BigDecimal hourlyRate) {

        validateBooking(customerId, parkingSlotId, vehicleId,
                startTime, endTime, hourlyRate);

        if (reservationDAO.hasOverlappingReservation(
                parkingSlotId, startTime, endTime)) {
            throw new IllegalArgumentException(
                    "This parking slot is already booked for the selected time.");
        }

        long minutes = Duration.between(startTime, endTime).toMinutes();
        long hours = (minutes + 59) / 60;

        BigDecimal totalAmount = hourlyRate
                .multiply(BigDecimal.valueOf(hours))
                .setScale(2, RoundingMode.HALF_UP);

        Reservation reservation = new Reservation();
        reservation.setCustomerId(customerId);
        reservation.setParkingSlotId(parkingSlotId);
        reservation.setVehicleId(vehicleId);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setTotalAmount(totalAmount);
        reservation.setStatus(Reservation.Status.PENDING);

        return reservationDAO.save(reservation);
    }

    public Reservation getReservation(Long reservationId) {
        if (reservationId == null || reservationId <= 0) {
            throw new IllegalArgumentException(
                    "A valid reservation ID is required.");
        }

        return reservationDAO.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reservation not found."));
    }

    public List<Reservation> getCustomerReservations(
            Long customerId) {

        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException(
                    "A valid customer ID is required.");
        }

        return reservationDAO.findByCustomerId(customerId);
    }


    public boolean cancelPendingReservation(Long reservationId, Long customerId) {
        if (reservationId == null || reservationId <= 0
                || customerId == null || customerId <= 0) {
            throw new IllegalArgumentException(
                    "Valid reservation and customer IDs are required.");
        }

        Reservation reservation = reservationDAO.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reservation not found."));

        if (!customerId.equals(reservation.getCustomerId())) {
            throw new IllegalArgumentException(
                    "You cannot cancel another customer's reservation.");
        }

        if (reservation.getStatus() != Reservation.Status.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending reservations can be cancelled using this operation.");
        }

        reservation.setStatus(Reservation.Status.CANCELLED);
        reservationDAO.update(reservation);

        return true;
    }


    private void validateBooking(
            Long customerId,
            Long parkingSlotId,
            Long vehicleId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            BigDecimal hourlyRate) {

        if (customerId == null || customerId <= 0
                || parkingSlotId == null || parkingSlotId <= 0
                || vehicleId == null || vehicleId <= 0) {
            throw new IllegalArgumentException(
                    "Customer, parking slot and vehicle are required.");
        }

        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException(
                    "Booking start and end times are required.");
        }

        if (!startTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Booking must start in the future.");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(
                    "End time must be after start time.");
        }

        if (hourlyRate == null
                || hourlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "A valid hourly parking rate is required.");
        }
    }
}
