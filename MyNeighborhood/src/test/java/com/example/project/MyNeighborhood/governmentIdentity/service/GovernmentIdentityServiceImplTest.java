package com.example.project.MyNeighborhood.governmentIdentity.service;

import com.example.project.MyNeighborhood.HelperUtils.FileUtils;
import com.example.project.MyNeighborhood.exception.DocumentUnsupportedMediaTypeException;
import com.example.project.MyNeighborhood.exception.GovernmentIdentityNotFoundException;
import com.example.project.MyNeighborhood.governmentIdentity.model.GovernmentIdentity;
import com.example.project.MyNeighborhood.governmentIdentity.repository.GovernmentIdentityRepository;
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
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class GovernmentIdentityServiceImplTest {
    private static final UUID ID = UUID.randomUUID();
    @Mock
    private GovernmentIdentityRepository governmentIdentityRepository;
    private GovernmentIdentityServiceImpl governmentIdentityServiceImpl;

    @BeforeEach
    void setUp() {
        Assertions.assertThat(governmentIdentityRepository).isNotNull();
        governmentIdentityServiceImpl = new GovernmentIdentityServiceImpl(governmentIdentityRepository);
    }

    @Test
    void uploadFile() throws IOException {
        //Given
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes()
        );
        final GovernmentIdentity governmentIdentity = getGovernmentIdentity(file);
        //When
        governmentIdentityServiceImpl.uploadFile(file);
        //Then
        Mockito.verify(governmentIdentityRepository, Mockito.only()).save(governmentIdentity);
    }

    @Test
    void uploadFileWhenMediaTypeNotSupported() throws IOException {
        //Given
        final MockMultipartFile file = new MockMultipartFile(
                "file2",
                "hello.pdf",
                MediaType.APPLICATION_XHTML_XML_VALUE,
                "Im a pdf".getBytes()
        );
        final GovernmentIdentity governmentIdentity = getGovernmentIdentity(file);
        final String message = String.format("%s type is not supported", MediaType.APPLICATION_XHTML_XML_VALUE);
        //When
        Assertions.assertThatThrownBy(() -> governmentIdentityServiceImpl.uploadFile(file))
                .isInstanceOf(DocumentUnsupportedMediaTypeException.class)
                .hasMessage(message);
        //Then
        Mockito.verify(governmentIdentityRepository, Mockito.never()).save(governmentIdentity);
    }

    @Test
    void getGovernmentIdentityById() throws IOException {
        //Given
        final UUID id = UUID.randomUUID();
        final MockMultipartFile file = new MockMultipartFile(
                "file3",
                "digimon.pdf",
                MediaType.IMAGE_JPEG_VALUE,
                "Im a digimon image".getBytes()
        );
        final Optional<GovernmentIdentity> optionalGovernmentIdentity = Optional.of(getGovernmentIdentity(file));
        //When
        Mockito.when(governmentIdentityRepository.findById(id)).thenReturn(optionalGovernmentIdentity);
        final GovernmentIdentity result = governmentIdentityServiceImpl.getGovernmentIdentityById(id);
        //Then
        Assertions.assertThat(result).isNotNull()
                .isEqualTo(getGovernmentIdentity(file));
        Mockito.verify(governmentIdentityRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void getGovernmentIdentityByIdWhenIdNotFound() {
        //Given
        final String message = "This government identity doesn't exist";
        //When
        Mockito.when(governmentIdentityRepository.findById(ID)).thenReturn(Optional.empty());
        //Then
        Assertions.assertThatThrownBy(() -> governmentIdentityServiceImpl.getGovernmentIdentityById(ID))
                .isInstanceOf(GovernmentIdentityNotFoundException.class)
                .hasMessage(message);
        Mockito.verify(governmentIdentityRepository, Mockito.times(1)).findById(ID);
    }

    private static GovernmentIdentity getGovernmentIdentity(final MockMultipartFile file) throws IOException {
        return GovernmentIdentity.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(FileUtils.compressImage(file.getBytes()))
                .build();
    }
}
