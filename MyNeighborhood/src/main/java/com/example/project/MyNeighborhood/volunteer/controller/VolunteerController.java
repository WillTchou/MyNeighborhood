package com.example.project.MyNeighborhood.volunteer.controller;

import com.example.project.MyNeighborhood.volunteer.model.Volunteer;
import com.example.project.MyNeighborhood.volunteer.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/volunteers")
public class VolunteerController {
    private final VolunteerService volunteerService;

    @Autowired
    public VolunteerController(final VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping(path = "/{requestId}")
    public ResponseEntity<Void> createVolunteer(@RequestHeader(name = "userId") final String userId,
                                                @PathVariable("requestId") final UUID requestId) {
        volunteerService.createVolunteer(userId, requestId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/{requestId}")
    public ResponseEntity<List<Volunteer>> getAllVolunteersByRequest(@PathVariable("requestId") final UUID requestId) {
        return ResponseEntity.ok(volunteerService.getAllVolunteersByRequest(requestId));
    }
}
