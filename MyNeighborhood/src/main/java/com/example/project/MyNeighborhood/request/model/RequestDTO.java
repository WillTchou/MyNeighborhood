package com.example.project.MyNeighborhood.request.model;

import com.example.project.MyNeighborhood.user.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestDTO(UUID id, Type type, Status status, Float latitude, Float longitude, String address,
                         String description, LocalDateTime creationDate, LocalDateTime fulfilledDate,
                         boolean displayed, User requester) {
}
