package com.energo.car_metrics.repositories;

import com.energo.car_metrics.models.FuturePriceAnalysisHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuturePriceAnalysisRepository extends JpaRepository<FuturePriceAnalysisHistory, Long> {
}