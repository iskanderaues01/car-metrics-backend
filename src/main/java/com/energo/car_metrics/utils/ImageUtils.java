package com.energo.car_metrics.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ImageUtils {
    public static String convertImageToBase64(String imagePath) throws IOException {
        File file = new File(imagePath);
        if (!file.exists()) {
            throw new IOException("Файл не найден: " + imagePath);
        }
        byte[] imageBytes = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}