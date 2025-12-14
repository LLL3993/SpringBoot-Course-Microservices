package com.zjsu.lyy.course.dto;

import com.zjsu.lyy.course.model.Student;

public class LoginResponse {
    private String token;
    private Student student;
    /* 全参构造 + getter/setter */
    public LoginResponse(String token, Student student) {
        this.token = token;
        this.student = student;
    }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}