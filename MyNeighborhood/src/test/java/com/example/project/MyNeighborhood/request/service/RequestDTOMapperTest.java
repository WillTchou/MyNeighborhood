package com.example.project.MyNeighborhood.request.service;

import com.example.project.MyNeighborhood.request.model.Request;
import com.example.project.MyNeighborhood.request.model.RequestDTO;
import com.example.project.MyNeighborhood.request.model.Status;
import com.example.project.MyNeighborhood.request.model.Type;
import com.example.project.MyNeighborhood.user.model.Role;
import com.example.project.MyNeighborhood.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RequestDTOMapperTest {
    private RequestDTOMapper requestDTOMapper;

    @BeforeEach
    void setUp() {
        requestDTOMapper = new RequestDTOMapper();
    }

    @Test
    void apply() {
        //Given
        final User user = buildUser();
        final UUID id = UUID.randomUUID();
        final Type oneTimeTask = Type.OneTimeTask;
        final Status fulfilled = Status.Fulfilled;
        final float latitude = 1F;
        final float longitude = 2f;
        final String description = "description";
        final LocalDateTime now = LocalDateTime.now();
        final String address = "address";
        final Request request = new Request(id, oneTimeTask, fulfilled, latitude, longitude, address,
                description, now, now, true, user, null);
        //When
        final RequestDTO result = requestDTOMapper.apply(request);
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.id()).isEqualTo(request.getId());
        Assertions.assertThat(result.type()).isEqualTo(request.getType());
        Assertions.assertThat(result.status()).isEqualTo(request.getStatus());
        Assertions.assertThat(result.description()).isEqualTo(request.getDescription());
        Assertions.assertThat(result.latitude()).isEqualTo(request.getLatitude());
        Assertions.assertThat(result.longitude()).isEqualTo(request.getLongitude());
        Assertions.assertThat(result.address()).isEqualTo(request.getAddress());
        Assertions.assertThat(result.creationDate()).isEqualTo(request.getCreationDate());
        Assertions.assertThat(result.fulfilledDate()).isEqualTo(request.getFulfilledDate());
        Assertions.assertThat(result.requester()).isEqualTo(request.getRequester());
    }

    private static User buildUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .firstname("Killua")
                .lastname("Zoldyck")
                .email("killua@hunter.hxh")
                .role(Role.USER)
                .password("aruka")
                .address("Zoldyck's house")
                .governmentIdentity(null)
                .profilePicture(null)
                .build();
    }
}
