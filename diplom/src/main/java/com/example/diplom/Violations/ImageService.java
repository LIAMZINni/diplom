package com.example.diplom.Violations;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class ImageService {
    public String imageToBase64(byte[] imageData) {
        if (imageData == null) return "";
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageData);
    }

    public String loadImageAsBase64(String path) throws IOException {
        byte[] fileContent = FileCopyUtils.copyToByteArray(new ClassPathResource(path).getInputStream());
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(fileContent);
    }

    public String generateQrCode(String text) {
        return "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=" +
                URLEncoder.encode(text, StandardCharsets.UTF_8);
    }
}
