package com.zjsu.lyy.course.controller;

import com.zjsu.lyy.course.model.Enrollment;
import com.zjsu.lyy.course.service.EnrollmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService service;
    private final RestTemplate restTemplate;

    @Autowired
    public EnrollmentController(EnrollmentService service, RestTemplate restTemplate) { 
        this.service = service; 
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public Map<String, Object> all() {
        List<Enrollment> list = service.getAll();
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Success");
        resp.put("data", list);
        return resp;
    }

    @GetMapping("/course/{courseId}")
    public Map<String, Object> byCourse(@PathVariable String courseId) {
        List<Enrollment> list = service.getByCourse(courseId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Success");
        resp.put("data", list);
        return resp;
    }

    @GetMapping("/student/{studentId}")
    public Map<String, Object> byStudent(@PathVariable String studentId) {
        List<Enrollment> list = service.getByStudent(studentId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Success");
        resp.put("data", list);
        return resp;
    }

    public static record EnrollDTO(String courseId, String studentId) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody EnrollDTO dto) {
        Enrollment e = service.enroll(dto.courseId, dto.studentId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 201);
        resp.put("message", "Enrolled");
        resp.put("data", e);
        return resp;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> drop(@PathVariable String id) {
        service.drop(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Dropped");
        resp.put("data", null);
        return resp;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String,Object> handleNotFound(IllegalArgumentException ex){
        Map<String,Object> resp = new HashMap<>();
        resp.put("code", 404);
        resp.put("message", ex.getMessage());
        resp.put("data", null);
        return resp;
    }

    @GetMapping("/test")
    public Map<String, Object> testLoadBalancing() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> serviceResponses = new ArrayList<>();
        
        try {
            // Call User Service using service discovery
            Map<String, Object> userResponse = restTemplate.getForObject(
                "http://user-service/api/students", Map.class);
            if (userResponse != null && userResponse.containsKey("port")) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("service", "user-service");
                userInfo.put("port", userResponse.get("port"));
                serviceResponses.add(userInfo);
            }
        } catch (Exception e) {
            Map<String, Object> errorInfo = new HashMap<>();
            errorInfo.put("service", "user-service");
            errorInfo.put("error", e.getMessage());
            serviceResponses.add(errorInfo);
        }
        
        try {
            // Call Catalog Service using service discovery
            Map<String, Object> catalogResponse = restTemplate.getForObject(
                "http://catalog-service/api/courses", Map.class);
            if (catalogResponse != null && catalogResponse.containsKey("port")) {
                Map<String, Object> catalogInfo = new HashMap<>();
                catalogInfo.put("service", "catalog-service");
                catalogInfo.put("port", catalogResponse.get("port"));
                serviceResponses.add(catalogInfo);
            }
        } catch (Exception e) {
            Map<String, Object> errorInfo = new HashMap<>();
            errorInfo.put("service", "catalog-service");
            errorInfo.put("error", e.getMessage());
            serviceResponses.add(errorInfo);
        }
        
        response.put("code", 200);
        response.put("message", "Load balancing test");
        response.put("data", serviceResponses);
        return response;
    }
}