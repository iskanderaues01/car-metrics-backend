package com.energo.car_metrics.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import com.energo.car_metrics.models.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roles", schema = "car_metrics_rest")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ERole name;

    @Size(max = 250)
    private String description;

    public Role(ERole name) {}
}
