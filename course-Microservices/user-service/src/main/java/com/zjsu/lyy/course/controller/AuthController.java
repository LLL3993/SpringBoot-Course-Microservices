package com.zjsu.lyy.course.controller;

import com.zjsu.lyy.course.dto.LoginRequest;
import com.zjsu.lyy.course.dto.LoginResponse;
import com.zjsu.lyy.course.model.Student;
import com.zjsu.lyy.course.service.StudentService;
import com.zjsu.lyy.course.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Student student = studentService.getByStudentId(req.getUsername());
        if (student == null || !"123456".equals(req.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("用户名或密码错误");
        }

        String role = "USER";

        String token = jwtUtil.generateToken(
                student.getStudentId(),
                student.getName(),
                role
        );
        return ResponseEntity.ok(new LoginResponse(token, student));
    }
}