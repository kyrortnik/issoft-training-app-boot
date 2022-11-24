package com.issoft.ticketstoreapp.security.config;

import com.issoft.ticketstoreapp.security.filter.AuthenticationFilter;
import com.issoft.ticketstoreapp.security.filter.AuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.header.HeaderWriterFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration /*implements WebMvcConfigurer*/ {
    private static final String LOGIN_URL = "/login";
    private static final String REGISTER_URL = "/api/v1/users/register";
    private static final String SWAGGER_URL = "/swagger-ui/**";
    private static final String OPEN_DOCS_URL = "/api-docs/**";
    private static final String H2_CONSOLE_URL = "/h2-console/**";
    private static final String LOGIN = "login";
    private static final String HTTP_LOCALHOST_4200 = "http://localhost:4200";
    private static final String PATH_PATTERN = "/**";

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

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setExposedHeaders(Arrays.asList(AUTHORIZATION));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(PATH_PATTERN).allowedOrigins(HTTP_LOCALHOST_4200);
            }
        };
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .mvcMatchers(POST, LOGIN_URL).permitAll()
                .mvcMatchers(POST, REGISTER_URL).permitAll()
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