package com.example.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
}
