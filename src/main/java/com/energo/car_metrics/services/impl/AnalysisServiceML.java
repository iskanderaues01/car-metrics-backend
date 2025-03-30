package com.energo.car_metrics.services.impl;

import com.energo.car_metrics.models.EpochAnalysisHistory;
import com.energo.car_metrics.models.FuturePriceAnalysisHistory;
import com.energo.car_metrics.repositories.EpochAnalysisRepository;
import com.energo.car_metrics.repositories.FuturePriceAnalysisRepository;
import com.energo.car_metrics.utils.ImageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisServiceML {

    @Value("${app.analysis_ml_analysis_epochs}")
    private String analysisMLAnalysisEpoch;

    @Value("${app.analysis_ml_future_price}")
    private String analysisMLFuturePrice;

    @Autowired
    private FuturePriceAnalysisRepository futurePriceRepo;

    @Autowired
    private EpochAnalysisRepository epochRepo;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public FuturePriceAnalysisHistory performFuturePriceAnalysis(
            String filename,
            String futureYear,
            String futureMileage,
            String futureEngineVolume,
            String futureFuel,
            String futureTransmission,
            Long userId,
            boolean save) throws Exception {

        String url = String.format(analysisMLFuturePrice,
                filename, futureYear, futureMileage, futureEngineVolume, futureFuel, futureTransmission);

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null) {
            throw new RuntimeException("Пустой ответ от Fast API сервиса");
        }

        // Извлекаем данные ответа
        Map<String, Object> analysisResult = (Map<String, Object>) responseBody.get("analysisResult");
        // Сохраняем только поле explanation
        String explanation = analysisResult != null ? (String) analysisResult.get("explanation") : "";
        String plotPath = (String) responseBody.get("priceDistributionPlot");
        String savedResultPath = (String) responseBody.get("savedResultPath");

        // Конвертация изображения в base64
        String base64Image = "";
        try {
            base64Image = ImageUtils.convertImageToBase64(plotPath);
        } catch (IOException e) {
            System.err.println("Ошибка при конвертации изображения: " + e.getMessage());
        }

        // Создаем объект сущности
        FuturePriceAnalysisHistory history = new FuturePriceAnalysisHistory();
        history.setUserId(userId);
        history.setFileAnalyzed(filename);
        history.setFutureYear(futureYear);
        history.setFutureMileage(futureMileage);
        history.setFutureEngineVolume(futureEngineVolume);
        history.setFutureFuel(futureFuel);
        history.setFutureTransmission(futureTransmission);
        // Сохраняем только explanation
        history.setAnalysisResult(explanation);
        history.setPriceDistributionPlot(base64Image);
        history.setSavedResultPath(savedResultPath);

        if (save) {
            futurePriceRepo.save(history);
        }

        return history;
    }

    public EpochAnalysisHistory performEpochAnalysis(
            String filename,
            int epochs,
            int batchSize,
            Long userId,
            boolean save) throws Exception {

        String url = String.format(analysisMLAnalysisEpoch,
                filename, epochs, batchSize);

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null) {
            throw new RuntimeException("Пустой ответ от Fast API сервиса");
        }

        // Извлекаем данные ответа
        Map<String, Object> analysisResult = (Map<String, Object>) responseBody.get("analysisResult");
        // Сохраняем только explanation
        String explanation = analysisResult != null ? (String) analysisResult.get("explanation") : "";
        String plotPath = (String) responseBody.get("epochTrainingPlot");
        String savedResultPath = (String) responseBody.get("savedResultPath");

        // Конвертация изображения в base64
        String base64Image = "";
        try {
            base64Image = ImageUtils.convertImageToBase64(plotPath);
        } catch (IOException e) {
            System.err.println("Ошибка при конвертации изображения: " + e.getMessage());
        }

        // Создаем объект сущности
        EpochAnalysisHistory history = new EpochAnalysisHistory();
        history.setUserId(userId);
        history.setFileAnalyzed(filename);
        history.setEpochs(epochs);
        history.setBatchSize(batchSize);
        // Сохраняем только explanation
        history.setAnalysisResult(explanation);
        history.setEpochTrainingPlot(base64Image);
        history.setSavedResultPath(savedResultPath);

        if (save) {
            epochRepo.save(history);
        }

        return history;
    }


    @Transactional(readOnly = true)
    public List<FuturePriceAnalysisHistory> getFuturePriceHistory(Long userId) {
        return futurePriceRepo.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<EpochAnalysisHistory> getEpochHistory(Long userId) {
        return epochRepo.findByUserId(userId);
    }
}
