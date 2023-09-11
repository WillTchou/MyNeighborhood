package com.example.project.MyNeighborhood.volunteer.service;

import com.example.project.MyNeighborhood.volunteer.model.Volunteer;

import java.util.List;
import java.util.UUID;

public interface VolunteerService {
    void createVolunteer(String userId, UUID requestId);
    List<Volunteer> getAllVolunteersByRequest(UUID requestId);
}
