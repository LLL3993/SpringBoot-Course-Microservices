package com.zjsu.lyy.course.client;

import com.zjsu.lyy.course.dto.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalog-service", fallback = CatalogClientFallback.class)
public interface CatalogClient {
    @GetMapping("/api/courses/{code}")
    CourseDto getCourse(@PathVariable String code);
}