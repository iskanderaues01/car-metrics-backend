package com.energo.car_metrics.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;

@Service
public class AnalysisService {

    @Value("${app.analysis_linear_by_year}")
    private String analysisLinearByYear;

    @Value("${app.analysis_linear_by_mileage}")
    private String analysisLinearByMileage;

    @Value("${app.analysis_linear_by_engine_volume}")
    private String analysisLinearByEngineVolume;

    @Value("${app.analysis_multiple_linear}")
    private String analysisMultipleLinear;

    @Value("${app.analysis_multiple_dummies}")
    private String analysisMultipleDummies;

    @Value("${app.api_image_k}")
    private String apiImageKey;

    private final RestTemplate restTemplate;

    public AnalysisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> performAnalysis(String analysisType, String fileName) throws IOException {
        String url = determineUrl(analysisType, fileName);
        if (url == null) {
            throw new IllegalArgumentException("Invalid analysis type: " + analysisType);
        }

        // Получаем JSON с анализа
        Map<String, Object> analysisResult = restTemplate.getForObject(url, Map.class);

        // Получаем путь к изображению
        String plotPath = (String) analysisResult.get("PlotPath");

        // Кодируем изображение в Base64
        String base64Image = encodeImageToBase64(plotPath);

        // Заменяем путь к изображению на Base64 строку в итоговом JSON
        analysisResult.put("PlotPath", base64Image);

        return analysisResult;
    }


    private String determineUrl(String analysisType, String fileName) {
        switch (analysisType) {
            case "year":
                return String.format(analysisLinearByYear, fileName);
            case "mileage":
                return String.format(analysisLinearByMileage, fileName);
            case "engine_volume":
                return String.format(analysisLinearByEngineVolume, fileName);
            case "multiple_linear":
                return String.format(analysisMultipleLinear, fileName);
            case "dummies":
                return String.format(analysisMultipleDummies, fileName);
            default:
                return null;
        }
    }

    private String encodeImageToBase64(String imagePath) throws IOException {
        File imageFile = new File(imagePath);

        // Проверяем существование файла
        if (!imageFile.exists()) {
            throw new IOException("Image file not found: " + imagePath);
        }

        // Читаем изображение и преобразуем в Base64
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);

        return base64Image;
    }






}
