package com.example.project.MyNeighborhood.request.model;

import com.example.project.MyNeighborhood.exception.RequestStateException;
import com.example.project.MyNeighborhood.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RequestTest {

    private static final UUID UUID = java.util.UUID.randomUUID();

    @Test
    void createRequestWithBlankedDescription() {
        //Given && When && Then
        Assertions.assertThatThrownBy(() -> getRequest("   "))
                .isInstanceOf(RequestStateException.class);
    }

    @Test
    void createRequestWithNullDescription() {
        //Given && When && Then
        Assertions.assertThatThrownBy(() -> getRequest(null))
                .isInstanceOf(RequestStateException.class);
    }

    @Test
    void createRequestWithNullLocation() {
        //Given && When && Then
        Assertions.assertThatThrownBy(() -> getRequest(Type.OneTimeTask,null,null))
                .isInstanceOf(RequestStateException.class);
    }

    @Test
    void createRequestWithNullType() {
        //Given && When && Then
        Assertions.assertThatThrownBy(() -> getRequest(null, -1F, 1F))
                .isInstanceOf(RequestStateException.class);
    }

    @Test
    void createRequestWithTooLongDescription() {
        //Given && When && Then
        Assertions.assertThatThrownBy(() -> getRequest("Lorem ipsum dolor sit " +
                        "amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. " +
                        "Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus." +
                        " Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat" +
                        " massa quis enim. Donec p\n" +
                        "\n"))
                .isInstanceOf(RequestStateException.class);
    }

    private static void getRequest(final String description) {
        new Request(UUID, Type.MaterialNeed, -1F, 1F,"address", description,
                getUser(), null);
    }

    private static void getRequest(final Type type, final Float latitude, final Float longitude) {
        new Request(UUID, type, latitude, longitude, "address","description",
                getUser(), null);
    }

    private static User getUser() {
        return User.builder()
                .id(UUID)
                .firstname("will")
                .lastname("liam")
                .email("wil@mil.com")
                .governmentIdentity(null)
                .profilePicture(null)
                .address("address")
                .password("password")
                .build();
    }
}
