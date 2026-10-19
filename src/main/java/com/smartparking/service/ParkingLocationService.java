
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
            Long providerId,
            String name,
            String address,
            String description,
            BigDecimal hourlyRate) {

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
        if (providerId == null || providerId <= 0) {
            throw new IllegalArgumentException("A valid provider is required.");
        }

        return parkingDAO.findByProviderId(providerId);
    }

    public List<ParkingLocation> searchActiveParkings() {
        return parkingDAO.findActiveLocations();
    }

    public List<ParkingLocation> getPendingParkings() {
        return parkingDAO.findByStatus(ParkingLocation.Status.PENDING);
    }

    public ParkingLocation reviewParkingLocation(
            Long parkingId,
            ParkingLocation.Status decision) {

        if (parkingId == null || parkingId <= 0) {
            throw new IllegalArgumentException(
                    "A valid parking location is required."
            );
        }

        if (decision != ParkingLocation.Status.ACTIVE
                && decision != ParkingLocation.Status.REJECTED) {
            throw new IllegalArgumentException(
                    "The review decision must be ACTIVE or REJECTED."
            );
        }

        ParkingLocation parking = parkingDAO.findById(parkingId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Parking location not found."
                ));

        if (parking.getStatus() != ParkingLocation.Status.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending parking locations can be reviewed."
            );
        }

        parking.setStatus(decision);
        return parkingDAO.update(parking);
    }

    public ParkingLocation updateParking(
            Long providerId,
            Long parkingId,
            String name,
            String address,
            String description,
            BigDecimal hourlyRate) {

        if (providerId == null || providerId <= 0
                || parkingId == null || parkingId <= 0) {
            throw new IllegalArgumentException(
                    "Valid provider and parking location are required."
            );
        }

        ParkingLocation parking = parkingDAO.findById(parkingId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Parking location not found."
                ));

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
