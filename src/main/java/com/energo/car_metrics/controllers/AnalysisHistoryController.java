package com.energo.car_metrics.controllers;

import com.energo.car_metrics.models.AnalysisHistory;
import com.energo.car_metrics.repositories.AnalysisHistoryRepository;
import com.energo.car_metrics.services.impl.AnalysisHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis-history")
public class AnalysisHistoryController {

    private final AnalysisHistoryService analysisHistoryService;
    private final AnalysisHistoryRepository analysisHistoryRepository;

    public AnalysisHistoryController(AnalysisHistoryService analysisHistoryService,
                                     AnalysisHistoryRepository analysisHistoryRepository) {
        this.analysisHistoryService = analysisHistoryService;
        this.analysisHistoryRepository = analysisHistoryRepository;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    @PostMapping("/save")
    public ResponseEntity<AnalysisHistory> saveAnalysisHistory(
            @RequestParam Long userId,
            @RequestBody Map<String, Object> analysisData) {

        AnalysisHistory savedHistory = analysisHistoryService.saveAnalysisHistory(userId, analysisData);
        return ResponseEntity.ok(savedHistory);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnalysisHistory>> getHistoryByUserId(@PathVariable Long userId) {
        List<AnalysisHistory> historyList = analysisHistoryRepository.findByUserId(userId);
        return ResponseEntity.ok(historyList);
    }
}
