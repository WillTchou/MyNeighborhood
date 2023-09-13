package com.example.project.MyNeighborhood.volunteer.service;

import com.example.project.MyNeighborhood.exception.AlreadyFulfilledTheRequestException;
import com.example.project.MyNeighborhood.exception.EnoughVolunteersException;
import com.example.project.MyNeighborhood.exception.RequestNotFoundException;
import com.example.project.MyNeighborhood.exception.UserDoesNotExistException;
import com.example.project.MyNeighborhood.request.model.Request;
import com.example.project.MyNeighborhood.request.model.Status;
import com.example.project.MyNeighborhood.request.repository.RequestRepository;
import com.example.project.MyNeighborhood.request.service.RequestService;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.repository.UserRepository;
import com.example.project.MyNeighborhood.volunteer.model.Volunteer;
import com.example.project.MyNeighborhood.volunteer.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.project.MyNeighborhood.HelperUtils.StringUtils.convertStringToUUID;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final VolunteerRepository volunteerRepository;
    private final RequestService requestService;

    public VolunteerServiceImpl(final UserRepository userRepository,
                                final RequestRepository requestRepository,
                                final VolunteerRepository volunteerRepository,
                                final RequestService requestService) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.volunteerRepository = volunteerRepository;
        this.requestService = requestService;
    }

    @Override
    public void createVolunteer(final String userId, final UUID requestId) {
        assertUserHasNotAlreadyFulfilledTheRequest(userId, requestId);
        assertRequestHasNotEnoughVolunteers(requestId);
        final User requester = getUser(userId);
        final Request request = getRequest(requestId);
        final Volunteer volunteer = Volunteer.builder()
                .user(requester)
                .request(request)
                .build();
        volunteerRepository.save(volunteer);
        updateRequestWhenVolunteerLimitationIsReached(requestId, request);
    }

    @Override
    public List<Volunteer> getAllVolunteersByRequest(final UUID requestId) {
        return volunteerRepository.findAllVolunteersByRequest(requestId);
    }

    private void updateRequestWhenVolunteerLimitationIsReached(final UUID requestId, final Request request) {
        if (volunteerRepository.countVolunteersForRequest(requestId) == 5) {
            final Request fulfilledRequest = new Request(requestId, request.getType(), Status.Fulfilled,
                    request.getLatitude(), request.getLongitude(), request.getAddress(), request.getDescription(),
                    request.getCreationDate(), LocalDateTime.now(), false, request.getRequester(),
                    request.getVolunteers());
            requestService.updateRequest(request.getId(), fulfilledRequest);
        }
    }

    private Request getRequest(final UUID requestId) {
        return requestRepository.findById(requestId).orElseThrow(RequestNotFoundException::new);
    }

    private User getUser(final String userId) {
        return userRepository.findById(convertStringToUUID(userId)).orElseThrow(UserDoesNotExistException::new);
    }

    private void assertUserHasNotAlreadyFulfilledTheRequest(final String userId, final UUID requestId) {
        final Optional<Volunteer> optionalVolunteer = volunteerRepository.findVolunteerByUserIdAndRequestId(
                convertStringToUUID(userId),
                requestId);
        if (optionalVolunteer.isPresent()) {
            throw new AlreadyFulfilledTheRequestException();
        }
    }

    private void assertRequestHasNotEnoughVolunteers(final UUID requestId) {
        if (volunteerRepository.countVolunteersForRequest(requestId) >= 5) {
            throw new EnoughVolunteersException();
        }
    }
}
