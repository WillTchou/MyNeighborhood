package com.example.project.MyNeighborhood.profilePicture.controller;

import com.example.project.MyNeighborhood.profilePicture.service.ProfilePictureServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ProfilePictureControllerTest {

    @Mock
    private ProfilePictureServiceImpl profilePictureService;
    private ProfilePictureController profilePictureController;

    @BeforeEach
    void setUp() {
        Assertions.assertThat(profilePictureService).isNotNull();
        profilePictureController = new ProfilePictureController(profilePictureService);
    }

    @Test
    void uploadImage() throws IOException {
        //Given
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes()
        );
        //When
        final ResponseEntity<UUID> result = profilePictureController.uploadImage(file);
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Mockito.verify(profilePictureService, Mockito.only()).uploadFile(file);
    }
}
