package com.smartparking.controller;

import com.smartparking.entity.ParkingLocation;
import com.smartparking.entity.ParkingSlot;
import com.smartparking.service.ParkingLocationService;
import com.smartparking.service.ParkingSlotService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@WebServlet("/provider/slots")
public class ParkingSlotController extends HttpServlet {

    private final ParkingLocationService parkingService =
            new ParkingLocationService();
    private final ParkingSlotService slotService =
            new ParkingSlotService();

    private static final Set<String> VEHICLE_TYPES =
            Set.of("CAR", "BIKE", "SCOOTER", "SUV", "VAN", "OTHER");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (!isProvider(session)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            Long providerId = (Long) session.getAttribute("userId");
            Long locationId = Long.valueOf(
                    request.getParameter("parkingLocationId"));

            List<ParkingLocation> providerParkings =
                    parkingService.getProviderParkings(providerId);

            ParkingLocation location = providerParkings.stream()
                    .filter(parking -> parking.getId().equals(locationId))
                    .findFirst()
                    .orElse(null);

            if (location == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute("parking", location);
            request.setAttribute("slots",
                    slotService.getSlots(providerId, locationId));

            request.getRequestDispatcher("/WEB-INF/views/provider-slots.jsp")
                    .forward(request, response);

        } catch (NumberFormatException | NullPointerException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
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
        String locationValue = request.getParameter("parkingLocationId");
        String redirect = request.getContextPath() + "/provider/parkings";

        try {
            Long locationId = Long.valueOf(locationValue);
            String slotNumber = request.getParameter("slotNumber");
            String vehicleType = request.getParameter("vehicleType");

            if (vehicleType == null
                    || !VEHICLE_TYPES.contains(vehicleType.trim().toUpperCase())) {
                throw new IllegalArgumentException("Select a valid vehicle type.");
            }

            String rateValue = request.getParameter("hourlyRate");
            BigDecimal hourlyRate = null;

            if (rateValue != null && !rateValue.isBlank()) {
                hourlyRate = new BigDecimal(rateValue);
            }

            slotService.createSlot(
                    providerId,
                    locationId,
                    slotNumber,
                    vehicleType.trim().toUpperCase(),
                    hourlyRate
            );

            session.setAttribute("slotMessage", "Parking slot added successfully.");
            redirect = request.getContextPath()
                    + "/provider/slots?parkingLocationId=" + locationId;

        } catch (NumberFormatException e) {
            session.setAttribute("slotMessage",
                    "Enter a valid parking location and hourly rate.");
        } catch (IllegalArgumentException e) {
            session.setAttribute("slotMessage", e.getMessage());
            if (locationValue != null && locationValue.matches("\\d+")) {
                redirect = request.getContextPath()
                        + "/provider/slots?parkingLocationId=" + locationValue;
            }
        } catch (RuntimeException e) {
            session.setAttribute("slotMessage",
                    "Unable to add the slot. Check that its number is unique for this location.");
            if (locationValue != null && locationValue.matches("\\d+")) {
                redirect = request.getContextPath()
                        + "/provider/slots?parkingLocationId=" + locationValue;
            }
        }

        response.sendRedirect(redirect);
    }

    private boolean isProvider(HttpSession session) {
        return session != null
                && session.getAttribute("userId") instanceof Long
                && "PROVIDER".equals(session.getAttribute("userRole"));
    }
}