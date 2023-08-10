package com.example.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Role {
    private final String name;
}
