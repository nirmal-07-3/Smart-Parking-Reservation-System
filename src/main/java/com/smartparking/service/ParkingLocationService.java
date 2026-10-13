package com.smartparking.service;

import com.smartparking.dao.ParkingLocationDAO;
import com.smartparking.dao.ParkingLocationDAOImpl;
import com.smartparking.entity.ParkingLocation;
import java.math.BigDecimal;
import java.util.List;

public class ParkingLocationService {

    private final ParkingLocationDAO parkingDAO;

    public ParkingLocationService() {
        this.parkingDAO = new ParkingLocationDAOImpl();
    }

    public ParkingLocation createParking(
            Long providerId, String name, String address,
            String description, BigDecimal hourlyRate) {

        if (providerId == null || providerId <= 0
                || name == null || name.isBlank()
                || address == null || address.isBlank()
                || hourlyRate == null
                || hourlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Valid provider, name, address and hourly rate are required."
            );
        }

        ParkingLocation parking = new ParkingLocation();
        parking.setProviderId(providerId);
        parking.setName(name.trim());
        parking.setAddress(address.trim());
        parking.setDescription(
                description == null ? "" : description.trim()
        );
        parking.setHourlyRate(hourlyRate);
        parking.setStatus(ParkingLocation.Status.PENDING);

        return parkingDAO.save(parking);
    }

    public List<ParkingLocation> getProviderParkings(Long providerId) {
        if (providerId == null) {
            throw new IllegalArgumentException("Provider is required.");
        }

        return parkingDAO.findByProviderId(providerId);
    }

    public List<ParkingLocation> searchActiveParkings() {
        return parkingDAO.findActiveLocations();
    }

    public ParkingLocation updateParking(
            Long providerId, Long parkingId, String name,
            String address, String description, BigDecimal hourlyRate) {

        ParkingLocation parking = parkingDAO.findById(parkingId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Parking location not found.")
                );

        if (!parking.getProviderId().equals(providerId)) {
            throw new IllegalArgumentException(
                    "You cannot modify another provider's parking."
            );
        }

        if (parking.getStatus() == ParkingLocation.Status.SUSPENDED
                || parking.getStatus() == ParkingLocation.Status.REJECTED) {
            throw new IllegalArgumentException(
                    "This parking listing cannot be edited in its current state."
            );
        }

        if (name == null || name.isBlank()
                || address == null || address.isBlank()
                || hourlyRate == null
                || hourlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid parking details.");
        }

        parking.setName(name.trim());
        parking.setAddress(address.trim());
        parking.setDescription(
                description == null ? "" : description.trim()
        );
        parking.setHourlyRate(hourlyRate);

        return parkingDAO.update(parking);
    }
}