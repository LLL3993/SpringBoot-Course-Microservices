package com.zjsu.lyy.course.client;

import com.zjsu.lyy.course.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", fallback = UserClientFallback.class)
public interface UserClient {
    @GetMapping("/api/students/studentId/{studentId}")
    StudentDto getStudent(@PathVariable String studentId);
}