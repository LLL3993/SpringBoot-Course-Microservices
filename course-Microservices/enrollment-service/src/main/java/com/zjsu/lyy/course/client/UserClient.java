package com.zjsu.lyy.course.client;

import com.zjsu.lyy.course.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "user-service",
    fallbackFactory = UserClientFallbackFactory.class
)
public interface UserClient {
    @GetMapping("/api/students/studentId/{studentId}")
    StudentDto getStudent(@PathVariable String studentId);
}

/**
 * FallbackFactory：能捕获所有异常（包括 503）
 */
@Component
class UserClientFallbackFactory implements FallbackFactory<UserClient> {
    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public StudentDto getStudent(String studentId) {
                System.out.println("【UserClientFallbackFactory】触发降级，原因: " + cause.getMessage());
                return null; // 服务不可用
            }
        };
    }
}