package com.autoescrow.escrow.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.autoescrow.escrow.exception.UnauthorizedActionException;

public final class ServiceSecurityUtil {

    private ServiceSecurityUtil() {
        // utility class
    }

    /**
     * Ensure current user has the required role.
     * Example: requireRole("ADMIN")
     */
    public static void requireRole(String requiredRole) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedActionException("Unauthenticated access");
        }

        String expectedAuthority = "ROLE_" + requiredRole;

        boolean hasRole = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(expectedAuthority::equals);

        if (!hasRole) {
            throw new UnauthorizedActionException(
                    "Access denied. Required role: " + requiredRole
            );
        }
    }

    /**
     * Get current authenticated user's email/username
     */
    public static String getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedActionException("Unauthenticated access");
        }

        return authentication.getName();
    }
}
