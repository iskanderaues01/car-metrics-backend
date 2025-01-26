package com.energo.car_metrics.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content";
    }

    @GetMapping("/teacher")
    @PreAuthorize("hasRole('TEACHER') " +
            "or hasRole('SENIOR_TEACHER')" +
            " or hasRole('ADMIN')")
    public String teacherAccess() {
        return "Teacher Content: ";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Private Content 2024";
    }
}
