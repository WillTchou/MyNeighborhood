package com.example.project.MyNeighborhood.governmentIdentity.repository;

import com.example.project.MyNeighborhood.governmentIdentity.model.GovernmentIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GovernmentIdentityRepository extends JpaRepository<GovernmentIdentity, UUID> {
}
