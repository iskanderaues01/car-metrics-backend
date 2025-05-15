package com.energo.car_metrics.services.impl;

import com.energo.car_metrics.dto.UserDetailsResponse;
import com.energo.car_metrics.dto.UserSummaryResponse;
import com.energo.car_metrics.models.UserAny;
import com.energo.car_metrics.models.enums.EStatus;
import com.energo.car_metrics.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserSummaryResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserSummary)
                .collect(Collectors.toList());
    }

    public UserDetailsResponse getUserDetails(String username) {
        UserAny user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return mapToUserDetails(user);
    }

    public void changeUserStatus(String username, String newStatus) {
        UserAny user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        EStatus status = EStatus.valueOf(newStatus.toUpperCase());
        user.setStatus(status);
        userRepository.save(user);
    }

    public void resetUserPassword(String username) {
        UserAny user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String defaultPassword = "Qwerty123";
        user.setPassword(passwordEncoder.encode(defaultPassword));
        userRepository.save(user);
    }

    private UserSummaryResponse mapToUserSummary(UserAny user) {
        UserSummaryResponse response = new UserSummaryResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setCreated(user.getCreated());
        response.setStatus(user.getStatus().name());
        response.setRoles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));
        return response;
    }

    private UserDetailsResponse mapToUserDetails(UserAny user) {
        UserDetailsResponse response = new UserDetailsResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setStatus(user.getStatus().name());
        response.setCreated(user.getCreated());
        response.setRoles(user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet()));
        return response;
    }
}