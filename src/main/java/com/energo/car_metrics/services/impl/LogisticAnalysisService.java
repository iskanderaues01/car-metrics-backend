package com.energo.car_metrics.services.impl;

import com.energo.car_metrics.models.LogisticAnalysisHistory;
import com.energo.car_metrics.repositories.LogisticAnalysisHistoryRepository;
import com.energo.car_metrics.utils.ImageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogisticAnalysisService {

    private final RestTemplate restTemplate;
    private final LogisticAnalysisHistoryRepository repository;
    private final ObjectMapper objectMapper;

    @Value("${app.analysis_logistic}")
    private String ANALYSIS_URL;

    public LogisticAnalysisHistory performAnalysis(String filename, Integer priceThreshold, Long userId, boolean save) throws Exception {
        String url = String.format(ANALYSIS_URL, filename, priceThreshold);

        // Отправляем запрос на внешний сервер
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null) throw new RuntimeException("Ответ от сервера пуст.");

        // Разбор JSON-ответа
        Map<String, Object> analysisResult = (Map<String, Object>) responseBody.get("analysisResult");
        String confusionMatrixPlotPath = (String) responseBody.get("confusionMatrixPlotPath");

        // ✅ Читаем изображение, конвертируем в base64
        String base64Image = "";
        try {
            base64Image = ImageUtils.convertImageToBase64(confusionMatrixPlotPath);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении изображения: " + e.getMessage());
        }

        LogisticAnalysisHistory history = new LogisticAnalysisHistory();
        history.setUserId(userId);
        history.setFileAnalyzed((String) analysisResult.get("fileAnalyzed"));
        history.setPriceThreshold((Integer) analysisResult.get("priceThreshold"));
        history.setAccuracy((Double) analysisResult.get("accuracy"));
        history.setConfusionMatrix(objectMapper.writeValueAsString(analysisResult.get("confusionMatrix")));
        history.setCoefficients(objectMapper.writeValueAsString(analysisResult.get("coefficients")));
        history.setIntercept(objectMapper.writeValueAsString(analysisResult.get("intercept")));
        history.setFeatures(objectMapper.writeValueAsString(analysisResult.get("features")));
        history.setExplanation((String) analysisResult.get("explanation"));
        history.setImgBase64(base64Image);  // ✅ Сохраняем изображение в base64
        history.setSavedResultPath((String) responseBody.get("savedResultPath"));

        // ✅ Сохраняем в БД, если save=true
        if (save) {
            repository.save(history);
        }

        return history;
    }
}
