package com.smartparking.service;

import com.smartparking.dao.ProviderApplicationDAO;
import com.smartparking.dao.ProviderApplicationDAOImpl;
import com.smartparking.dao.ParkingDocumentDAO;
import com.smartparking.dao.ParkingDocumentDAOImpl;
import com.smartparking.entity.ProviderApplication;
import com.smartparking.entity.ParkingDocument;
import java.util.List;

public class ProviderApplicationService {

    private final ProviderApplicationDAO applicationDAO;
    private final ParkingDocumentDAO documentDAO;

    public ProviderApplicationService() {
        this.applicationDAO = new ProviderApplicationDAOImpl();
        this.documentDAO = new ParkingDocumentDAOImpl();
    }

    public ProviderApplication submitApplication(
            Long providerId, String businessName, String address,
            String description) {

        if (providerId == null || providerId <= 0
                || businessName == null || businessName.isBlank()
                || address == null || address.isBlank()) {
            throw new IllegalArgumentException(
                    "Provider, business name and address are required."
            );
        }

        List<ProviderApplication> existing =
                applicationDAO.findByProviderId(providerId);

        boolean pendingExists = existing.stream().anyMatch(
                application -> application.getStatus()
                        == ProviderApplication.Status.PENDING
        );

        if (pendingExists) {
            throw new IllegalArgumentException(
                    "You already have a pending provider application."
            );
        }

        ProviderApplication application = new ProviderApplication();
        application.setProviderId(providerId);
        application.setBusinessName(businessName.trim());
        application.setAddress(address.trim());
        application.setDescription(
                description == null ? "" : description.trim()
        );
        application.setStatus(ProviderApplication.Status.PENDING);

        return applicationDAO.save(application);
    }

    public List<ProviderApplication> getProviderApplications(Long providerId) {
        if (providerId == null) {
            throw new IllegalArgumentException("Provider is required.");
        }

        return applicationDAO.findByProviderId(providerId);
    }

    public List<ProviderApplication> getPendingApplications() {
        return applicationDAO.findByStatus(
                ProviderApplication.Status.PENDING
        );
    }

    public ProviderApplication reviewApplication(
            Long applicationId, ProviderApplication.Status status,
            String reviewNote) {

        if (applicationId == null || status == null
                || status == ProviderApplication.Status.PENDING) {
            throw new IllegalArgumentException("Invalid application review.");
        }

        ProviderApplication application = applicationDAO.findById(applicationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Application not found.")
                );

        if (application.getStatus() != ProviderApplication.Status.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending applications can be reviewed."
            );
        }

        List<ParkingDocument> documents =
                documentDAO.findByApplicationId(applicationId);

        if (status == ProviderApplication.Status.APPROVED
                && (documents.isEmpty()
                || documents.stream().anyMatch(
                document -> document.getStatus()
                        != ParkingDocument.Status.APPROVED))) {
            throw new IllegalArgumentException(
                    "All submitted documents must be approved first."
            );
        }

        application.setStatus(status);
        application.setReviewedAt(java.time.LocalDateTime.now());
        application.setReviewNote(
                reviewNote == null ? "" : reviewNote.trim()
        );

        return applicationDAO.update(application);
    }
}