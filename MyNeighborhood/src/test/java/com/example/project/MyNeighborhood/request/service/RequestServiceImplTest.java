package com.example.project.MyNeighborhood.request.service;

import com.example.project.MyNeighborhood.exception.RequestNotFoundException;
import com.example.project.MyNeighborhood.exception.UserDoesNotExistException;
import com.example.project.MyNeighborhood.request.model.*;
import com.example.project.MyNeighborhood.request.repository.RequestRepository;
import com.example.project.MyNeighborhood.user.model.Role;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    private static final UUID REQUEST_ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();
    private static final User USER = buildUser();
    private static final Type MATERIAL_NEED = Type.MaterialNeed;
    private static final Request REQUEST = buildRequest();
    private static final float LATITUDE = -1.2F;
    private static final float LONGITUDE = 2.3F;
    private static final String DESCRIPTION = "description";
    private static final String ADDRESS = "address";
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    private final RequestDTOMapper requestDTOMapper = new RequestDTOMapper();
    private RequestServiceImpl requestServiceImpl;

    @BeforeEach
    void setUp() {
        Assertions.assertThat(requestRepository).isNotNull();
        Assertions.assertThat(userRepository).isNotNull();
        requestServiceImpl = new RequestServiceImpl(requestRepository, userRepository, requestDTOMapper);
    }

    @Test
    void getAllRequestsForUser() {
        //Given
        final List<Request> requests = List.of(REQUEST);
        final RequestDTO requestDTO = requestDTOMapper.apply(requests.get(0));
        //When
        Mockito.when(requestRepository.findAllRequestsForUser(USER_ID)).thenReturn(requests);
        final List<RequestDTO> result = requestServiceImpl.getAllRequestsForUser(USER_ID.toString());
        //Then
        Assertions.assertThat(result).isNotNull()
                .hasSize(1);
        Assertions.assertThat(result.get(0)).isEqualTo(requestDTO);
    }

    @Test
    void createRequest() {
        //Given
        final RequestForm requestForm = new RequestForm(MATERIAL_NEED, LATITUDE, LONGITUDE, ADDRESS, DESCRIPTION);
        //When
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));
        requestServiceImpl.createRequest(USER_ID.toString(), requestForm);
        //Then
        Mockito.verify(userRepository, Mockito.times(1)).findById(USER_ID);
    }

    @Test
    void createRequestWhenUserDoesNotExist() {
        //Given
        final RequestForm requestForm = new RequestForm(MATERIAL_NEED, LATITUDE, LONGITUDE, ADDRESS,DESCRIPTION);
        //When
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        //Then
        Assertions.assertThatThrownBy(() -> requestServiceImpl.createRequest(USER_ID.toString(), requestForm))
                .isInstanceOf(UserDoesNotExistException.class);
        Mockito.verify(userRepository, Mockito.times(1)).findById(USER_ID);
        Mockito.verify(requestRepository, Mockito.never()).save(REQUEST);
    }

    @Test
    void deleteRequestById() {
        //Given && When
        Mockito.when(requestRepository.existsById(REQUEST_ID)).thenReturn(true);
        Mockito.when(requestRepository.findRequestByIdForUser(USER_ID, REQUEST_ID)).thenReturn(Optional.of(REQUEST));
        requestServiceImpl.deleteRequestById(USER_ID.toString(), REQUEST_ID);
        //Then
        Mockito.verify(requestRepository, Mockito.times(1)).existsById(REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.times(1)).findRequestByIdForUser(USER_ID, REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.times(1)).deleteById(REQUEST_ID);
    }

    @Test
    void deleteRequestByIdWhenRequestDoesNotExist() {
        //Given && When
        Mockito.when(requestRepository.existsById(REQUEST_ID)).thenReturn(false);
        Assertions.assertThatThrownBy(() -> requestServiceImpl.deleteRequestById(USER_ID.toString(), REQUEST_ID))
                .isInstanceOf(RequestNotFoundException.class);
        //Then
        Mockito.verify(requestRepository, Mockito.only()).existsById(REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.never()).findRequestByIdForUser(USER_ID, REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.never()).deleteById(REQUEST_ID);
    }


    @Test
    void deleteRequestByIdWhenUserDoesNotOwnRequest() {
        //Given && When
        Mockito.when(requestRepository.existsById(REQUEST_ID)).thenReturn(true);
        Mockito.when(requestRepository.findRequestByIdForUser(USER_ID, REQUEST_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> requestServiceImpl.deleteRequestById(USER_ID.toString(), REQUEST_ID))
                .isInstanceOf(RequestNotFoundException.class);
        //Then
        Mockito.verify(requestRepository, Mockito.times(1)).existsById(REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.times(1)).findRequestByIdForUser(USER_ID, REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.never()).deleteById(REQUEST_ID);
    }

    @Test
    void updateRequest() {
        //Given
        final Request newRequest = getNewRequest();
        //When
        Mockito.when(requestRepository.existsById(REQUEST_ID)).thenReturn(true);
        Mockito.when(requestRepository.findRequestByIdForUser(USER_ID, REQUEST_ID)).thenReturn(Optional.of(REQUEST));
        requestServiceImpl.updateRequestWithUser(USER_ID.toString(), REQUEST_ID.toString(), newRequest);
        //Then
        assertionsRequestUpdate(newRequest);
    }

    @Test
    void updateRequestWhenRequestFulfilled() {
        //Given
        final Request newRequest = new Request(Type.OneTimeTask, Status.Fulfilled, 2.4F, -0.9F,
                ADDRESS,"new description", USER);
        //When
        Mockito.when(requestRepository.existsById(REQUEST_ID)).thenReturn(true);
        Mockito.when(requestRepository.findRequestByIdForUser(USER_ID, REQUEST_ID)).thenReturn(Optional.of(REQUEST));
        requestServiceImpl.updateRequestWithUser(USER_ID.toString(), REQUEST_ID.toString(), newRequest);
        //Then
        Assertions.assertThat(REQUEST.getFulfilledDate()).isNotNull();
        assertionsRequestUpdate(newRequest);
    }

    @Test
    void updateRequestByIdWhenRequestDoesNotExist() {
        //Given
        final Request newRequest = getNewRequest();
        //When
        Mockito.when(requestRepository.existsById(REQUEST_ID)).thenReturn(false);
        Assertions.assertThatThrownBy(() -> requestServiceImpl.updateRequestWithUser(USER_ID.toString(),
                        REQUEST_ID.toString(), newRequest))
                .isInstanceOf(RequestNotFoundException.class);
        //Then
        Mockito.verify(requestRepository, Mockito.only()).existsById(REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.never()).findRequestByIdForUser(USER_ID, REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.never()).save(REQUEST);
    }

    @Test
    void updateRequestByIdWhenUserDoesNotOwnRequest() {
        //Given
        final Request newRequest = getNewRequest();
        //When
        Mockito.when(requestRepository.existsById(REQUEST_ID)).thenReturn(true);
        Mockito.when(requestRepository.findRequestByIdForUser(USER_ID, REQUEST_ID)).thenReturn(Optional.empty());
        requestServiceImpl.updateRequestWithUser(USER_ID.toString(), REQUEST_ID.toString(), newRequest);
        //Then
        Mockito.verify(requestRepository, Mockito.times(1)).existsById(REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.times(1)).findRequestByIdForUser(USER_ID, REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.never()).save(REQUEST);
    }

    @Test
    void getUnfulfilledRequestsNumber() {
        //Given && When
        Mockito.when(requestRepository.countUnfulfilledRequests()).thenReturn(2);
        final int result = requestServiceImpl.getUnfulfilledRequestsNumber();
        //Then
        Assertions.assertThat(result).isEqualTo(2);
        Mockito.verify(requestRepository, Mockito.only()).countUnfulfilledRequests();
    }

    private static Request buildRequest() {
        return new Request(MATERIAL_NEED,
                LATITUDE,
                LONGITUDE,
                ADDRESS,
                DESCRIPTION,
                USER);
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

    private static Request getNewRequest() {
        return new Request(Type.OneTimeTask, Status.Unfulfilled, 2.4F, -0.9F, ADDRESS,
                "new description", USER);
    }

    private void assertionsRequestUpdate(Request newRequest) {
        Assertions.assertThat(REQUEST).isNotNull();
        Assertions.assertThat(REQUEST.getType()).isEqualTo(newRequest.getType());
        Assertions.assertThat(REQUEST.getStatus()).isEqualTo(newRequest.getStatus());
        Assertions.assertThat(REQUEST.getDescription()).isEqualTo(newRequest.getDescription());
        Assertions.assertThat(REQUEST.getLatitude()).isEqualTo(newRequest.getLatitude());
        Assertions.assertThat(REQUEST.getLongitude()).isEqualTo(newRequest.getLongitude());
        Mockito.verify(requestRepository, Mockito.times(1)).existsById(REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.times(1)).findRequestByIdForUser(USER_ID,
                REQUEST_ID);
        Mockito.verify(requestRepository, Mockito.times(1)).save(REQUEST);
    }
}
