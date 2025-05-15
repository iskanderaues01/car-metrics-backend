package com.energo.car_metrics.services.impl;

import com.energo.car_metrics.models.AnalysisHistory;
import com.energo.car_metrics.repositories.AnalysisHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AnalysisHistoryService {

    private final AnalysisHistoryRepository analysisHistoryRepository;

    public AnalysisHistoryService(AnalysisHistoryRepository analysisHistoryRepository) {
        this.analysisHistoryRepository = analysisHistoryRepository;
    }

    @Transactional
    public AnalysisHistory saveAnalysisHistory(Long userId, Map<String, Object> analysisData) {
        AnalysisHistory history = new AnalysisHistory();
        history.setUserId(userId);
        history.setMessage((String) analysisData.get("message"));
        history.setFileAnalyzed((String) analysisData.get("fileAnalyzed"));
        history.setCountRecords((Integer) analysisData.get("countRecords"));
        history.setMse(String.valueOf(analysisData.get("mse")));
        history.setRSquared(String.valueOf(analysisData.get("rSquared")));
        history.setEquation((String) analysisData.get("equation"));
        history.setImgBase64((String) analysisData.get("imgBase64"));
        history.setCarBrand((String) analysisData.get("carBrand"));
        history.setCarModel((String) analysisData.get("carModel"));

        // Преобразуем DummyFeatures в строку, если они есть
        Object dummyFeatures = analysisData.get("dummyFeatures");
        if (dummyFeatures != null) {
            history.setDummyFeatures(dummyFeatures.toString());
        }

        history.setCreatedAt(LocalDateTime.now());

        return analysisHistoryRepository.save(history);
    }
}
