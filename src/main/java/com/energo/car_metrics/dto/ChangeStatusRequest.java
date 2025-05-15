package com.energo.car_metrics.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeStatusRequest {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "New status must not be blank")
    private String status; // ACTIVE, DISABLED, BLOCKED
}