package com.energo.car_metrics.repositories;

import com.energo.car_metrics.models.EpochAnalysisHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpochAnalysisRepository extends JpaRepository<EpochAnalysisHistory, Long> {
}
