package com.energo.car_metrics.controllers;

import com.energo.car_metrics.models.EpochAnalysisHistory;
import com.energo.car_metrics.models.FuturePriceAnalysisHistory;
import com.energo.car_metrics.services.impl.AnalysisService;
import com.energo.car_metrics.services.impl.AnalysisServiceML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnalysisController {
    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private AnalysisServiceML analysisServiceML;

    @GetMapping("/perform-analysis")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public Map<String, Object> performAnalysis(
            @RequestParam String analysisType,
            @RequestParam String fileName) {
        try {
            return analysisService.performAnalysis(analysisType, fileName);
        } catch (IOException e) {
            throw new RuntimeException("Error processing analysis or uploading image", e);
        }
    }

    @GetMapping("/future-price-analysis")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public ResponseEntity<FuturePriceAnalysisHistory> performFuturePriceAnalysis(
            @RequestParam String filename,
            @RequestParam String futureYear,
            @RequestParam String futureMileage,
            @RequestParam String futureEngineVolume,
            @RequestParam String futureFuel,
            @RequestParam String futureTransmission,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean save) {
        try {
            FuturePriceAnalysisHistory history = analysisServiceML.performFuturePriceAnalysis(
                    filename, futureYear, futureMileage, futureEngineVolume, futureFuel, futureTransmission, userId, save);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/epochs-analysis")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public ResponseEntity<EpochAnalysisHistory> performEpochAnalysis(
            @RequestParam String filename,
            @RequestParam int epochs,
            @RequestParam int batchSize,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean save) {
        try {
            EpochAnalysisHistory history = analysisServiceML.performEpochAnalysis(filename, epochs, batchSize, userId, save);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Эндпоинт для получения истории Future Price Analysis
    @GetMapping("/history/future-price")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public ResponseEntity<List<FuturePriceAnalysisHistory>> getFuturePriceHistory(@RequestParam Long userId) {
        List<FuturePriceAnalysisHistory> histories = analysisServiceML.getFuturePriceHistory(userId);
        return ResponseEntity.ok(histories);
    }

    // Эндпоинт для получения истории Epoch Analysis
    @GetMapping("/history/epochs")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public ResponseEntity<List<EpochAnalysisHistory>> getEpochHistory(@RequestParam Long userId) {
        List<EpochAnalysisHistory> histories = analysisServiceML.getEpochHistory(userId);
        return ResponseEntity.ok(histories);
    }
}
