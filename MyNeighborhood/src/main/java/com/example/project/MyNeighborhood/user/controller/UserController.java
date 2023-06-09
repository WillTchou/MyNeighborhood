package com.example.project.MyNeighborhood.user.controller;

import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.model.UserDTO;
import com.example.project.MyNeighborhood.user.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") final String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") final UUID userId,
                                           @RequestBody @NotNull final User user) {
        userService.updateUser(userId,user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
