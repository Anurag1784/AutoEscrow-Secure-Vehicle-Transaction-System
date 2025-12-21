package com.autoescrow.escrow.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration().after(new Date());
    }

    private Claims extractAllClaims(String token) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
