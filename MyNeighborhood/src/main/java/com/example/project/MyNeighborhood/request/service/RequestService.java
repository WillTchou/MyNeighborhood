package com.example.project.MyNeighborhood.request.service;

import com.example.project.MyNeighborhood.request.model.Request;
import com.example.project.MyNeighborhood.request.model.RequestDTO;
import com.example.project.MyNeighborhood.request.model.RequestForm;

import java.util.List;
import java.util.UUID;

public interface RequestService {
    List<RequestDTO> getAllRequestsForUser(String userId);
    List<RequestDTO> getAllUnfulfilledRequests(String userId);
    int getUnfulfilledRequestsNumber();
    UUID createRequest(String userId, RequestForm requestForm);
    void deleteRequestById(String userId, UUID requestId);
    void updateRequest(UUID requestId, Request updatedRequest);
    void updateRequestWithUser(String userId, String requestId, Request updatedRequest);
}
