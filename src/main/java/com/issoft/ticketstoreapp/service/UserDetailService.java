package com.issoft.ticketstoreapp.service;

import com.issoft.ticketstoreapp.model.User;
import com.issoft.ticketstoreapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static org.springframework.security.core.userdetails.User.withUsername;

@Component
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private static final String USERNAME_NOT_FOUND_MESSAGE = " username [%s] not found";

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) {
        User user = userRepository.findByLogin(login).orElseThrow(() ->
                new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MESSAGE, login)));
        return withUsername(user.getLogin())
                .password(user.getPassword())
                .authorities(user.getRoles())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}