package com.example.SpringShop.Utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JWTUtil {
    private String key = "e76aee2131c72fe53e7a0657bc97f34d1517170210085c424cbcc20febf1304086e1cc7cf6488da9dffa5398156174e42df2763c7acecedce8d8f17530154ce160f55b2172252ca7443f017d3546602c53884774fd6427cb5bf525a16d1e7238634bfb5ee3eae4701d065ff500fadfb459faf3a352ea349d2eb7df014626c3d5";
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private long expirationTime = 86400000;
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }

    public void invalidateToken(String token) {
        tokenBlacklist.add(token);
    }
}
