package com.example.project.MyNeighborhood.request.controller;

import com.example.project.MyNeighborhood.request.model.Request;
import com.example.project.MyNeighborhood.request.model.RequestDTO;
import com.example.project.MyNeighborhood.request.model.RequestForm;
import com.example.project.MyNeighborhood.request.service.RequestService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requests")
public class RequestController {
    private final RequestService requestService;

    public RequestController(final RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<RequestDTO>> getAllRequestsForUser(@RequestHeader(name = "userId") final String userId) {
        return ResponseEntity.ok(requestService.getAllRequestsForUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<RequestDTO>> getAllUnfulfilledRequests() {
        return ResponseEntity.ok(requestService.getAllUnfulfilledRequests());
    }

    @GetMapping(path = "/number")
    public ResponseEntity<Integer> getUnfulfilledRequestsNumber() {
        return ResponseEntity.ok(requestService.getUnfulfilledRequestsNumber());
    }

    @PostMapping
    public ResponseEntity<UUID> createRequest(@RequestHeader(name = "userId") final String userId,
                                              @RequestBody @NotNull final RequestForm requestForm) {
        return ResponseEntity.ok(requestService.createRequest(userId, requestForm));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteRequest(@RequestHeader(name = "userId") final String userId,
                                              @PathVariable("requestId") final UUID requestId) {
        requestService.deleteRequestById(userId, requestId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateRequest(@RequestHeader(name = "userId") final String userId,
                                              @PathVariable("requestId") final UUID requestId,
                                              @RequestBody @NotNull final Request request) {
        requestService.updateRequest(userId, requestId, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
