package com.zjsu.lyy.course.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
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

    @Value("${server.port}")
    private String port;

    @GetMapping("/api/identify")
    public Map<String,String> identify(){
        return Map.of("instance", getClass().getSimpleName(), "port", port);
    }
}