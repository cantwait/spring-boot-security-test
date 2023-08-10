package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.demo.model.Role;
import com.example.demo.model.User;

@Component
public class UserRepo {
    private static final List<User> USERS = List.of(
        User.builder()
            .username("user")
            .password("password")
            .authorities(
                List.of(new SimpleGrantedAuthority("USER"))
            ).build(),
        User.builder()
            .username("admin")
            .password("password")
            .authorities(
                List.of(
                    new SimpleGrantedAuthority("USER"),
                    new SimpleGrantedAuthority("ADMIN")
                )
            )
            .build()
    );

    public Optional<User> findByUsername(String username) {
        return USERS.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }
}
