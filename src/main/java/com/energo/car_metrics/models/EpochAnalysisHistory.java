package com.energo.car_metrics.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EpochAnalysisHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String fileAnalyzed;

    private int epochs;
    private int batchSize;

    @Lob
    private String analysisResult;

    @Lob
    private String epochTrainingPlot; // base64 строки изображения

    private String savedResultPath;

    private LocalDateTime created = LocalDateTime.now();

}
