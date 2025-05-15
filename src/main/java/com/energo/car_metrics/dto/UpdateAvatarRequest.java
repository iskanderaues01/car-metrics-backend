package com.energo.car_metrics.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateAvatarRequest {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Avatar (base64) must not be blank")
    private String avatarBase64;
}
