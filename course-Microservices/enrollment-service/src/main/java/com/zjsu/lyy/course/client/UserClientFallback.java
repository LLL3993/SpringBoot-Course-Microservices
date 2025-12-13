package com.zjsu.lyy.course.client;

import com.zjsu.lyy.course.dto.StudentDto;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {
    @Override
    public StudentDto getStudent(String studentId) {
        throw new IllegalArgumentException("用户服务不可用，请稍后再试");
    }
}