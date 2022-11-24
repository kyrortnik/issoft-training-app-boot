package com.issoft.ticketstoreapp.security.util;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final String ROLES_KEY = "roles";
    private static final String AUTHORITY = "authority";
    private final String secretKey;
    private final long validityInMilliseconds;

    @Autowired
    public JwtUtil(@Value("${security.jwt.token.secret-key}") String secretKey,
                   @Value("${security.jwt.token.expiration}") long validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(String username, Set<GrantedAuthority> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(ROLES_KEY, roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList()));
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean isNotExpiredToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            Date expirationTime = claimsJws.getBody().getExpiration();
            Date currentTime = new Date();

            return currentTime.before(expirationTime);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public List<GrantedAuthority> getRoles(String token) {
        List<Map<String, String>> roleClaims = (List<Map<String, String>>) Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().get(ROLES_KEY);
        return roleClaims.stream().map(roleClaim ->
                        new SimpleGrantedAuthority(roleClaim.get(AUTHORITY)))
                .collect(Collectors.toList());
    }
}