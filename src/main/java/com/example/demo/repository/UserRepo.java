package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;

@Component
public class UserRepo {
    private static final List<User> USERS = List.of(
        User.builder()
            .username("user")
            .password("$2a$12$vJpj1G2jfWrh.n/W4tLJJuoSLlNTzbx4qh41KHZIuRdell/pdPYnO")
            .authorities(
                List.of(new SimpleGrantedAuthority("USER"))
            ).build(),
        User.builder()
            .username("admin")
            .password("$2a$12$vJpj1G2jfWrh.n/W4tLJJuoSLlNTzbx4qh41KHZIuRdell/pdPYnO")
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
