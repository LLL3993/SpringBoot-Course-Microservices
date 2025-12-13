package com.zjsu.lyy.course.client;

import com.zjsu.lyy.course.dto.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "catalog-service",
    fallbackFactory = CatalogClientFallbackFactory.class
)
public interface CatalogClient {
    @GetMapping("/api/courses/{code}")
    CourseDto getCourse(@PathVariable String code);
}

/**
 * FallbackFactory：能捕获所有异常（包括 503）
 */
@Component
class CatalogClientFallbackFactory implements FallbackFactory<CatalogClient> {
    @Override
    public CatalogClient create(Throwable cause) {
        return new CatalogClient() {
            @Override
            public CourseDto getCourse(String code) {
                System.out.println("【CatalogClientFallbackFactory】触发降级，原因: " + cause.getMessage());
                return null; // 服务不可用
            }
        };
    }
}