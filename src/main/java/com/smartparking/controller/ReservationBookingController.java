
package com.smartparking.controller;

import com.smartparking.entity.ParkingLocation;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.entity.Reservation;
import com.smartparking.entity.Vehicle;
import com.smartparking.service.ParkingLocationService;
import com.smartparking.service.ParkingSlotService;
import com.smartparking.service.ReservationService;
import com.smartparking.service.VehicleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(urlPatterns = {"/reservation/search", "/reservation/create"})
public class ReservationBookingController extends HttpServlet {

    private final ParkingLocationService parkingLocationService =
            new ParkingLocationService();
    private final ParkingSlotService parkingSlotService =
            new ParkingSlotService();
    private final VehicleService vehicleService = new VehicleService();
    private final ReservationService reservationService =
            new ReservationService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isCustomer(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            loadPageData(request, (Long) session.getAttribute("userId"));

            request.getRequestDispatcher(
                    "/WEB-INF/views/reservation-search.jsp"
            ).forward(request, response);
        } catch (RuntimeException e) {
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load parking options."
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isCustomer(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long customerId = (Long) session.getAttribute("userId");

        try {
            Long parkingSlotId = parseId(
                    request.getParameter("parkingSlotId"),
                    "Select a parking slot."
            );

            Long vehicleId = parseId(
                    request.getParameter("vehicleId"),
                    "Select a vehicle."
            );

            LocalDateTime startTime = LocalDateTime.parse(
                    required(request.getParameter("startTime"), "Start time is required.")
            );

            LocalDateTime endTime = LocalDateTime.parse(
                    required(request.getParameter("endTime"), "End time is required.")
            );

            Reservation reservation = reservationService.createReservation(
                    customerId,
                    parkingSlotId,
                    vehicleId,
                    startTime,
                    endTime,
                    null
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/reservation/payment?reservationId="
                            + reservation.getId()
            );

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());

            try {
                loadPageData(request, customerId);
            } catch (RuntimeException loadError) {
                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Unable to reload parking options."
                );
                return;
            }

            request.getRequestDispatcher(
                    "/WEB-INF/views/reservation-search.jsp"
            ).forward(request, response);

        } catch (RuntimeException e) {
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to create the reservation."
            );
        }
    }

    private void loadPageData(HttpServletRequest request, Long customerId) {
        List<ParkingLocation> locations =
                parkingLocationService.searchActiveParkings();

        List<Vehicle> vehicles = vehicleService.getVehicles(customerId);

        request.setAttribute("locations", locations);
        request.setAttribute("vehicles", vehicles);

        String locationParameter = request.getParameter("parkingLocationId");

        if (locationParameter != null && !locationParameter.isBlank()) {
            Long locationId = parseId(
                    locationParameter, "Select a valid parking location."
            );

            ParkingLocation selected = locations.stream()
                    .filter(location -> locationId.equals(location.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "The selected parking location is unavailable."
                    ));

            request.setAttribute("selectedLocationId", selected.getId());
            request.setAttribute(
                    "slots",
                    parkingSlotService.getAvailableSlots(selected.getId())
            );
        } else {
            request.setAttribute("slots", List.of());
        }
    }

    private boolean isCustomer(HttpSession session) {
        return session != null
                && session.getAttribute("userId") instanceof Long
                && "CUSTOMER".equals(session.getAttribute("userRole"));
    }

    private Long parseId(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        try {
            long id = Long.parseLong(value);
            if (id <= 0) {
                throw new IllegalArgumentException(message);
            }
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(message);
        }
    }

    private String required(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
