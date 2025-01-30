package com.energo.car_metrics.models;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Data
@Table
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String price;
    private Integer year;
    private String link;
    private String conditionBody;
    private Double engineVolume;
    private String fuel;
    private String transmission;
    private Double mileage;
    private String rawDescription;
}
