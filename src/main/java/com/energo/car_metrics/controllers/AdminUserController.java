package com.energo.car_metrics.controllers;

import com.energo.car_metrics.dto.ChangeStatusRequest;
import com.energo.car_metrics.dto.UserDetailsResponse;
import com.energo.car_metrics.dto.UserSummaryResponse;
import com.energo.car_metrics.services.impl.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{username}")
    public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable String username) {
        return ResponseEntity.ok(adminUserService.getUserDetails(username));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/change-status")
    public ResponseEntity<String> changeUserStatus(@Valid @RequestBody ChangeStatusRequest request) {
        adminUserService.changeUserStatus(request.getUsername(), request.getStatus());
        return ResponseEntity.ok("User status updated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reset-password/{username}")
    public ResponseEntity<String> resetPassword(@PathVariable String username) {
        adminUserService.resetUserPassword(username);
        return ResponseEntity.ok("Password reset to default successfully");
    }
}
