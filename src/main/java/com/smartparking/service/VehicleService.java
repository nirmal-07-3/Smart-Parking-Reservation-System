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

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid customer.");
        }

        if (vehicleNumber == null || vehicleNumber.isBlank()) {
            throw new IllegalArgumentException("Vehicle number is required.");
        }

        if (vehicleType == null || !VEHICLE_TYPES.contains(vehicleType.trim().toUpperCase())) {
            throw new IllegalArgumentException("Select a valid vehicle type.");
        }

        String normalizedNumber = vehicleNumber.trim().toUpperCase();

        if (!normalizedNumber.matches("[A-Z0-9 -]{4,20}")) {
            throw new IllegalArgumentException("Enter a valid vehicle number.");
        }

        if (vehicleDAO.existsByVehicleNumber(normalizedNumber)) {
            throw new IllegalArgumentException("This vehicle number is already registered.");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setUserId(userId);
        vehicle.setVehicleNumber(normalizedNumber);
        vehicle.setVehicleType(vehicleType.trim().toUpperCase());
        vehicle.setBrand(cleanOptional(brand));
        vehicle.setModel(cleanOptional(model));
        vehicle.setColor(cleanOptional(color));

        return vehicleDAO.save(vehicle);
    }

    public List<Vehicle> getVehicles(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Customer session is required.");
        }

        return vehicleDAO.findByUserId(userId);
    }

    public boolean deleteVehicle(Long vehicleId, Long userId) {
        if (vehicleId == null || userId == null) {
            throw new IllegalArgumentException("Invalid vehicle request.");
        }

        return vehicleDAO.delete(vehicleId, userId);
    }

    private String cleanOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}