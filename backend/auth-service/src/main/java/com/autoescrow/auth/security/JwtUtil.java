package com.autoescrow.auth.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

    private static final String SECRET_KEY =
            "autoescrow_secret_key_which_is_very_secure_12345";

    private static final String ISSUER =
            "autoescrow-auth-service";

    private static final String AUDIENCE =
            "autoescrow-admin-service";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 10; // 10 hours

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ✅ Generate JWT with EMAIL + ROLE + ISSUER + AUDIENCE
    public static String generateToken(String email, String role) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuer(ISSUER)
                .setAudience(AUDIENCE)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔹 Validate Token
    public static boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 🔹 Extract Email
    public static String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    private static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
