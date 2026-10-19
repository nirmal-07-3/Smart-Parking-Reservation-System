package com.smartparking.controller;

import com.smartparking.entity.ParkingDocument;
import com.smartparking.service.ParkingDocumentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/document-review")
public class AdminDocumentReviewController extends HttpServlet {

    private final ParkingDocumentService documentService =
            new ParkingDocumentService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute(
                "documents", documentService.getPendingDocuments());

        request.getRequestDispatcher(
                        "/WEB-INF/views/admin-document-review.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        HttpSession session = request.getSession(false);

        try {
            Long documentId =
                    Long.valueOf(request.getParameter("documentId"));

            ParkingDocument.Status status =
                    ParkingDocument.Status.valueOf(
                            request.getParameter("status"));

            documentService.reviewDocument(
                    documentId,
                    status,
                    request.getParameter("reviewNote"));

            session.setAttribute(
                    "adminMessage", "Document review saved successfully.");
        } catch (IllegalArgumentException | NullPointerException e) {
            session.setAttribute("adminMessage",
                    e.getMessage() == null
                            ? "Invalid document review request."
                            : e.getMessage());
        } catch (RuntimeException e) {
            session.setAttribute(
                    "adminMessage", "Unable to save the document review.");
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/document-review");
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null
                && "ADMIN".equals(session.getAttribute("userRole"));
    }
}