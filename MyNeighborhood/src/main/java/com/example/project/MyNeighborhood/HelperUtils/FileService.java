package com.example.project.MyNeighborhood.HelperUtils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface FileService {
    UUID uploadFile(MultipartFile file) throws IOException;
}
