package com.example.project.MyNeighborhood.governmentIdentity.controller;

import com.example.project.MyNeighborhood.governmentIdentity.service.GovernmentIdentityServiceImpl;
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
class GovernmentIdentityControllerTest {
    @Mock
    private GovernmentIdentityServiceImpl governmentIdentityService;
    private GovernmentIdentityController governmentIdentityController;

    @BeforeEach
    void setUp() {
        Assertions.assertThat(governmentIdentityService).isNotNull();
        governmentIdentityController = new GovernmentIdentityController(governmentIdentityService);
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
        final ResponseEntity<UUID> result = governmentIdentityController.uploadDocument(file);
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Mockito.verify(governmentIdentityService, Mockito.only()).uploadFile(file);
    }
}
