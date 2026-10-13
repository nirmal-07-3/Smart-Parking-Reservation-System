package com.smartparking.dao;

import com.smartparking.entity.ParkingSlot;
import java.util.List;
import java.util.Optional;

public interface ParkingSlotDAO {

    ParkingSlot save(ParkingSlot slot);

    Optional<ParkingSlot> findById(Long id);

    List<ParkingSlot> findByParkingLocationId(Long parkingLocationId);

    List<ParkingSlot> findAvailableSlots(Long parkingLocationId);

    ParkingSlot update(ParkingSlot slot);
}