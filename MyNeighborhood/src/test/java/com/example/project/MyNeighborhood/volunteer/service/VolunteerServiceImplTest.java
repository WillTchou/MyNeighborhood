package com.example.project.MyNeighborhood.volunteer.service;

import com.example.project.MyNeighborhood.exception.EnoughVolunteersException;
import com.example.project.MyNeighborhood.exception.RequestNotFoundException;
import com.example.project.MyNeighborhood.exception.UserDoesNotExistException;
import com.example.project.MyNeighborhood.request.model.Request;
import com.example.project.MyNeighborhood.request.model.Status;
import com.example.project.MyNeighborhood.request.model.Type;
import com.example.project.MyNeighborhood.request.repository.RequestRepository;
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
    private VolunteerServiceImpl volunteerServiceImpl;

    @BeforeEach
    void setUp() {
        Assertions.assertThat(requestRepository).isNotNull();
        Assertions.assertThat(userRepository).isNotNull();
        Assertions.assertThat(volunteerRepository).isNotNull();
        volunteerServiceImpl = new VolunteerServiceImpl(userRepository, requestRepository, volunteerRepository);
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
        Mockito.verify(volunteerRepository, Mockito.times(1)).countVolunteersForRequest(REQUEST_ID);
        Mockito.verify(userRepository, Mockito.times(1)).findById(USER_ID);
        Mockito.verify(requestRepository, Mockito.times(1)).findById(REQUEST_ID);
        Mockito.verify(volunteerRepository, Mockito.times(1)).save(volunteer);
    }

    @Test
    void createVolunteerWhenEnoughVolunteers() {
        //Given
        final Request request = getRequest();
        final Volunteer volunteer = getVolunteer(request);
        //When
        Mockito.when(volunteerRepository.countVolunteersForRequest(REQUEST_ID)).thenReturn(5);
        Assertions.assertThatThrownBy(() -> volunteerServiceImpl.createVolunteer(USER_ID.toString(), REQUEST_ID))
                .isInstanceOf(EnoughVolunteersException.class);
        //Then
        Mockito.verify(volunteerRepository, Mockito.only()).countVolunteersForRequest(REQUEST_ID);
        Mockito.verify(userRepository, Mockito.never()).findById(USER_ID);
        Mockito.verify(requestRepository, Mockito.never()).findById(REQUEST_ID);
        Mockito.verify(volunteerRepository, Mockito.never()).save(volunteer);
    }

    @Test
    void createVolunteerRequesterDoesNotExist() {
        //Given
        final Request request = getRequest();
        final Volunteer volunteer = getVolunteer(request);
        //When
        Mockito.when(volunteerRepository.countVolunteersForRequest(REQUEST_ID)).thenReturn(2);
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> volunteerServiceImpl.createVolunteer(USER_ID.toString(), REQUEST_ID))
                .isInstanceOf(UserDoesNotExistException.class);
        Mockito.verify(volunteerRepository, Mockito.times(1)).countVolunteersForRequest(REQUEST_ID);
        Mockito.verify(userRepository, Mockito.times(1)).findById(USER_ID);
        Mockito.verify(requestRepository, Mockito.never()).findById(REQUEST_ID);
        Mockito.verify(volunteerRepository, Mockito.never()).save(volunteer);
    }

    @Test
    void createVolunteerRequestDoesNotExist() {
        //Given
        final Request request = getRequest();
        final Volunteer volunteer = getVolunteer(request);
        //When
        Mockito.when(volunteerRepository.countVolunteersForRequest(REQUEST_ID)).thenReturn(1);
        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(buildUser()));
        Mockito.when(requestRepository.findById(REQUEST_ID)).thenReturn(Optional.empty());
        //Then
        Assertions.assertThatThrownBy(() -> volunteerServiceImpl.createVolunteer(USER_ID.toString(), REQUEST_ID))
                .isInstanceOf(RequestNotFoundException.class);
        Mockito.verify(volunteerRepository, Mockito.times(1)).countVolunteersForRequest(REQUEST_ID);
        Mockito.verify(userRepository, Mockito.times(1)).findById(USER_ID);
        Mockito.verify(requestRepository, Mockito.times(1)).findById(REQUEST_ID);
        Mockito.verify(volunteerRepository, Mockito.never()).save(volunteer);
    }

    private static Request getRequest() {
        return new Request(Type.OneTimeTask, Status.Unfulfilled, 2.4F, -0.9F,
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
