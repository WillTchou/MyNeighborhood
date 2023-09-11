package com.example.project.MyNeighborhood.auth.service;

import com.example.project.MyNeighborhood.auth.model.AuthenticationRequest;
import com.example.project.MyNeighborhood.auth.model.AuthenticationResponse;
import com.example.project.MyNeighborhood.auth.model.RegisterRequest;
import com.example.project.MyNeighborhood.config.JwtService;
import com.example.project.MyNeighborhood.governmentIdentity.model.GovernmentIdentity;
import com.example.project.MyNeighborhood.governmentIdentity.service.GovernmentIdentityService;
import com.example.project.MyNeighborhood.user.model.Role;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final GovernmentIdentityService governmentIdentityService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(final RegisterRequest registerRequest) {
        final User user = buildUserWithRegisterRequest(registerRequest);
        userService.createUser(user);
        final String jwtToken = jwtService.generateToken(user);
        return buildAuthenticationResponse(jwtToken, user.getId());
    }

    public AuthenticationResponse authenticate(final AuthenticationRequest authenticationRequest) {
        final String requestUsername = authenticationRequest.getEmail();
        final String requestPassword = authenticationRequest.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                requestUsername,
                requestPassword)
        );
        final User user = userService.getUserByEmail(requestUsername);
        final String jwtToken = jwtService.generateToken(user);
        return buildAuthenticationResponse(jwtToken, user.getId());
    }

    private User buildUserWithRegisterRequest(final RegisterRequest registerRequest) {
        final String registerRequestPassword = registerRequest.getPassword();
        final GovernmentIdentity governmentIdentity = getGovernmentIdentity(registerRequest);
        return new User(registerRequest.getFirstname(), registerRequest.getLastname(), registerRequest.getEmail(),
                registerRequest.getAddress(),
                Role.USER,
                passwordEncoder.encode(registerRequestPassword),
                governmentIdentity);
    }

    private GovernmentIdentity getGovernmentIdentity(RegisterRequest registerRequest) {
        return governmentIdentityService.getGovernmentIdentityById(registerRequest.getGovernmentIdentityId());
    }

    private AuthenticationResponse buildAuthenticationResponse(final String jwtToken, final UUID userId) {
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(userId)
                .build();
    }
}
