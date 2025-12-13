package com.zjsu.lyy.course.client;

import com.zjsu.lyy.course.dto.StudentDto;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public StudentDto getStudent(String studentId) {
        // 返回 null 表示服务不可用，业务层会抛异常
        return null;
    }
}