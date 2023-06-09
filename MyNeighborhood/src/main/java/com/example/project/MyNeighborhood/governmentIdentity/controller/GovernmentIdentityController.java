package com.example.project.MyNeighborhood.governmentIdentity.controller;

import com.example.project.MyNeighborhood.governmentIdentity.service.GovernmentIdentityServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/governmentIdentity")
public class GovernmentIdentityController {
    private final GovernmentIdentityServiceImpl governmentIdentityServiceImpl;

    public GovernmentIdentityController(final GovernmentIdentityServiceImpl governmentIdentityServiceImpl) {
        this.governmentIdentityServiceImpl = governmentIdentityServiceImpl;
    }

    @PostMapping
    public ResponseEntity<UUID> uploadDocument(@RequestParam("document") final MultipartFile file) throws IOException {
        return ResponseEntity.ok(governmentIdentityServiceImpl.uploadFile(file));
    }
}
