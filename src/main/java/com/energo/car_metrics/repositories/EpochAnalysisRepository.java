package com.energo.car_metrics.repositories;

import com.energo.car_metrics.models.EpochAnalysisHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpochAnalysisRepository extends JpaRepository<EpochAnalysisHistory, Long> {
    List<EpochAnalysisHistory> findByUserId(Long userId);
}
