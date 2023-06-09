package com.example.project.MyNeighborhood.profilePicture.service;

import com.example.project.MyNeighborhood.HelperUtils.FileUtils;
import com.example.project.MyNeighborhood.exception.DocumentUnsupportedMediaTypeException;
import com.example.project.MyNeighborhood.profilePicture.model.ProfilePicture;
import com.example.project.MyNeighborhood.profilePicture.repository.ProfilePictureRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ProfilePictureServiceImplTest {
    @Mock
    private ProfilePictureRepository profilePictureRepository;
    private ProfilePictureServiceImpl profilePictureService;

    @BeforeEach
    void setUp() {
        Assertions.assertThat(profilePictureRepository).isNotNull();
        profilePictureService = new ProfilePictureServiceImpl(profilePictureRepository);
    }

    @Test
    void uploadFile() throws IOException {
        //Given
        final MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes()
        );
        final ProfilePicture profilePicture = getProfilePicture(file);
        //When
        final UUID result = profilePictureService.uploadFile(file);
        //Then
        Assertions.assertThat(result).isEqualTo(profilePicture.getId());
        Mockito.verify(profilePictureRepository, Mockito.only()).save(profilePicture);
    }

    @Test
    void uploadFileWhenMediaTypeNotSupported() throws IOException {
        //Given
        final MockMultipartFile file
                = new MockMultipartFile(
                "file2",
                "hello.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "Im a pdf".getBytes()
        );
        final ProfilePicture profilePicture = getProfilePicture(file);
        final String message = String.format("%s type is not supported", MediaType.APPLICATION_PDF_VALUE);
        //When
        Assertions.assertThatThrownBy(() -> profilePictureService.uploadFile(file))
                .isInstanceOf(DocumentUnsupportedMediaTypeException.class)
                .hasMessage(message);
        //Then
        Mockito.verify(profilePictureRepository, Mockito.never()).save(profilePicture);
    }

    private static ProfilePicture getProfilePicture(final MockMultipartFile file) throws IOException {
        return ProfilePicture.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(FileUtils.compressImage(file.getBytes()))
                .build();
    }
}
