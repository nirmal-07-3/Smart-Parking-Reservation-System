package com.smartparking.service;

import com.smartparking.dao.ParkingDocumentDAO;
import com.smartparking.dao.ParkingDocumentDAOImpl;
import com.smartparking.entity.ParkingDocument;
import java.util.List;

public class ParkingDocumentService {

    private final ParkingDocumentDAO documentDAO;

    public ParkingDocumentService() {
        this.documentDAO = new ParkingDocumentDAOImpl();
    }

    public List<ParkingDocument> getPendingDocuments() {
        return documentDAO.findByStatus(ParkingDocument.Status.PENDING);
    }

    public ParkingDocument reviewDocument(
            Long documentId, ParkingDocument.Status status, String reviewNote) {

        if (documentId == null || documentId <= 0 || status == null
                || status == ParkingDocument.Status.PENDING) {
            throw new IllegalArgumentException("Invalid document review.");
        }

        ParkingDocument document = documentDAO.findById(documentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Document not found."));

        if (document.getStatus() != ParkingDocument.Status.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending documents can be reviewed.");
        }

        if (status == ParkingDocument.Status.REJECTED
                && (reviewNote == null || reviewNote.isBlank())) {
            throw new IllegalArgumentException(
                    "Provide a reason when rejecting a document.");
        }

        document.setStatus(status);
        document.setReviewNote(reviewNote == null ? "" : reviewNote.trim());

        return documentDAO.update(document);
    }
}