package com.zjsu.lyy.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/db")
    public Map<String, Object> db() {
        try (Connection conn = dataSource.getConnection()) {
            return Map.of("status", "UP",
                          "database", conn.getMetaData().getDatabaseProductName());
        } catch (Exception e) {
            return Map.of("status", "DOWN",
                          "error", e.getMessage());
        }
    }

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @GetMapping("/test/lb")
    public String testLb() {
        return restTemplate.getForObject("http://catalog-service/health/api/identify", String.class);
    }
}