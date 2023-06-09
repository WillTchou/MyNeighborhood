package com.example.project.MyNeighborhood.volunteer.controller;

import com.example.project.MyNeighborhood.volunteer.service.VolunteerService;
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
class VolunteerControllerTest {

    @Mock
    private VolunteerService volunteerService;
    private VolunteerController volunteerController;

    @BeforeEach
    void setUp() {
        Assertions.assertThat(volunteerService).isNotNull();
        volunteerController = new VolunteerController(volunteerService);
    }

    @Test
    void createRequest() {
        //Given
        final String userId = UUID.randomUUID().toString();
        final UUID requestId = UUID.randomUUID();
        //When
        final ResponseEntity<Void> result = volunteerController.createVolunteer(userId, requestId);
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Mockito.verify(volunteerService, Mockito.only()).createVolunteer(userId, requestId);
    }
}
