package com.energo.car_metrics.controllers;

import com.energo.car_metrics.models.LogisticAnalysisHistory;
import com.energo.car_metrics.services.impl.AnalysisHistoryService;
import com.energo.car_metrics.services.impl.LogisticAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logistic-analysis")
@RequiredArgsConstructor
public class LogisticAnalysisController {

    private final LogisticAnalysisService analysisService;

    @GetMapping("/perform-logic")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public ResponseEntity<LogisticAnalysisHistory> performAnalysisLogistic(
            @RequestParam String filename,
            @RequestParam Integer priceThreshold,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "false") boolean save) {

        try {
            LogisticAnalysisHistory result = analysisService.performAnalysis(filename, priceThreshold, userId, save);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    public ResponseEntity<List<LogisticAnalysisHistory>> getHistory(@RequestParam Long userId) {
        List<LogisticAnalysisHistory> history = analysisService.getHistoryByUserId(userId);
        return ResponseEntity.ok(history);
    }
}
