package com.energo.car_metrics.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserSummaryResponse {

    private String username;
    private String email;
    private LocalDateTime created;
    private String status;
    private Set<String> roles;
}
