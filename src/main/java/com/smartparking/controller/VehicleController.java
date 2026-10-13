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

        if (session == null || session.getAttribute("userId") == null
                || !"CUSTOMER".equals(session.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long userId = (Long) session.getAttribute("userId");
        List<Vehicle> vehicles = vehicleService.getVehicles(userId);

        request.setAttribute("vehicles", vehicles);
        request.getRequestDispatcher("/WEB-INF/views/vehicles.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null
                || !"CUSTOMER".equals(session.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long userId = (Long) session.getAttribute("userId");
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                Long vehicleId = Long.valueOf(request.getParameter("vehicleId"));
                boolean deleted = vehicleService.deleteVehicle(vehicleId, userId);

                if (!deleted) {
                    session.setAttribute("vehicleMessage", "Vehicle was not found.");
                } else {
                    session.setAttribute("vehicleMessage", "Vehicle deleted successfully.");
                }
            } else {
                vehicleService.addVehicle(
                        userId,
                        request.getParameter("vehicleNumber"),
                        request.getParameter("vehicleType"),
                        request.getParameter("brand"),
                        request.getParameter("model"),
                        request.getParameter("color")
                );

                session.setAttribute("vehicleMessage", "Vehicle added successfully.");
            }
        } catch (IllegalArgumentException e) {
            session.setAttribute("vehicleMessage", e.getMessage());
        } catch (RuntimeException e) {
            session.setAttribute("vehicleMessage",
                    "Unable to process the vehicle request. Check the details and try again.");
        }

        response.sendRedirect(request.getContextPath() + "/vehicles");
    }
}