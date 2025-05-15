package com.energo.car_metrics.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserDetailsResponse {

    private Long id;
    private String username;
    private String email;
    private String avatarUrl;
    private String status;
    private LocalDateTime created;
    private Set<String> roles;
}
