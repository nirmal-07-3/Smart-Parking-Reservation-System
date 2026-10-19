package com.smartparking.controller;

import com.smartparking.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordController extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher(
                        "/WEB-INF/views/change-password.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Long userId = Long.valueOf(
                    session.getAttribute("userId").toString());

            userService.changePassword(
                    userId,
                    request.getParameter("currentPassword"),
                    request.getParameter("newPassword"),
                    request.getParameter("confirmPassword"));

            session.setAttribute(
                    "passwordMessage",
                    "Password changed successfully.");

            response.sendRedirect(
                    request.getContextPath() + "/change-password");

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher(
                            "/WEB-INF/views/change-password.jsp")
                    .forward(request, response);

        } catch (RuntimeException e) {
            request.setAttribute(
                    "errorMessage",
                    "Unable to change the password. Please try again.");

            request.getRequestDispatcher(
                            "/WEB-INF/views/change-password.jsp")
                    .forward(request, response);
        }
    }
}