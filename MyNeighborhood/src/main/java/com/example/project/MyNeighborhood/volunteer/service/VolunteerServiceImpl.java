package com.example.project.MyNeighborhood.volunteer.service;

import com.example.project.MyNeighborhood.exception.AlreadyFulfilledTheRequestException;
import com.example.project.MyNeighborhood.exception.EnoughVolunteersException;
import com.example.project.MyNeighborhood.exception.RequestNotFoundException;
import com.example.project.MyNeighborhood.exception.UserDoesNotExistException;
import com.example.project.MyNeighborhood.request.model.Request;
import com.example.project.MyNeighborhood.request.repository.RequestRepository;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.repository.UserRepository;
import com.example.project.MyNeighborhood.volunteer.model.Volunteer;
import com.example.project.MyNeighborhood.volunteer.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.project.MyNeighborhood.HelperUtils.StringUtils.convertStringToUUID;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final VolunteerRepository volunteerRepository;

    public VolunteerServiceImpl(final UserRepository userRepository,
                                final RequestRepository requestRepository,
                                final VolunteerRepository volunteerRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public void createVolunteer(final String userId, final UUID requestId) {
        assertRequestHasNotEnoughVolunteer(requestId);
        assertUserHasNotAlreadyFulfilledTheRequest(userId, requestId);
        final User requester = getUser(userId);
        final Request request = getRequest(requestId);
        final Volunteer volunteer = Volunteer.builder()
                .user(requester)
                .request(request)
                .build();
        volunteerRepository.save(volunteer);
    }

    private Request getRequest(final UUID requestId) {
        return requestRepository.findById(requestId).orElseThrow(RequestNotFoundException::new);
    }

    private User getUser(final String userId) {
        return userRepository.findById(convertStringToUUID(userId)).orElseThrow(UserDoesNotExistException::new);
    }

    private void assertRequestHasNotEnoughVolunteer(final UUID requestId) {
        final int volunteersNumber = volunteerRepository.countVolunteersForRequest(requestId);
        if (volunteersNumber >= 5) {
            throw new EnoughVolunteersException();
        }
    }

    private void assertUserHasNotAlreadyFulfilledTheRequest(final String userId, final UUID requestId) {
        if (volunteerRepository.findVolunteerByUserIdAndRequestId(userId, requestId).isPresent()) {
            throw new AlreadyFulfilledTheRequestException();
        }
    }
}
