package com.example.project.MyNeighborhood.user.model;

import com.example.project.MyNeighborhood.exception.UserStateException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserTest {

    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String EMAIL = "email";
    private static final String ADDRESS = "address";
    private static final String PASSWORD = "password";

    @Test
    void createUserWithBlankedAttribute() {
        //Given && When && Then
        Assertions.assertThatThrownBy(() -> getUser("   ", LASTNAME,
                        EMAIL, ADDRESS))
                .isInstanceOf(UserStateException.class);
        Assertions.assertThatThrownBy(() -> getUser(FIRSTNAME, "   ",
                        EMAIL, ADDRESS))
                .isInstanceOf(UserStateException.class);
        Assertions.assertThatThrownBy(() -> getUser(FIRSTNAME, LASTNAME,
                        "   ", ADDRESS))
                .isInstanceOf(UserStateException.class);
        Assertions.assertThatThrownBy(() -> getUser(FIRSTNAME, LASTNAME,
                        EMAIL, "   "))
                .isInstanceOf(UserStateException.class);
    }

    @Test
    void createUserWithNullAttribute() {
        //Given && When && Then
        Assertions.assertThatThrownBy(() -> getUser(null, LASTNAME,
                        EMAIL, ADDRESS))
                .isInstanceOf(UserStateException.class);
        Assertions.assertThatThrownBy(() -> getUser(FIRSTNAME, null,
                        EMAIL, ADDRESS))
                .isInstanceOf(UserStateException.class);
        Assertions.assertThatThrownBy(() -> getUser(FIRSTNAME, LASTNAME,
                        null, ADDRESS))
                .isInstanceOf(UserStateException.class);
        Assertions.assertThatThrownBy(() -> getUser(FIRSTNAME, LASTNAME,
                        EMAIL, null))
                .isInstanceOf(UserStateException.class);
    }

    private static void getUser(final String firstname, final String lastname, final String email,
                                final String address) {
        User.builder()
                .id(UUID.randomUUID())
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .governmentIdentity(null)
                .profilePicture(null)
                .address(address)
                .password(PASSWORD)
                .build();
    }
}
