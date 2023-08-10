package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.AuthenticationService;
import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.AuthenticationResponse;
import com.example.demo.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RestController
@Log
@RequiredArgsConstructor
public class DemoController {

    private final AuthenticationService authenticationService;


    @GetMapping("/api/hello-user")
    public ResponseEntity<String> hello() {
        var principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Hello User: " + principal.getUsername());
        return ResponseEntity.ok("Hello User");
    }

    @GetMapping("/api/hello-admin")
    public ResponseEntity<String> helloAdmin() {
        var principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Hello Admin: " + principal.getUsername());
        return ResponseEntity.ok("Hello Admin");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Validated AuthenticationRequest user) {
        log.info("Login: " + user.getUsername());
        return ResponseEntity.ok(authenticationService.authenticate(user));
    }
}
