package com.smartparking.controller;

import com.smartparking.dao.PaymentDAO;
import com.smartparking.dao.PaymentDAOImpl;
import com.smartparking.dao.ReservationDAO;
import com.smartparking.dao.ReservationDAOImpl;
import com.smartparking.dao.UserDAO;
import com.smartparking.dao.UserDAOImpl;
import com.smartparking.entity.Payment;
import com.smartparking.entity.Reservation;
import com.smartparking.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin/reports")
public class AdminReportsController extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();
    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        List<User> users = userDAO.findAll();
        List<Reservation> reservations = reservationDAO.findAll();
        List<Payment> payments = paymentDAO.findAll();

        long customers = users.stream()
                .filter(u -> u.getRole() == User.Role.CUSTOMER)
                .count();

        long providers = users.stream()
                .filter(u -> u.getRole() == User.Role.PROVIDER)
                .count();

        long activeUsers = users.stream()
                .filter(u -> u.getStatus() == User.Status.ACTIVE)
                .count();

        long confirmedReservations = reservations.stream()
                .filter(r -> r.getStatus() == Reservation.Status.CONFIRMED)
                .count();

        long pendingReservations = reservations.stream()
                .filter(r -> r.getStatus() == Reservation.Status.PENDING)
                .count();

        long cancelledReservations = reservations.stream()
                .filter(r -> r.getStatus() == Reservation.Status.CANCELLED)
                .count();

        long successfulPayments = payments.stream()
                .filter(p -> p.getStatus() == Payment.Status.SUCCESS)
                .count();

        long failedPayments = payments.stream()
                .filter(p -> p.getStatus() == Payment.Status.FAILED)
                .count();

        BigDecimal successfulPaymentTotal = payments.stream()
                .filter(p -> p.getStatus() == Payment.Status.SUCCESS)
                .map(Payment::getAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        request.setAttribute("totalUsers", users.size());
        request.setAttribute("customers", customers);
        request.setAttribute("providers", providers);
        request.setAttribute("activeUsers", activeUsers);
        request.setAttribute("totalReservations", reservations.size());
        request.setAttribute("confirmedReservations", confirmedReservations);
        request.setAttribute("pendingReservations", pendingReservations);
        request.setAttribute("cancelledReservations", cancelledReservations);
        request.setAttribute("totalPayments", payments.size());
        request.setAttribute("successfulPayments", successfulPayments);
        request.setAttribute("failedPayments", failedPayments);
        request.setAttribute("successfulPaymentTotal", successfulPaymentTotal);

        request.getRequestDispatcher(
                        "/WEB-INF/views/admin-reports.jsp")
                .forward(request, response);
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null
                && "ADMIN".equals(session.getAttribute("userRole"));
    }
}