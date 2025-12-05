package com.houssam.smartShop.config;

import com.houssam.smartShop.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Vous devez vous connecter");
            return false;
        }
        String userId = (String) session.getAttribute("userId");
        UserRole userRole = (UserRole) session.getAttribute("role");

        if (userId == null || userRole == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session invalide");
            return false;
        }
        request.setAttribute("currentUserId", userId);
        request.setAttribute("currentUserRole", userRole);

        if (!hasAccess(request, userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé");
            return false;
        }
        return true;
    }

    private boolean hasAccess(HttpServletRequest request, UserRole userRole) {
        String uri = request.getRequestURI();

        return switch (userRole) {
            case ADMIN -> true;
            case CLIENT -> !uri.startsWith("/api/admin");
            default -> false;
        };
    }
}