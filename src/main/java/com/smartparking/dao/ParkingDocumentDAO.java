package com.smartparking.dao;

import com.smartparking.entity.ParkingDocument;
import java.util.List;
import java.util.Optional;

public interface ParkingDocumentDAO {

    ParkingDocument save(ParkingDocument document);

    Optional<ParkingDocument> findById(Long id);

    List<ParkingDocument> findByApplicationId(Long applicationId);

    List<ParkingDocument> findByStatus(ParkingDocument.Status status);

    ParkingDocument update(ParkingDocument document);
}