package com.issoft.ticketstoreapp.security.config;

import com.issoft.ticketstoreapp.security.filter.AuthenticationFilter;
import com.issoft.ticketstoreapp.security.filter.AuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.header.HeaderWriterFilter;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {
    private static final String LOGIN_URL = "/login";
    private static final String SWAGGER_URL = "/swagger-ui/**";
    private static final String OPEN_DOCS_URL = "/api-docs/**";
    private static final String H2_CONSOLE_URL = "/h2-console/**";
    private static final String LOGIN = "login";


    @Bean
    public AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(authenticationManager());
        authenticationFilter.setUsernameParameter(LOGIN);
        return authenticationFilter;
    }

    @Bean
    public AuthorizationFilter appAuthorizationFilter() throws Exception {
        return new AuthorizationFilter(authenticationManager());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .mvcMatchers(POST, LOGIN_URL).permitAll()
                .antMatchers(SWAGGER_URL).permitAll()
                .antMatchers(OPEN_DOCS_URL).permitAll()
                .antMatchers(H2_CONSOLE_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(authenticationFilter(), HeaderWriterFilter.class)
                .addFilterAfter(appAuthorizationFilter(), AuthenticationFilter.class)
                .headers().frameOptions().disable();
        return http.build();
    }
}