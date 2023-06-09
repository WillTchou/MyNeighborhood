package com.example.project.MyNeighborhood.governmentIdentity.service;

import com.example.project.MyNeighborhood.HelperUtils.FileUtils;
import com.example.project.MyNeighborhood.HelperUtils.FileService;
import com.example.project.MyNeighborhood.exception.GovernmentIdentityNotFoundException;
import com.example.project.MyNeighborhood.governmentIdentity.model.GovernmentIdentity;
import com.example.project.MyNeighborhood.governmentIdentity.repository.GovernmentIdentityRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.example.project.MyNeighborhood.HelperUtils.FileUtils.assertFileType;


@Service
public class GovernmentIdentityServiceImpl implements FileService, GovernmentIdentityService {
    private static final List<String> CONTENT_TYPES = List.of("image/png", "image/jpeg", "application/pdf");

    private final GovernmentIdentityRepository governmentIdentityRepository;

    public GovernmentIdentityServiceImpl(final GovernmentIdentityRepository governmentIdentityRepository) {
        this.governmentIdentityRepository = governmentIdentityRepository;
    }

    public UUID uploadFile(final MultipartFile file) throws IOException {
        assertFileType(CONTENT_TYPES, file);
        final GovernmentIdentity governmentIdentity = GovernmentIdentity.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(FileUtils.compressImage(file.getBytes()))
                .build();
        governmentIdentityRepository.save(governmentIdentity);
        return governmentIdentity.getId();
    }

    @Override
    public GovernmentIdentity getGovernmentIdentityById(final UUID id) {
        return governmentIdentityRepository.findById(id).orElseThrow(GovernmentIdentityNotFoundException::new);
    }
}
