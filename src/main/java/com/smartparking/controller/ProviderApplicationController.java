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

@WebServlet("/provider/applications")
public class ProviderApplicationController extends HttpServlet {

    private final ProviderApplicationService applicationService =
            new ProviderApplicationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (!isProvider(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long providerId = (Long) session.getAttribute("userId");
        request.setAttribute("applications",
                applicationService.getProviderApplications(providerId));

        request.getRequestDispatcher("/WEB-INF/views/provider-applications.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (!isProvider(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long providerId = (Long) session.getAttribute("userId");

            ProviderApplication application = applicationService.submitApplication(
                    providerId,
                    request.getParameter("businessName"),
                    request.getParameter("address"),
                    request.getParameter("description")
            );

            session.setAttribute("providerApplicationMessage",
                    "Application submitted successfully. Upload the required documents next.");
            session.setAttribute("lastProviderApplicationId", application.getId());

        } catch (IllegalArgumentException e) {
            session.setAttribute("providerApplicationMessage", e.getMessage());
        } catch (RuntimeException e) {
            session.setAttribute("providerApplicationMessage",
                    "Unable to submit the application. Please try again.");
        }

        response.sendRedirect(request.getContextPath() + "/provider/applications");
    }

    private boolean isProvider(HttpSession session) {
        return session != null
                && session.getAttribute("userId") instanceof Long
                && "PROVIDER".equals(session.getAttribute("userRole"));
    }
}