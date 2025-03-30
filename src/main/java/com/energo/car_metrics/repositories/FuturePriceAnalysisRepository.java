package com.energo.car_metrics.repositories;

import com.energo.car_metrics.models.FuturePriceAnalysisHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuturePriceAnalysisRepository extends JpaRepository<FuturePriceAnalysisHistory, Long> {
    List<FuturePriceAnalysisHistory> findByUserId(Long userId);
}