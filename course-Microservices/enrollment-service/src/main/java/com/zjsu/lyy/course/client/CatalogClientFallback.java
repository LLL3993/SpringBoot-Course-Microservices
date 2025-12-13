package com.zjsu.lyy.course.client;

import com.zjsu.lyy.course.dto.CourseDto;
import org.springframework.stereotype.Component;

@Component
public class CatalogClientFallback implements CatalogClient {
    @Override
    public CourseDto getCourse(String code) {
        throw new IllegalArgumentException("课程服务不可用，请稍后再试");
    }
}