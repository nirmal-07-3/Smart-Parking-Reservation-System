
package com.smartparking.controller;

import com.smartparking.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/reservation/cancel")
public class ReservationCancellationController extends HttpServlet {

    private final ReservationService reservationService =
            new ReservationService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || !(session.getAttribute("userId") instanceof Long)
                || !"CUSTOMER".equals(session.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            String value = request.getParameter("reservationId");

            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(
                        "Reservation ID is required.");
            }

            Long reservationId = Long.parseLong(value);

            reservationService.cancelPendingReservation(
                    reservationId,
                    (Long) session.getAttribute("userId")
            );

            response.sendRedirect(
                    request.getContextPath() + "/reservation/history"
            );

        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute(
                    "reservationError", e.getMessage()
            );

            response.sendRedirect(
                    request.getContextPath() + "/reservation/history"
            );
        } catch (RuntimeException e) {
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to cancel the reservation."
            );
        }
    }
}
