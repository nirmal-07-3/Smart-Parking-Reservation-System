package com.smartparking.dao;

import com.smartparking.entity.ParkingLocation;
import java.util.List;
import java.util.Optional;

public interface ParkingLocationDAO {

    ParkingLocation save(ParkingLocation parkingLocation);

    Optional<ParkingLocation> findById(Long id);

    List<ParkingLocation> findByProviderId(Long providerId);

    List<ParkingLocation> findActiveLocations();

    ParkingLocation update(ParkingLocation parkingLocation);
}