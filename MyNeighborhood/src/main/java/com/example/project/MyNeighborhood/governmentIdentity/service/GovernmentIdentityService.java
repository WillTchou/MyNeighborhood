package com.example.project.MyNeighborhood.governmentIdentity.service;

import com.example.project.MyNeighborhood.governmentIdentity.model.GovernmentIdentity;

import java.util.UUID;

public interface GovernmentIdentityService {

    GovernmentIdentity getGovernmentIdentityById(UUID id);
}
