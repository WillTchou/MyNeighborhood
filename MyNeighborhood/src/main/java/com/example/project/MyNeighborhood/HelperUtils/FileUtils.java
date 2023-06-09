package com.example.project.MyNeighborhood.HelperUtils;

import com.example.project.MyNeighborhood.exception.DocumentUnsupportedMediaTypeException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.Deflater;

public class FileUtils {
    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    public static void assertFileType(final List<String> contentTypes, final MultipartFile file) {
        final String contentType = file.getContentType();
        if (!contentTypes.contains(contentType)) {
            final String message = String.format("%s type is not supported", contentType);
            throw new DocumentUnsupportedMediaTypeException(message);
        }
    }
}
