
package com.smartparking.controller;

import com.smartparking.entity.Payment;
import com.smartparking.entity.Reservation;
import com.smartparking.service.PaymentService;
import com.smartparking.service.ReservationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/reservation/payment")
public class PaymentController extends HttpServlet {

    private final PaymentService paymentService = new PaymentService();
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
            Long reservationId = Long.valueOf(
                    request.getParameter("reservationId"));

            Reservation reservation =
                    reservationService.getReservation(reservationId);

            Long customerId = (Long) session.getAttribute("userId");

            if (!customerId.equals(reservation.getCustomerId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            if (reservation.getStatus() != Reservation.Status.PENDING) {
                request.setAttribute("paymentMessage",
                        "This reservation is not awaiting payment.");
            }

            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher(
                            "/WEB-INF/views/payment.jsp")
                    .forward(request, response);

        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid reservation request.");
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

        try {
            Long reservationId = Long.valueOf(
                    request.getParameter("reservationId"));

            Long customerId = (Long) session.getAttribute("userId");

            Payment.Method method = Payment.Method.valueOf(
                    request.getParameter("method"));

            String outcome = request.getParameter("outcome");

            if (!"SUCCESS".equals(outcome)
                    && !"FAILED".equals(outcome)) {
                throw new IllegalArgumentException(
                        "Select a valid simulation outcome.");
            }

            Payment payment = paymentService.processPayment(
                    reservationId,
                    customerId,
                    method,
                    "SUCCESS".equals(outcome));

            Reservation reservation =
                    reservationService.getReservation(reservationId);

            request.setAttribute("payment", payment);
            request.setAttribute("reservation", reservation);

            request.getRequestDispatcher(
                            "/WEB-INF/views/payment-result.jsp")
                    .forward(request, response);

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher(
                            "/WEB-INF/views/payment-error.jsp")
                    .forward(request, response);
        } catch (RuntimeException e) {
            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to process the simulated payment.");
        }
    }

    private boolean isCustomer(HttpSession session) {
        return session != null
                && session.getAttribute("userId") instanceof Long
                && "CUSTOMER".equals(session.getAttribute("userRole"));
    }
}
