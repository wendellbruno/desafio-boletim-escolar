package com.wendell.backend.modules.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(String username, Long userId,
                                List<Long> classrooms,
                                List<Long> disciplines) {

        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("classrooms", classrooms)
                .claim("disciplines", disciplines)
                .issuedAt(now)
                .expiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    public List<Long> getClassrooms(String token) {
        return convertToLongList(getClaims(token).get("classrooms", List.class));
    }

    public List<Long> getDisciplines(String token) {
        return convertToLongList(getClaims(token).get("disciplines", List.class));
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private List<Long> convertToLongList(List<?> values) {
        if (values == null) {
            return List.of();
        }

        return values.stream()
                .map(value -> Long.valueOf(String.valueOf(value)))
                .collect(Collectors.toList());
    }
}
