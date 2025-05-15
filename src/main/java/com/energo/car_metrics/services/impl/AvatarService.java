package com.energo.car_metrics.services.impl;

import com.energo.car_metrics.dto.AvatarResponse;
import com.energo.car_metrics.models.UserAny;
import com.energo.car_metrics.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class AvatarService {

    private final UserRepository userRepository;

    public void updateAvatar(String username, String avatarBase64) {
        UserAny user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setAvatarUrl(avatarBase64);
        userRepository.save(user);
    }

    public AvatarResponse getCurrentUserAvatar() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserAny user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new AvatarResponse(user.getAvatarUrl());
    }
}
