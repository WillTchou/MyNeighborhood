package com.example.project.MyNeighborhood.profilePicture.controller;

import com.example.project.MyNeighborhood.profilePicture.service.ProfilePictureServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profilePicture")
public class ProfilePictureController {
    private final ProfilePictureServiceImpl profilePictureServiceImpl;

    public ProfilePictureController(final ProfilePictureServiceImpl profilePictureServiceImpl) {
        this.profilePictureServiceImpl = profilePictureServiceImpl;
    }

    @PostMapping
    public ResponseEntity<UUID> uploadImage(@RequestParam("image") final MultipartFile file) throws IOException {
        return ResponseEntity.ok(profilePictureServiceImpl.uploadFile(file));
    }
}
