package com.smartparking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "parking_slots",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_parking_slot_number",
                columnNames = {"parking_location_id", "slot_number"}
        )
)
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parking_location_id", nullable = false)
    private Long parkingLocationId;

    @Column(name = "slot_number", nullable = false, length = 30)
    private String slotNumber;

    @Column(name = "vehicle_type", nullable = false, length = 30)
    private String vehicleType;

    @Column(name = "hourly_rate", precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    public enum Status {
        AVAILABLE,
        RESERVED,
        OCCUPIED,
        MAINTENANCE
    }

    public ParkingSlot() {
    }

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = Status.AVAILABLE;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getParkingLocationId() { return parkingLocationId; }
    public void setParkingLocationId(Long parkingLocationId) { this.parkingLocationId = parkingLocationId; }
    public String getSlotNumber() { return slotNumber; }
    public void setSlotNumber(String slotNumber) { this.slotNumber = slotNumber; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}