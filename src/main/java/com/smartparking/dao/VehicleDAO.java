package com.smartparking.dao;

import com.smartparking.entity.Vehicle;
import java.util.List;

public interface VehicleDAO {
    Vehicle save(Vehicle vehicle);
    Vehicle findById(Long id);
    List<Vehicle> findByUserId(Long userId);
    boolean existsByVehicleNumber(String vehicleNumber);
    void update(Vehicle vehicle);
    boolean delete(Long id, Long userId);
}