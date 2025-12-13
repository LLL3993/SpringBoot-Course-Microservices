package com.zjsu.lyy.course.controller;

import com.zjsu.lyy.course.dto.StudentDto;
import com.zjsu.lyy.course.model.Student;
import com.zjsu.lyy.course.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;

    @Value("${server.port}")
    private String port;

    public StudentController(StudentService service) { this.service = service; }

    @GetMapping
    public Map<String, Object> all() {
        List<Student> list = service.getAll();
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Success");
        resp.put("data", list);
        resp.put("port", port);
        return resp;
    }

    @GetMapping("/{studentId}")
    public Map<String, Object> one(@PathVariable String studentId) {
        Student stu = service.getByStudentId(studentId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Success");
        resp.put("data", stu);
        return resp;
    }

    @GetMapping("/studentId/{studentId}")
    public StudentDto getOne(@PathVariable String studentId) {
        Student stu = service.getByStudentId(studentId);
        if (stu == null) {
            throw new IllegalArgumentException("Student not found");
        }
        StudentDto dto = new StudentDto();
        dto.setId(stu.getId());
        dto.setStudentId(stu.getStudentId());
        dto.setName(stu.getName());
        dto.setMajor(stu.getMajor());
        dto.setGrade(stu.getGrade());
        dto.setEmail(stu.getEmail());
        dto.setCreatedAt(stu.getCreatedAt());
        return dto;          // 返回 DTO
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@Valid @RequestBody Student student) {
        Student saved = service.create(student);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 201);
        resp.put("message", "Created");
        resp.put("data", saved);
        return resp;
    }

    @PutMapping("/{studentId}")
    public Map<String, Object> update(@PathVariable String studentId,
                                      @Valid @RequestBody Student student) {
        Student updated = service.update(studentId, student);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Updated");
        resp.put("data", updated);
        return resp;
    }

    @DeleteMapping("/{studentId}")
    public Map<String, Object> delete(@PathVariable String studentId) {
        service.delete(studentId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 200);
        resp.put("message", "Deleted");
        resp.put("data", null);
        return resp;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)   // 400
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, Object> handleBizException(IllegalArgumentException e) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", 400);
        resp.put("message", e.getMessage());
        resp.put("data", null);
        return resp;
    }
}