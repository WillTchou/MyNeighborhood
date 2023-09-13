package com.example.project.MyNeighborhood.volunteer.service;

import com.example.project.MyNeighborhood.exception.AlreadyFulfilledTheRequestException;
import com.example.project.MyNeighborhood.exception.EnoughVolunteersException;
import com.example.project.MyNeighborhood.exception.RequestNotFoundException;
import com.example.project.MyNeighborhood.exception.UserDoesNotExistException;
import com.example.project.MyNeighborhood.request.model.Request;
import com.example.project.MyNeighborhood.request.model.Status;
import com.example.project.MyNeighborhood.request.model.Type;
import com.example.project.MyNeighborhood.request.repository.RequestRepository;
import com.example.project.MyNeighborhood.request.service.RequestService;
import com.example.project.MyNeighborhood.user.model.Role;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.repository.UserRepository;
import com.example.project.MyNeighborhood.volunteer.model.Volunteer;
import com.example.project.MyNeighborhood.volunteer.repository.VolunteerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceImplTest {

    private static final UUID REQUEST_ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VolunteerRepository volunteerRepository;
    @Mock
    private RequestService requestService;
    private VolunteerServiceImpl volunteerServiceImpl;

    @BeforeEach
    void setUp() {
        Assertions.assertThat(requestRepository).isNotNull();
        Assertions.assertThat(userRepository).isNotNull();
        Assertions.assertThat(volunteerRepository).isNotNull();
        Assertions.assertThat(requestService).isNotNull();
        volunteerServiceImpl = new VolunteerServiceImpl(userRepository, requestRepository, volunteerRepository, requestService);
    }

    @Test
    void createVolunteer() {
        //Given
        final Request request = getRequest();
        final Volunteer volunteer = getVolunteer(request);
        //When
        Mockito.when(volunteerRepository.countVolunteersForRequest(REQUEST_ID)).thenReturn(3);
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(buildUser()));
        Mockito.when(requestRepository.findById(REQUEST_ID)).thenReturn(Optional.of(request));
        volunteerServiceImpl.createVolunteer(USER_ID.toString(), REQUEST_ID);
        //Then
        Mockito.verify(volunteerRepository, Mockito.times(2)).countVolunteersForRequest(REQUEST_ID);
        Mockito.verify(userRepository, Mockito.times(1)).findById(USER_ID);
        Mockito.verify(requestRepository, Mockito.times(1)).findById(REQUEST_ID);
        Mockito.verify(volunteerRepository, Mockito.times(1)).save(volunteer);
    }

    private static Request getRequest() {
        return new Request(Type.OneTimeTask, Status.Unfulfilled, 2.4F, -0.9F,"address",
                "new description", buildUser());
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

    private static Volunteer getVolunteer(final Request request) {
        return Volunteer.builder()
                .request(request)
                .user(buildUser())
                .build();
    }
}
