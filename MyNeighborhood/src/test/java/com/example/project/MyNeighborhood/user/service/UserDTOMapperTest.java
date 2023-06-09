package com.example.project.MyNeighborhood.user.service;

import com.example.project.MyNeighborhood.user.model.Role;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.model.UserDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserDTOMapperTest {
    private UserDTOMapper userDTOMapper;

    @BeforeEach
    void setUp(){
        userDTOMapper=new UserDTOMapper();
    }

    @Test
    void apply(){
        //Given
        final User user=buildUser();
        //When
        final UserDTO result=userDTOMapper.apply(user);
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.firstname()).isEqualTo(user.getFirstname());
        Assertions.assertThat(result.lastname()).isEqualTo(user.getLastname());
        Assertions.assertThat(result.email()).isEqualTo(user.getEmail());
        Assertions.assertThat(result.role()).isEqualTo(user.getRole());
        Assertions.assertThat(result.address()).isEqualTo(user.getAddress());
        Assertions.assertThat(result.governmentIdentity()).isEqualTo(user.getGovernmentIdentity());
        Assertions.assertThat(result.profilePicture()).isEqualTo(user.getProfilePicture());
    }

    private User buildUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .firstname("Eikichi")
                .lastname("Onizuka")
                .email("eikichi@gto.jp")
                .role(Role.USER)
                .password("gto")
                .address("address")
                .governmentIdentity(null)
                .profilePicture(null)
                .build();
    }
}
