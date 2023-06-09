package com.example.project.MyNeighborhood.user.model;

import com.example.project.MyNeighborhood.governmentIdentity.model.GovernmentIdentity;
import com.example.project.MyNeighborhood.profilePicture.model.ProfilePicture;

import java.util.UUID;

public record UserDTO(UUID id,
                      String firstname,
                      String lastname,
                      String email,
                      String address,
                      Role role,
                      GovernmentIdentity governmentIdentity,
                      ProfilePicture profilePicture) {
}
