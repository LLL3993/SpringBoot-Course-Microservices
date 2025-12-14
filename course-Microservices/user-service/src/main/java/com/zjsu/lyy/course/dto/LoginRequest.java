package com.zjsu.lyy.course.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank private String username;
    @NotBlank private String password;
    /* 自己生成 getter/setter */
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}