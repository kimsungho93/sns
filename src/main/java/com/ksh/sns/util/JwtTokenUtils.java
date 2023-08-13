package com.ksh.sns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    public static String getEmail(String token, String key) {
        return extractClaims(token, key).get("email", String.class);
    }

    public static boolean isExpired(String token, String key) {
        Date expiredDate = extractClaims(token, key).getExpiration();
        return expiredDate.before(new Date());
    }

    private static Claims extractClaims(String token, String key) {
        return Jwts.parserBuilder().setSigningKey(geKey(key))
                .build().parseClaimsJws(token).getBody();
    }

    public static String generateToken(String email, String key, long expiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(geKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key geKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
