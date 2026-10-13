package com.smartparking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setHeader(
                "Cache-Control",
                "no-cache, no-store, must-revalidate"
        );
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("userRole");

        if ("CUSTOMER".equals(role)) {
            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-dashboard.jsp"
            ).forward(request, response);
        } else if ("PROVIDER".equals(role)) {
            request.getRequestDispatcher(
                    "/WEB-INF/views/provider-dashboard.jsp"
            ).forward(request, response);
        } else if ("ADMIN".equals(role)) {
            request.getRequestDispatcher(
                    "/WEB-INF/views/admin-dashboard.jsp"
            ).forward(request, response);
        } else {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}