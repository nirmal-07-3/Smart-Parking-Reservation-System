package com.smartparking.service;

import com.smartparking.dao.ParkingLocationDAO;
import com.smartparking.dao.ParkingLocationDAOImpl;
import com.smartparking.dao.ParkingSlotDAO;
import com.smartparking.dao.ParkingSlotDAOImpl;
import com.smartparking.entity.ParkingLocation;
import com.smartparking.entity.ParkingSlot;
import java.math.BigDecimal;
import java.util.List;

public class ParkingSlotService {

    private final ParkingLocationDAO parkingDAO;
    private final ParkingSlotDAO slotDAO;

    public ParkingSlotService() {
        this.parkingDAO = new ParkingLocationDAOImpl();
        this.slotDAO = new ParkingSlotDAOImpl();
    }

    public ParkingSlot createSlot(
            Long providerId, Long parkingLocationId,
            String slotNumber, String vehicleType, BigDecimal hourlyRate) {

        ParkingLocation parking = parkingDAO.findById(parkingLocationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Parking location not found.")
                );

        if (!parking.getProviderId().equals(providerId)) {
            throw new IllegalArgumentException(
                    "You cannot manage slots for another provider."
            );
        }

        if (parking.getStatus() != ParkingLocation.Status.ACTIVE) {
            throw new IllegalArgumentException(
                    "Slots can only be added to active parking locations."
            );
        }

        if (slotNumber == null || slotNumber.isBlank()
                || vehicleType == null || vehicleType.isBlank()
                || (hourlyRate != null
                && hourlyRate.compareTo(BigDecimal.ZERO) <= 0)) {
            throw new IllegalArgumentException("Invalid slot details.");
        }

        ParkingSlot slot = new ParkingSlot();
        slot.setParkingLocationId(parkingLocationId);
        slot.setSlotNumber(slotNumber.trim());
        slot.setVehicleType(vehicleType.trim());
        slot.setHourlyRate(hourlyRate);
        slot.setStatus(ParkingSlot.Status.AVAILABLE);

        return slotDAO.save(slot);
    }

    public List<ParkingSlot> getSlots(Long providerId, Long parkingLocationId) {
        ParkingLocation parking = parkingDAO.findById(parkingLocationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Parking location not found.")
                );

        if (!parking.getProviderId().equals(providerId)) {
            throw new IllegalArgumentException(
                    "You cannot view another provider's slots."
            );
        }

        return slotDAO.findByParkingLocationId(parkingLocationId);
    }

    public List<ParkingSlot> getAvailableSlots(Long parkingLocationId) {
        ParkingLocation parking = parkingDAO.findById(parkingLocationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Parking location not found.")
                );

        if (parking.getStatus() != ParkingLocation.Status.ACTIVE) {
            return List.of();
        }

        return slotDAO.findAvailableSlots(parkingLocationId);
    }
}