package com.smartparking.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String contextPath = httpRequest.getContextPath();
        String path = httpRequest.getRequestURI()
                .substring(contextPath.length());

        if (path.equals("/login")
                || path.equals("/register")
                || path.equals("/home")
                || path.equals("/logout")
                || path.equals("/")
                || path.startsWith("/assets/")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.equals("/favicon.ico")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }

        String role = (String) session.getAttribute("userRole");

        if (role == null) {
            session.invalidate();
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }

        boolean allowed = true;

        if (path.startsWith("/admin/")) {
            allowed = "ADMIN".equals(role);
        } else if (path.startsWith("/provider/")) {
            allowed = "PROVIDER".equals(role) || "ADMIN".equals(role);
        } else if (path.startsWith("/customer/")
                || path.startsWith("/vehicles/")
                || path.startsWith("/reservation/")) {
            allowed = "CUSTOMER".equals(role);
        }

        if (!allowed) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }
}