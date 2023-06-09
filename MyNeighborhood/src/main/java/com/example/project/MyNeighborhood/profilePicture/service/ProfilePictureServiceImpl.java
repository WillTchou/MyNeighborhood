package com.example.project.MyNeighborhood.profilePicture.service;

import com.example.project.MyNeighborhood.HelperUtils.FileUtils;
import com.example.project.MyNeighborhood.HelperUtils.FileService;
import com.example.project.MyNeighborhood.profilePicture.model.ProfilePicture;
import com.example.project.MyNeighborhood.profilePicture.repository.ProfilePictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.example.project.MyNeighborhood.HelperUtils.FileUtils.assertFileType;

@Service
public class ProfilePictureServiceImpl implements FileService {

    private static final List<String> CONTENT_TYPES = List.of("image/png", "image/jpeg");
    private final ProfilePictureRepository profilePictureRepository;

    public ProfilePictureServiceImpl(final ProfilePictureRepository profilePictureRepository) {
        this.profilePictureRepository = profilePictureRepository;
    }

    public UUID uploadFile(final MultipartFile file) throws IOException {
        assertFileType(CONTENT_TYPES, file);
        final ProfilePicture profilePicture = ProfilePicture.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(FileUtils.compressImage(file.getBytes()))
                .build();
        profilePictureRepository.save(profilePicture);
        return profilePicture.getId();
    }
}
