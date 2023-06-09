package com.example.project.MyNeighborhood.user.controller;

import com.example.project.MyNeighborhood.user.model.Role;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.model.UserDTO;
import com.example.project.MyNeighborhood.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final UUID USER_ID = UUID.randomUUID();
    @Mock
    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        Assertions.assertThat(userService).isNotNull();
        userController = new UserController(userService);
    }

    @Test
    void getUserById() {
        //Given
        final UserDTO userDTO = new UserDTO(USER_ID, "harry", "potter", "potterHarry@hogwarts.gb",
                "hogwarts", Role.USER, null, null);
        //When
        Mockito.when(userService.getUserById(USER_ID.toString())).thenReturn(userDTO);
        final ResponseEntity<UserDTO> result = userController.getUserById(USER_ID.toString());
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        final UserDTO resultBody = result.getBody();
        Assertions.assertThat(resultBody).isNotNull()
                .isEqualTo(userDTO);
    }

    @Test
    void updateUser() {
        //Given
        final User user = buildUser();
        //When
        final ResponseEntity<Void> result = userController.updateUser(USER_ID, user);
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Mockito.verify(userService, Mockito.only()).updateUser(USER_ID, user);
    }

    private User buildUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .firstname("Nicky")
                .lastname("Larson")
                .email("email@oui.com")
                .role(Role.USER)
                .password("password")
                .address("address")
                .governmentIdentity(null)
                .profilePicture(null)
                .build();
    }
}
