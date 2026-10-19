package com.smartparking.controller;

import com.smartparking.dao.UserDAO;
import com.smartparking.dao.UserDAOImpl;
import com.smartparking.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/admin/users")
public class AdminUsersController extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        request.setAttribute("users", userDAO.findAll());

        request.getRequestDispatcher(
                        "/WEB-INF/views/admin-users.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        HttpSession session = request.getSession(false);

        try {
            String userIdValue = request.getParameter("userId");
            String statusValue = request.getParameter("status");

            if (userIdValue == null || statusValue == null) {
                throw new IllegalArgumentException(
                        "User ID and status are required.");
            }

            Long userId = Long.valueOf(userIdValue);

            if (userId <= 0) {
                throw new IllegalArgumentException(
                        "Invalid user ID.");
            }

            User.Status status = User.Status.valueOf(statusValue);

            if (status != User.Status.ACTIVE
                    && status != User.Status.BLOCKED) {
                throw new IllegalArgumentException(
                        "Invalid account status.");
            }

            User user = userDAO.findById(userId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "User not found."));

            Long adminId = (Long) session.getAttribute("userId");

            if (user.getRole() == User.Role.ADMIN
                    || user.getId().equals(adminId)) {
                throw new IllegalArgumentException(
                        "Admin accounts cannot be modified here.");
            }

            user.setStatus(status);
            userDAO.update(user);

            session.setAttribute(
                    "adminMessage",
                    "User status updated successfully.");

        } catch (IllegalArgumentException e) {
            session.setAttribute(
                    "adminMessage",
                    e.getMessage() == null
                            ? "Invalid user update request."
                            : e.getMessage());

        } catch (RuntimeException e) {
            session.setAttribute(
                    "adminMessage",
                    "Unable to update the user.");
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/users");
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null
                && "ADMIN".equals(
                session.getAttribute("userRole"));
    }
}