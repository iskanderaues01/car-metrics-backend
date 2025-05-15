package com.energo.car_metrics.controllers;

import com.energo.car_metrics.dto.ChangePasswordRequest;
import com.energo.car_metrics.dto.UpdateAvatarRequest;
import com.energo.car_metrics.services.impl.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import com.energo.car_metrics.models.Role;
import com.energo.car_metrics.models.UserAny;
import com.energo.car_metrics.models.enums.ERole;
import com.energo.car_metrics.models.enums.EStatus;
import com.energo.car_metrics.payload.request.LoginRequest;
import com.energo.car_metrics.payload.request.SignupRequest;
import com.energo.car_metrics.payload.response.JwtResponse;
import com.energo.car_metrics.payload.response.MessageResponse;
import com.energo.car_metrics.repositories.RoleRepository;
import com.energo.car_metrics.repositories.UserRepository;
import com.energo.car_metrics.security.jwt.JwtUtils;
import com.energo.car_metrics.services.impl.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsServiceImpl userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity
                .ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if(userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        UserAny userAny = new UserAny(signupRequest.getUsername(), signupRequest.getEmail(), passwordEncoder.encode(signupRequest.getPassword()), EStatus.ACTIVE);

        String roleRequest = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();


        if(roleRequest == null || roleRequest.isEmpty()) {
            Role operatorRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found!"));
            roles.add(operatorRole);
        } else {
            ERole role = ERole.valueOf(roleRequest);
            log.info(role.toString());
            switch (role) {
                case ROLE_ADMIN:
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role not found!"));
                    roles.add(adminRole);
                    break;
                case ROLE_MODERATOR:
                    Role moderatorRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role not found!"));
                    roles.add(moderatorRole);
                    break;
                default:
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role not found!"));
                    roles.add(userRole);
            }
        }

        userAny.setRoles(roles);
        userRepository.save(userAny);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }



}
