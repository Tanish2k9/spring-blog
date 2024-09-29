package com.tanish.Blog_springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class healthCheck {
    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheckup(){
        return ResponseEntity.ok("health 100%");
    }
}
