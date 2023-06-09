package com.example.project.MyNeighborhood.user.service;

import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.model.UserDTO;

import java.util.UUID;

public interface UserService {
    UserDTO getUserById(String userId);

    User getUserByEmail(String email);

    void createUser(User user);

    void updateUser(UUID userId, User updatedUser);
}
