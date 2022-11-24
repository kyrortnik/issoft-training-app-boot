package com.issoft.ticketstoreapp.security.filter;

import com.issoft.ticketstoreapp.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private static final String LOGIN = "/login";
    private static final String BEARER = "Bearer ";
    @Autowired
    private JwtUtil jwtUtil;

    public AuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request.getServletPath().equals(LOGIN)) {
            chain.doFilter(request, response);
        } else {
            String authHeader = request.getHeader(AUTHORIZATION);
            Optional<String> jwtToken = getBearerToken(authHeader);
            if (jwtToken.isPresent() && this.jwtUtil.isNotExpiredToken(jwtToken.get())) {
                String username = this.jwtUtil.getUsername(jwtToken.get());
                List<GrantedAuthority> roles = this.jwtUtil.getRoles(jwtToken.get());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, roles);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    private Optional<String> getBearerToken(String authHeader) {
        Optional<String> token = Optional.empty();
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            token = Optional.of(authHeader.substring(BEARER.length()));
        }
        return token;
    }
}