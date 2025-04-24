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
public class LogisticAnalysisHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "file_analyzed")
    private String fileAnalyzed;

    @Column(name = "price_threshold")
    private Double priceThreshold;

    @Column(name = "accuracy")
    private Double accuracy;

    @Column(name = "confusion_matrix", columnDefinition = "TEXT")
    private String confusionMatrix;

    @Column(name = "coefficients", columnDefinition = "TEXT")
    private String coefficients;

    @Column(name = "intercept", columnDefinition = "TEXT")
    private String intercept;

    @Column(name = "features", columnDefinition = "TEXT")
    private String features;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "img_base64", columnDefinition = "TEXT")
    private String imgBase64;

    @Column(name = "saved_result_path", columnDefinition = "TEXT")
    private String savedResultPath;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}

