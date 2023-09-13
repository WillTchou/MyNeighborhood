package com.example.project.MyNeighborhood.request.controller;

import com.example.project.MyNeighborhood.request.model.*;
import com.example.project.MyNeighborhood.request.service.RequestService;
import com.example.project.MyNeighborhood.user.model.Role;
import com.example.project.MyNeighborhood.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String USER_ID_STRING = USER_ID.toString();
    @Mock
    private RequestService requestService;
    private RequestController requestController;

    @BeforeEach
    void setUp() {
        Assertions.assertThat(requestService).isNotNull();
        requestController = new RequestController(requestService);
    }

    @Test
    void getAllRequestsForUser() {
        //Given
        final String userId = USER_ID.toString();
        final User user = buildUser();
        final RequestDTO requestDTO = new RequestDTO(UUID.randomUUID(), Type.MaterialNeed, Status.Unfulfilled, 1F,
                3F, "address","description", LocalDateTime.now(), null,
                true, user);
        final List<RequestDTO> requestDTOS = List.of(requestDTO);
        //When
        Mockito.when(requestService.getAllRequestsForUser(userId)).thenReturn(requestDTOS);
        final ResponseEntity<List<RequestDTO>> result = requestController.getAllRequestsForUser(userId);
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        final List<RequestDTO> resultBody = result.getBody();
        Assertions.assertThat(resultBody).isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .isEqualTo(requestDTOS);
        Mockito.verify(requestService, Mockito.only()).getAllRequestsForUser(userId);
    }

    @Test
    void getUnfulfilledRequestsNumber() {
        //Given && When
        Mockito.when(requestService.getUnfulfilledRequestsNumber()).thenReturn(3);
        final ResponseEntity<Integer> result = requestController.getUnfulfilledRequestsNumber();
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        final Integer resultBody = result.getBody();
        Assertions.assertThat(resultBody).isNotNull()
                .isEqualTo(3);
        Mockito.verify(requestService, Mockito.only()).getUnfulfilledRequestsNumber();
    }

    @Test
    void createRequest() {
        //Given
        final RequestForm requestForm = RequestForm.builder()
                .type(Type.OneTimeTask)
                .description("request")
                .latitude(0.2F)
                .longitude(-2F)
                .build();
        // When
        final ResponseEntity<UUID> result = requestController.createRequest(USER_ID_STRING, requestForm);
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Mockito.verify(requestService, Mockito.only()).createRequest(USER_ID_STRING, requestForm);
    }

    @Test
    void deleteRequest() {
        //Given
        final UUID requestId = UUID.randomUUID();
        // When
        final ResponseEntity<Void> result = requestController.deleteRequest(USER_ID_STRING, requestId);
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        Mockito.verify(requestService, Mockito.only()).deleteRequestById(USER_ID_STRING, requestId);
    }

    @Test
    void updateRequest() {
        //Given
        final UUID requestId = UUID.randomUUID();
        final Request request = new Request(requestId, Type.OneTimeTask, Status.Unfulfilled, 2.4F, -0.9F,
                "address","new description", LocalDateTime.now(), LocalDateTime.now(), false,buildUser(), null);
        // When
        final ResponseEntity<Void> result = requestController.updateRequest(USER_ID_STRING, requestId.toString(), request);
        //Then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Mockito.verify(requestService, Mockito.only()).updateRequestWithUser(USER_ID_STRING, requestId.toString(), request);
    }

    private static User buildUser() {
        return User.builder()
                .id(USER_ID)
                .firstname("Gon")
                .lastname("Freecs")
                .email("gonFreecs@hunter.hxh")
                .role(Role.USER)
                .password("mito")
                .address("île de la baleine")
                .governmentIdentity(null)
                .profilePicture(null)
                .build();
    }
}
