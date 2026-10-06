package org.ftm.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private final SecretKey signingKey;
    @Getter
    private final long expiration;
    private final String issuer;


    public JwtService(
            @Value("${spring.security.jwt.secret}") String secret,
            @Value("${spring.security.jwt.expiration}") long expiration,
            @Value("${spring.security.jwt.issuer}") String issuer
    ) {
        this.signingKey = Keys.hmacShaKeyFor( secret.getBytes(StandardCharsets.UTF_8) );
        this.expiration = expiration;
        this.issuer = issuer;
    }


    public String generateToken(CustomUserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", userDetails.getId().toString());

        claims.put("userType", userDetails.getUserType());

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(authority -> authority.startsWith("ROLE_"))
                .toList();

        List<String> permissions = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(authority -> !authority.startsWith("ROLE_"))
                .toList();

        claims.put("roles", roles);
        claims.put("permissions", permissions);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + expiration
                ))
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UUID extractUserId(String token) {
        String userId = extractClaim(
                token, claims -> claims.get("userId", String.class)
        );
        return UUID.fromString(userId);
    }

    public <T> T extractClaim( String token, Function<Claims, T> resolver ) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid( String token, UserDetails userDetails ) {
        final String username = extractUsername(token);
        return username.equals( userDetails.getUsername() ) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
