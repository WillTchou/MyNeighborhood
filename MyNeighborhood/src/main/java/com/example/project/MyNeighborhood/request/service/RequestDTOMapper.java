package com.example.project.MyNeighborhood.request.service;

import com.example.project.MyNeighborhood.request.model.Request;
import com.example.project.MyNeighborhood.request.model.RequestDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public final class RequestDTOMapper implements Function<Request, RequestDTO> {
    @Override
    public RequestDTO apply(final Request request) {
        return new RequestDTO(request.getId(), request.getType(), request.getStatus(),
                request.getLatitude(), request.getLongitude(), request.getDescription(), request.getCreationDate(),
                request.getFulfilledDate(), request.getRequester());
    }
}
