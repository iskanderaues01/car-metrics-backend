package com.energo.car_metrics.repositories;

import com.energo.car_metrics.models.LogisticAnalysisHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogisticAnalysisHistoryRepository extends JpaRepository<LogisticAnalysisHistory, Long> {
}
