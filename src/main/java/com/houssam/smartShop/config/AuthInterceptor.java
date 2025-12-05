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
        if (userRole == UserRole.ADMIN) {
            return true;
        }

        if (userRole == UserRole.CLIENT) {
            String uri = request.getRequestURI();
            String method = request.getMethod();

            if (uri.contains("/confirm")) return false;
            if (uri.startsWith("/api/paiements")) return false;
            if (uri.equals("/api/clients") && method.equals("GET")) return false;
            if (uri.equals("/api/orders") && method.equals("GET")) return false;
            return true;
        }
        return false;
    }
}