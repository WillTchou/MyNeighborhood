package com.example.project.MyNeighborhood.user.service;

import com.example.project.MyNeighborhood.governmentIdentity.model.GovernmentIdentity;
import com.example.project.MyNeighborhood.profilePicture.model.ProfilePicture;
import com.example.project.MyNeighborhood.user.model.Role;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Function;

@Service
public final class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(final User user) {
        final UUID id = user.getId();
        final String firstname = user.getFirstname();
        final String lastname = user.getLastname();
        final String email = user.getEmail();
        final String address = user.getAddress();
        final Role role = user.getRole();
        final GovernmentIdentity governmentIdentity = user.getGovernmentIdentity();
        final ProfilePicture profilePicture = user.getProfilePicture();
        return new UserDTO(id, firstname, lastname, email, address, role, governmentIdentity, profilePicture);
    }
}
