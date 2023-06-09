package com.example.project.MyNeighborhood.volunteer.repository;

import com.example.project.MyNeighborhood.volunteer.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    @Query("SELECT COUNT(DISTINCT v.user.id) FROM Volunteer v WHERE v.request.id = :requestId")
    int countVolunteersForRequest(@Param("requestId") final UUID requestId);
}
