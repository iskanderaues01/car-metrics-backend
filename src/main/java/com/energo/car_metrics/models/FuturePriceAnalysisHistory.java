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
public class FuturePriceAnalysisHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String fileAnalyzed;

    private String futureYear;
    private String futureMileage;
    private String futureEngineVolume;
    private String futureFuel;
    private String futureTransmission;

    @Lob
    private String analysisResult;

    @Lob
    private String priceDistributionPlot; // base64 строки изображения

    private String savedResultPath;

    private LocalDateTime created = LocalDateTime.now();
}
