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
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/provider/parkings")
public class ParkingLocationController extends HttpServlet {

    private final ParkingLocationService parkingService = new ParkingLocationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isProvider(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long providerId = (Long) session.getAttribute("userId");
        List<ParkingLocation> parkings =
                parkingService.getProviderParkings(providerId);

        request.setAttribute("parkings", parkings);

        String editId = request.getParameter("edit");

        if (editId != null && !editId.isBlank()) {
            try {
                Long id = Long.valueOf(editId);

                ParkingLocation editable = parkings.stream()
                        .filter(parking -> parking.getId().equals(id))
                        .findFirst()
                        .orElse(null);

                if (editable == null) {
                    session.setAttribute("parkingMessage",
                            "Parking location was not found.");
                    response.sendRedirect(request.getContextPath() + "/provider/parkings");
                    return;
                }

                request.setAttribute("editParking", editable);

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/provider-parkings.jsp")
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

        Long providerId = (Long) session.getAttribute("userId");
        String action = request.getParameter("action");

        try {
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String description = request.getParameter("description");
            BigDecimal hourlyRate = new BigDecimal(request.getParameter("hourlyRate"));

            if ("update".equals(action)) {
                Long parkingId = Long.valueOf(request.getParameter("parkingId"));

                parkingService.updateParking(
                        providerId, parkingId, name, address, description, hourlyRate
                );

                session.setAttribute("parkingMessage",
                        "Parking location updated successfully.");

            } else if (action == null || action.isBlank() || "create".equals(action)) {
                parkingService.createParking(
                        providerId, name, address, description, hourlyRate
                );

                session.setAttribute("parkingMessage",
                        "Parking location submitted. It will remain pending until approved.");

            } else {
                throw new IllegalArgumentException("Invalid parking action.");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("parkingMessage",
                    "Enter a valid parking ID and hourly rate.");
        } catch (IllegalArgumentException e) {
            session.setAttribute("parkingMessage", e.getMessage());
        } catch (RuntimeException e) {
            session.setAttribute("parkingMessage",
                    "Unable to save the parking location. Please try again.");
        }

        response.sendRedirect(request.getContextPath() + "/provider/parkings");
    }

    private boolean isProvider(HttpSession session) {
        return session != null
                && session.getAttribute("userId") instanceof Long
                && "PROVIDER".equals(session.getAttribute("userRole"));
    }
}