package com.zjsu.lyy.course.controller;

import com.zjsu.lyy.course.model.Enrollment;
import com.zjsu.lyy.course.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) { this.service = service; }

    @GetMapping
    public Map<String, Object> all() {
        List<Enrollment> list = service.getAll();
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Success");
        resp.put("data", list);
        return resp;
    }

    @GetMapping("/course/{courseCode}")
    public Map<String, Object> byCourse(@PathVariable String courseCode) {
        List<Enrollment> list = service.getByCourse(courseCode);
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

    public static record EnrollDTO(String courseCode, String studentId) {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@RequestBody EnrollDTO dto) {
        Enrollment e = service.enroll(dto.courseCode, dto.studentId);
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
}