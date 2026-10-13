package com.smartparking.service;

import com.smartparking.dao.VehicleDAO;
import com.smartparking.dao.VehicleDAOImpl;
import com.smartparking.entity.Vehicle;

import java.util.List;
import java.util.Set;

public class VehicleService {

    private final VehicleDAO vehicleDAO = new VehicleDAOImpl();

    private static final Set<String> VEHICLE_TYPES =
            Set.of("CAR", "BIKE", "SCOOTER", "SUV", "VAN", "OTHER");

    public Vehicle addVehicle(
            Long userId,
            String vehicleNumber,
            String vehicleType,
            String brand,
            String model,
            String color) {

        validateUser(userId);
        String normalizedNumber = validateVehicleNumber(vehicleNumber);
        String normalizedType = validateVehicleType(vehicleType);

        if (vehicleDAO.existsByVehicleNumber(normalizedNumber)) {
            throw new IllegalArgumentException(
                    "This vehicle number is already registered.");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setUserId(userId);
        vehicle.setVehicleNumber(normalizedNumber);
        vehicle.setVehicleType(normalizedType);
        vehicle.setBrand(cleanOptional(brand));
        vehicle.setModel(cleanOptional(model));
        vehicle.setColor(cleanOptional(color));

        return vehicleDAO.save(vehicle);
    }

    public List<Vehicle> getVehicles(Long userId) {
        validateUser(userId);
        return vehicleDAO.findByUserId(userId);
    }

    public boolean updateVehicle(
            Long vehicleId,
            Long userId,
            String vehicleNumber,
            String vehicleType,
            String brand,
            String model,
            String color) {

        if (vehicleId == null || vehicleId <= 0) {
            throw new IllegalArgumentException("Invalid vehicle.");
        }

        validateUser(userId);

        Vehicle vehicle = vehicleDAO.findById(vehicleId);

        if (vehicle == null || !userId.equals(vehicle.getUserId())) {
            return false;
        }

        String normalizedNumber = validateVehicleNumber(vehicleNumber);
        String normalizedType = validateVehicleType(vehicleType);

        if (!normalizedNumber.equalsIgnoreCase(vehicle.getVehicleNumber())
                && vehicleDAO.existsByVehicleNumber(normalizedNumber)) {
            throw new IllegalArgumentException(
                    "This vehicle number is already registered.");
        }

        vehicle.setVehicleNumber(normalizedNumber);
        vehicle.setVehicleType(normalizedType);
        vehicle.setBrand(cleanOptional(brand));
        vehicle.setModel(cleanOptional(model));
        vehicle.setColor(cleanOptional(color));

        vehicleDAO.update(vehicle);
        return true;
    }

    public boolean deleteVehicle(Long vehicleId, Long userId) {
        if (vehicleId == null || vehicleId <= 0) {
            throw new IllegalArgumentException("Invalid vehicle.");
        }

        validateUser(userId);
        return vehicleDAO.delete(vehicleId, userId);
    }

    private void validateUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid customer.");
        }
    }

    private String validateVehicleNumber(String vehicleNumber) {
        if (vehicleNumber == null || vehicleNumber.isBlank()) {
            throw new IllegalArgumentException("Vehicle number is required.");
        }

        String normalized = vehicleNumber.trim().toUpperCase();

        if (!normalized.matches("[A-Z0-9 -]{4,20}")) {
            throw new IllegalArgumentException("Enter a valid vehicle number.");
        }

        return normalized;
    }

    private String validateVehicleType(String vehicleType) {
        if (vehicleType == null
                || !VEHICLE_TYPES.contains(vehicleType.trim().toUpperCase())) {
            throw new IllegalArgumentException("Select a valid vehicle type.");
        }

        return vehicleType.trim().toUpperCase();
    }

    private String cleanOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}