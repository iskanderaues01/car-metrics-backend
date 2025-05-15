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
public class AnalysisHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "file_analyzed")
    private String fileAnalyzed;

    @Column(name = "count_records")
    private Integer countRecords;

    @Column(name = "mse")
    private String mse;

    @Column(name = "r_squared")
    private String rSquared;

    @Column(name = "equation", columnDefinition = "TEXT")
    private String equation;

    @Column(name = "img_base64", columnDefinition = "TEXT")
    private String imgBase64;

    @Column(name = "car_brand")
    private String carBrand;

    @Column(name = "car_model")
    private String carModel;

    @Column(name = "dummy_features", columnDefinition = "TEXT")
    private String dummyFeatures;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
