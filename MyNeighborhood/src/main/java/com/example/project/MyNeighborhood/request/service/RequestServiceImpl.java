package com.example.project.MyNeighborhood.request.service;

import com.example.project.MyNeighborhood.exception.RequestNotFoundException;
import com.example.project.MyNeighborhood.exception.UserDoesNotExistException;
import com.example.project.MyNeighborhood.request.model.Request;
import com.example.project.MyNeighborhood.request.model.RequestDTO;
import com.example.project.MyNeighborhood.request.model.RequestForm;
import com.example.project.MyNeighborhood.request.model.Status;
import com.example.project.MyNeighborhood.request.repository.RequestRepository;
import com.example.project.MyNeighborhood.user.model.User;
import com.example.project.MyNeighborhood.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.project.MyNeighborhood.HelperUtils.StringUtils.convertStringToUUID;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestDTOMapper requestDTOMapper;

    @Autowired
    public RequestServiceImpl(final RequestRepository requestRepository, final UserRepository userRepository,
                              final RequestDTOMapper requestDTOMapper) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.requestDTOMapper = requestDTOMapper;
    }

    @Override
    public List<RequestDTO> getAllRequestsForUser(final String userId) {
        return requestRepository.findAllRequestsForUser(convertStringToUUID(userId))
                .stream()
                .map(requestDTOMapper)
                .collect(Collectors.toList());
    }
    @Override
    public List<RequestDTO> getAllUnfulfilledRequests() {
        return requestRepository.findAllUnfulfilledRequests()
                .stream()
                .map(requestDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public UUID createRequest(final String userId, final RequestForm requestForm) {
        final User requester = userRepository.findById(convertStringToUUID(userId))
                .orElseThrow(UserDoesNotExistException::new);
        final Request request = new Request(
                requestForm.getType(),
                requestForm.getLatitude(),
                requestForm.getLongitude(),
                requestForm.getDescription(),
                requester);
        requestRepository.save(request);
        return request.getId();
    }

    @Override
    public void deleteRequestById(final String userId, final UUID requestId) {
        assertRequestExist(requestId);
        assertUserOwnsRequest(userId, requestId);
        requestRepository.deleteById(requestId);
    }

    @Override
    @Transactional
    public void updateRequest(final String userId,final UUID requestId, final Request updatedRequest) {
        assertRequestExist(requestId);
        getRequestByIdForUser(userId, requestId).ifPresent(requestToUpdate -> {
            setRequestField(requestToUpdate, updatedRequest);
            requestRepository.save(requestToUpdate);
        });
    }

    @Override
    public int getUnfulfilledRequestsNumber() {
        return requestRepository.countUnfulfilledRequests();
    }

    private void assertRequestExist(final UUID requestId) {
        final boolean isRequestExist = requestRepository.existsById(requestId);
        if (!isRequestExist) {
            throw new RequestNotFoundException();
        }
    }

    private void assertUserOwnsRequest(final String userId, final UUID requestId) {
        if (getRequestByIdForUser(userId, requestId).isEmpty()) {
            throw new RequestNotFoundException();
        }
    }


    private Optional<Request> getRequestByIdForUser(final String userId, final UUID requestId) {
        return requestRepository.findRequestByIdForUser(convertStringToUUID(userId), requestId);
    }

    private void setRequestField(final Request requestToUpdate, final Request updatedRequest) {
        requestToUpdate.setType(updatedRequest.getType());
        requestToUpdate.setDescription(updatedRequest.getDescription());
        requestToUpdate.setLatitude(updatedRequest.getLatitude());
        requestToUpdate.setLongitude(updatedRequest.getLongitude());
        requestToUpdate.setStatus(updatedRequest.getStatus());
        if (requestToUpdate.getStatus() == Status.Fulfilled) {
            requestToUpdate.setFulfilledDate(LocalDateTime.now());
        }
    }
}
