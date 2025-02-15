package com.energo.car_metrics.controllers;

import com.energo.car_metrics.services.impl.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnalysisController {
    @Autowired
    private AnalysisService analysisService;

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
}
