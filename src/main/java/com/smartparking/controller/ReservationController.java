
package com.smartparking.controller;

import com.smartparking.entity.Reservation;
import com.smartparking.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/reservation/history")
public class ReservationController extends HttpServlet {

    private final ReservationService reservationService =
            new ReservationService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null
                || !"CUSTOMER".equals(session.getAttribute("userRole"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Long customerId = (Long) session.getAttribute("userId");
        List<Reservation> reservations =
                reservationService.getCustomerReservations(customerId);

        request.setAttribute("reservations", reservations);

        request.getRequestDispatcher(
                        "/WEB-INF/views/reservation-history.jsp")
                .forward(request, response);
    }
}
