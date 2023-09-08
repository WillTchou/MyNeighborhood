package com.example.project.MyNeighborhood.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder
@AllArgsConstructor
public class AuthenticationRequest {

    String email;
    String password;
}