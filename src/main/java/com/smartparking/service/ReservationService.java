
package com.smartparking.service;

import com.smartparking.config.JPAUtil;
import com.smartparking.dao.ReservationDAO;
import com.smartparking.dao.ReservationDAOImpl;
import com.smartparking.entity.ParkingLocation;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.entity.Reservation;
import com.smartparking.entity.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
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

        validateBooking(
                customerId, parkingSlotId, vehicleId, startTime, endTime);

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            ParkingSlot slot = em.find(
                    ParkingSlot.class,
                    parkingSlotId,
                    LockModeType.PESSIMISTIC_WRITE);

            if (slot == null) {
                throw new IllegalArgumentException(
                        "Parking slot not found.");
            }

            if (slot.getStatus() != ParkingSlot.Status.AVAILABLE) {
                throw new IllegalArgumentException(
                        "This parking slot is not available.");
            }

            ParkingLocation parking = em.find(
                    ParkingLocation.class,
                    slot.getParkingLocationId());

            if (parking == null
                    || parking.getStatus() != ParkingLocation.Status.ACTIVE) {
                throw new IllegalArgumentException(
                        "This parking location is not active.");
            }

            Vehicle vehicle = em.find(Vehicle.class, vehicleId);

            if (vehicle == null
                    || !customerId.equals(vehicle.getUserId())) {
                throw new IllegalArgumentException(
                        "Select a vehicle registered to your account.");
            }

            if (slot.getVehicleType() == null
                    || vehicle.getVehicleType() == null
                    || !slot.getVehicleType().trim()
                    .equalsIgnoreCase(vehicle.getVehicleType().trim())) {
                throw new IllegalArgumentException(
                        "The selected slot does not support this vehicle type.");
            }

            Long overlappingCount = em.createQuery(
                            "SELECT COUNT(r) FROM Reservation r "
                                    + "WHERE r.parkingSlotId = :slotId "
                                    + "AND r.status <> :cancelled "
                                    + "AND r.startTime < :endTime "
                                    + "AND r.endTime > :startTime",
                            Long.class)
                    .setParameter("slotId", parkingSlotId)
                    .setParameter(
                            "cancelled", Reservation.Status.CANCELLED)
                    .setParameter("startTime", startTime)
                    .setParameter("endTime", endTime)
                    .getSingleResult();

            if (overlappingCount > 0) {
                throw new IllegalArgumentException(
                        "This parking slot is already booked for the selected time.");
            }

            BigDecimal effectiveRate = slot.getHourlyRate() != null
                    ? slot.getHourlyRate()
                    : parking.getHourlyRate();

            if (effectiveRate == null
                    || effectiveRate.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "A valid parking rate is not configured.");
            }

            long seconds = Duration.between(
                    startTime, endTime).getSeconds();

            long hours = (seconds + 3599) / 3600;

            BigDecimal totalAmount = effectiveRate
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

            em.persist(reservation);
            em.flush();
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

    public Reservation getReservation(Long reservationId) {
        if (reservationId == null || reservationId <= 0) {
            throw new IllegalArgumentException(
                    "A valid reservation ID is required.");
        }

        return reservationDAO.findById(reservationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Reservation not found."));
    }

    public List<Reservation> getCustomerReservations(Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException(
                    "A valid customer ID is required.");
        }

        return reservationDAO.findByCustomerId(customerId);
    }

    public boolean cancelPendingReservation(
            Long reservationId, Long customerId) {

        if (reservationId == null || reservationId <= 0
                || customerId == null || customerId <= 0) {
            throw new IllegalArgumentException(
                    "Valid reservation and customer IDs are required.");
        }

        Reservation reservation = reservationDAO.findById(reservationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Reservation not found."));

        if (!customerId.equals(reservation.getCustomerId())) {
            throw new IllegalArgumentException(
                    "You cannot cancel another customer's reservation.");
        }

        if (reservation.getStatus() != Reservation.Status.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending reservations can be cancelled.");
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
            LocalDateTime endTime) {

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
    }
}
