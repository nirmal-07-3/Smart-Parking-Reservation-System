package com.smartparking.controller;

import com.smartparking.entity.ProviderApplication;
import com.smartparking.service.ProviderApplicationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/provider-applications")
public class AdminProviderApplicationController extends HttpServlet {

    private final ProviderApplicationService applicationService =
            new ProviderApplicationService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("applications",
                applicationService.getPendingApplications());

        request.getRequestDispatcher(
                        "/WEB-INF/views/admin-provider-applications.jsp")
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

        try {
            Long applicationId = Long.valueOf(
                    request.getParameter("applicationId"));

            ProviderApplication.Status status =
                    ProviderApplication.Status.valueOf(
                            request.getParameter("status"));

            if (status != ProviderApplication.Status.APPROVED
                    && status != ProviderApplication.Status.REJECTED) {
                throw new IllegalArgumentException(
                        "Choose approve or reject.");
            }

            applicationService.reviewApplication(
                    applicationId, status, request.getParameter("reviewNote"));

            setMessage(request.getSession(),
                    "Provider application review saved successfully.");
        } catch (IllegalArgumentException | NullPointerException e) {
            setMessage(request.getSession(),
                    e.getMessage() == null
                            ? "Invalid application review request."
                            : e.getMessage());
        } catch (RuntimeException e) {
            setMessage(request.getSession(),
                    "Unable to save the application review.");
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/provider-applications");
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null
                && "ADMIN".equals(session.getAttribute("userRole"));
    }

    private void setMessage(HttpSession session, String message) {
        session.setAttribute("adminMessage", message);
    }
}