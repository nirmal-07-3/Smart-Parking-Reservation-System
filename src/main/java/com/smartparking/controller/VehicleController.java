package com.smartparking.controller;

import com.smartparking.entity.Vehicle;
import com.smartparking.service.VehicleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/vehicles")
public class VehicleController extends HttpServlet {

    private final VehicleService vehicleService = new VehicleService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isCustomer(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long userId = (Long) session.getAttribute("userId");
        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            try {
                Long vehicleId = Long.valueOf(request.getParameter("vehicleId"));

                Vehicle vehicle = vehicleService.getVehicles(userId).stream()
                        .filter(v -> v.getId().equals(vehicleId))
                        .findFirst()
                        .orElse(null);

                if (vehicle == null) {
                    session.setAttribute("vehicleMessage",
                            "Vehicle was not found.");
                    response.sendRedirect(request.getContextPath() + "/vehicles");
                    return;
                }

                request.setAttribute("editVehicle", vehicle);
            } catch (NumberFormatException e) {
                session.setAttribute("vehicleMessage", "Invalid vehicle.");
                response.sendRedirect(request.getContextPath() + "/vehicles");
                return;
            }
        }

        List<Vehicle> vehicles = vehicleService.getVehicles(userId);
        request.setAttribute("vehicles", vehicles);
        request.getRequestDispatcher("/WEB-INF/views/vehicles.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (!isCustomer(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long userId = (Long) session.getAttribute("userId");
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                Long vehicleId = Long.valueOf(request.getParameter("vehicleId"));
                boolean deleted = vehicleService.deleteVehicle(vehicleId, userId);

                session.setAttribute("vehicleMessage",
                        deleted ? "Vehicle deleted successfully."
                                : "Vehicle was not found.");

            } else if ("update".equals(action)) {
                Long vehicleId = Long.valueOf(request.getParameter("vehicleId"));

                boolean updated = vehicleService.updateVehicle(
                        vehicleId,
                        userId,
                        request.getParameter("vehicleNumber"),
                        request.getParameter("vehicleType"),
                        request.getParameter("brand"),
                        request.getParameter("model"),
                        request.getParameter("color")
                );

                session.setAttribute("vehicleMessage",
                        updated ? "Vehicle updated successfully."
                                : "Vehicle was not found.");

            } else if (action == null || action.isBlank() || "add".equals(action)) {
                vehicleService.addVehicle(
                        userId,
                        request.getParameter("vehicleNumber"),
                        request.getParameter("vehicleType"),
                        request.getParameter("brand"),
                        request.getParameter("model"),
                        request.getParameter("color")
                );

                session.setAttribute("vehicleMessage",
                        "Vehicle added successfully.");
            } else {
                session.setAttribute("vehicleMessage",
                        "Invalid vehicle action.");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("vehicleMessage", "Invalid vehicle.");
        } catch (IllegalArgumentException e) {
            session.setAttribute("vehicleMessage", e.getMessage());
        } catch (RuntimeException e) {
            session.setAttribute("vehicleMessage",
                    "Unable to process the vehicle request. Please try again.");
        }

        response.sendRedirect(request.getContextPath() + "/vehicles");
    }

    private boolean isCustomer(HttpSession session) {
        return session != null
                && session.getAttribute("userId") instanceof Long
                && "CUSTOMER".equals(session.getAttribute("userRole"));
    }
}