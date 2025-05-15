package com.energo.car_metrics.controllers;

import com.energo.car_metrics.dto.AvatarResponse;
import com.energo.car_metrics.dto.UpdateAvatarRequest;
import com.energo.car_metrics.services.impl.AvatarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping("/update-avatar")
    public ResponseEntity<String> updateAvatar(@Valid @RequestBody UpdateAvatarRequest request) {
        avatarService.updateAvatar(request.getUsername(), request.getAvatarBase64());
        return ResponseEntity.ok("Avatar updated successfully");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MOD', 'USER')")
    @GetMapping("/avatar")
    public ResponseEntity<AvatarResponse> getAvatar() {
        AvatarResponse avatarResponse = avatarService.getCurrentUserAvatar();
        return ResponseEntity.ok(avatarResponse);
    }
}