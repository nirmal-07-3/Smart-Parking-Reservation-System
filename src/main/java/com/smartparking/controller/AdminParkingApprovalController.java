package com.smartparking.controller;

import com.smartparking.entity.ParkingLocation;
import com.smartparking.service.ParkingLocationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/parking-approvals")
public class AdminParkingApprovalController extends HttpServlet {

    private final ParkingLocationService parkingService =
            new ParkingLocationService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("parkings", parkingService.getPendingParkings());

        request.getRequestDispatcher(
                        "/WEB-INF/views/admin-parking-approvals.jsp")
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
            Long parkingId = Long.valueOf(
                    request.getParameter("parkingId"));

            ParkingLocation.Status status = ParkingLocation.Status.valueOf(
                    request.getParameter("status"));

            parkingService.reviewParkingLocation(parkingId, status);

            request.getSession().setAttribute(
                    "adminMessage", "Parking review saved successfully.");
        } catch (IllegalArgumentException | NullPointerException e) {
            request.getSession().setAttribute(
                    "adminMessage",
                    e.getMessage() == null
                            ? "Invalid parking review request."
                            : e.getMessage());
        } catch (RuntimeException e) {
            request.getSession().setAttribute(
                    "adminMessage", "Unable to save the parking review.");
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/parking-approvals");
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null
                && "ADMIN".equals(session.getAttribute("userRole"));
    }
}