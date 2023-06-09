package com.example.project.MyNeighborhood.request.repository;

import com.example.project.MyNeighborhood.request.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {
    @Query("SELECT r FROM Request r WHERE r.requester.id = :userId")
    List<Request> findAllRequestsForUser(@Param("userId") final UUID userId);

    @Query("SELECT r FROM Request r WHERE r.status = Unfulfilled")
    List<Request> findAllUnfulfilledRequests();

    @Query("SELECT r FROM Request r WHERE r.requester.id = :userId AND r.id = :requestId")
    Optional<Request> findRequestByIdForUser(@Param("userId") final UUID userId,
                                             @Param("requestId") final UUID requestId);

    @Query("SELECT COUNT(r) FROM Request r WHERE r.status = Unfulfilled")
    int countUnfulfilledRequests();
}
