package com.smartparking.controller;

import com.smartparking.dao.ParkingDocumentDAO;
import com.smartparking.dao.ParkingDocumentDAOImpl;
import com.smartparking.dao.ProviderApplicationDAO;
import com.smartparking.dao.ProviderApplicationDAOImpl;
import com.smartparking.entity.ParkingDocument;
import com.smartparking.entity.ProviderApplication;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@WebServlet("/provider/documents")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class ProviderDocumentController extends HttpServlet {

    private final ParkingDocumentDAO documentDAO = new ParkingDocumentDAOImpl();
    private final ProviderApplicationDAO applicationDAO = new ProviderApplicationDAOImpl();

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("pdf", "jpg", "jpeg", "png");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isSignedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if ("download".equals(request.getParameter("action"))) {
            downloadDocument(request, response, session);
            return;
        }

        if (!isProvider(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long applicationId = Long.valueOf(request.getParameter("applicationId"));
            ProviderApplication application = getOwnedApplication(
                    applicationId, (Long) session.getAttribute("userId"));

            if (application == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("application", application);
            request.setAttribute("documents",
                    documentDAO.findByApplicationId(applicationId));

            request.getRequestDispatcher("/WEB-INF/views/provider-documents.jsp")
                    .forward(request, response);

        } catch (NumberFormatException | NullPointerException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isProvider(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long providerId = (Long) session.getAttribute("userId");
        String redirect = request.getContextPath() + "/provider/applications";

        try {
            Long applicationId = Long.valueOf(request.getParameter("applicationId"));
            ProviderApplication application =
                    getOwnedApplication(applicationId, providerId);

            if (application == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (application.getStatus() != ProviderApplication.Status.PENDING) {
                session.setAttribute("providerApplicationMessage",
                        "Documents can only be uploaded for a pending application.");
                response.sendRedirect(redirect);
                return;
            }

            String documentType = request.getParameter("documentType");

            if (documentType == null || documentType.isBlank()
                    || documentType.trim().length() > 100) {
                throw new IllegalArgumentException("Select a valid document type.");
            }

            Part filePart = request.getPart("documentFile");

            if (filePart == null || filePart.getSize() == 0) {
                throw new IllegalArgumentException("Choose a document to upload.");
            }

            String submittedName = filePart.getSubmittedFileName();

            if (submittedName == null || submittedName.isBlank()) {
                throw new IllegalArgumentException("The uploaded filename is invalid.");
            }

            String originalName = Paths.get(submittedName).getFileName().toString();
            int dot = originalName.lastIndexOf('.');

            if (dot < 1 || dot == originalName.length() - 1) {
                throw new IllegalArgumentException("Upload a PDF, JPG, JPEG, or PNG file.");
            }

            String extension = originalName.substring(dot + 1).toLowerCase();

            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new IllegalArgumentException("Only PDF, JPG, JPEG, and PNG files are allowed.");
            }

            String contentType = filePart.getContentType();

            if (contentType == null ||
                    !(contentType.equalsIgnoreCase("application/pdf")
                            || contentType.equalsIgnoreCase("image/jpeg")
                            || contentType.equalsIgnoreCase("image/png"))) {
                throw new IllegalArgumentException("The uploaded file type is not supported.");
            }

            Path uploadDirectory = getUploadDirectory();
            Files.createDirectories(uploadDirectory);

            String storedName = UUID.randomUUID() + "." + extension;
            Path destination = uploadDirectory.resolve(storedName).normalize();

            if (!destination.getParent().equals(uploadDirectory.toAbsolutePath().normalize())) {
                throw new IllegalArgumentException("Invalid upload destination.");
            }

            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, destination);
            }

            try {
                ParkingDocument document = new ParkingDocument();
                document.setApplicationId(applicationId);
                document.setDocumentType(documentType.trim());
                document.setFilePath(storedName);
                document.setStatus(ParkingDocument.Status.PENDING);
                documentDAO.save(document);
            } catch (RuntimeException e) {
                Files.deleteIfExists(destination);
                throw e;
            }

            session.setAttribute("providerApplicationMessage",
                    "Document uploaded successfully.");

        } catch (IllegalArgumentException e) {
            session.setAttribute("providerApplicationMessage", e.getMessage());
        } catch (IllegalStateException e) {
            session.setAttribute("providerApplicationMessage",
                    "The upload is too large. Maximum file size is 5 MB.");
        } catch (RuntimeException e) {
            session.setAttribute("providerApplicationMessage",
                    "Unable to upload the document. Please try again.");
        }

        response.sendRedirect(redirect);
    }

    private void downloadDocument(HttpServletRequest request,
                                  HttpServletResponse response,
                                  HttpSession session)
            throws IOException {

        try {
            Long documentId = Long.valueOf(request.getParameter("id"));

            ParkingDocument document = documentDAO.findById(documentId).orElse(null);

            if (document == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ProviderApplication application =
                    applicationDAO.findById(document.getApplicationId()).orElse(null);

            if (application == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            boolean owner = isProvider(session)
                    && application.getProviderId().equals(session.getAttribute("userId"));
            boolean admin = "ADMIN".equals(session.getAttribute("userRole"));

            if (!owner && !admin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            Path file = getUploadDirectory()
                    .resolve(document.getFilePath())
                    .normalize();

            if (!file.getParent().equals(getUploadDirectory().toAbsolutePath().normalize())
                    || !Files.isRegularFile(file)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String contentType = Files.probeContentType(file);
            response.setContentType(contentType == null
                    ? "application/octet-stream" : contentType);
            response.setHeader("X-Content-Type-Options", "nosniff");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + file.getFileName() + "\"");
            response.setContentLengthLong(Files.size(file));

            try (InputStream input = Files.newInputStream(file);
                 OutputStream output = response.getOutputStream()) {
                input.transferTo(output);
            }

        } catch (NumberFormatException | NullPointerException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private ProviderApplication getOwnedApplication(Long applicationId, Long providerId) {
        ProviderApplication application =
                applicationDAO.findById(applicationId).orElse(null);

        if (application == null || !application.getProviderId().equals(providerId)) {
            return null;
        }

        return application;
    }

    private Path getUploadDirectory() {
        return Paths.get(System.getProperty("catalina.base",
                        System.getProperty("user.home")), "smartparking-uploads")
                .toAbsolutePath().normalize();
    }

    private boolean isSignedIn(HttpSession session) {
        return session != null && session.getAttribute("userId") instanceof Long;
    }

    private boolean isProvider(HttpSession session) {
        return isSignedIn(session)
                && "PROVIDER".equals(session.getAttribute("userRole"));
    }
}