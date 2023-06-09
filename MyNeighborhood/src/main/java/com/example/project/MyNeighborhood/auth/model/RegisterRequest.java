package com.example.project.MyNeighborhood.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.UUID;

@Data
@Value
@Builder
@AllArgsConstructor
public class RegisterRequest {
    String firstname;
    String lastname;
    String email;
    String password;
    String address;
    UUID governmentIdentityId;
}
