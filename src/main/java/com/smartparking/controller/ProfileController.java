package com.smartparking.controller;

import com.smartparking.entity.User;
import com.smartparking.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("userId") instanceof Long)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long userId = (Long) session.getAttribute("userId");

        try {
            User user = new com.smartparking.dao.UserDAOImpl()
                    .findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User account was not found."));

            request.setAttribute("profileUser", user);
            request.getRequestDispatcher("/WEB-INF/views/profile.jsp")
                    .forward(request, response);

        } catch (IllegalArgumentException e) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || !(session.getAttribute("userId") instanceof Long)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long userId = (Long) session.getAttribute("userId");

        try {
            User updated = userService.updateProfile(
                    userId,
                    request.getParameter("name"),
                    request.getParameter("phone")
            );

            session.setAttribute("userName", updated.getName());
            session.setAttribute("profileMessage", "Your profile was updated successfully.");
            response.sendRedirect(request.getContextPath() + "/profile");

        } catch (IllegalArgumentException e) {
            session.setAttribute("profileMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/profile");

        } catch (RuntimeException e) {
            session.setAttribute("profileMessage",
                    "Unable to update your profile. Please try again.");
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
}