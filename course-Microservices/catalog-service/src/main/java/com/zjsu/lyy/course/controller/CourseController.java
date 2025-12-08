package com.zjsu.lyy.course.controller;

import com.zjsu.lyy.course.model.Course;
import com.zjsu.lyy.course.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService service;

    @Value("${server.port}")
    private String port;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public Map<String, Object> all() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Success");
        resp.put("data", service.getAll());
        resp.put("port", port);
        return resp;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@Valid @RequestBody Course course) {
        Course saved = service.create(course);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 201);
        resp.put("message", "Created");
        resp.put("data", saved);
        return resp;
    }

    @GetMapping("/{courseId}")
    public Map<String, Object> one(@PathVariable String courseId) {
        Course c = service.getByCode(courseId); // 你用 courseId 字段存的是编号
        if (c == null) {
            throw new IllegalArgumentException("Course not found");
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Success");
        resp.put("data", c);
        return resp;
    }
    
    @GetMapping("/code/{code}")
    public Map<String, Object> getOne(@PathVariable String code) {
        Course c = service.getByCode(code);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Success");
        resp.put("data", c);
        return resp;
    }

    @PutMapping("/code/{code}")
    public Map<String, Object> update(@PathVariable String code,
                                    @Valid @RequestBody Course course) {
        Course updated = service.update(code, course);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Updated");
        resp.put("data", updated);
        return resp;
    }

    @DeleteMapping("/code/{code}")
    public Map<String, Object> delete(@PathVariable String code) {
        service.delete(code); 
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Deleted");
        resp.put("data", null);
        return resp;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) 
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, Object> handleNotFound(IllegalArgumentException ex) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 404);
        resp.put("message", ex.getMessage());
        resp.put("data", null);
        return resp;
    }
}