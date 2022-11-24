package com.issoft.ticketstoreapp.security.filter;

import com.issoft.ticketstoreapp.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String BEARER = "Bearer ";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        User user = (User) authResult.getPrincipal();
        String jwt = this.jwtUtil.createToken(user.getUsername(), new HashSet<>(user.getAuthorities()));
        response.setHeader(AUTHORIZATION, BEARER + jwt);
    }
}