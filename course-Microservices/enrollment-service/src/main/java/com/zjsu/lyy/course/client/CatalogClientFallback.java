package com.zjsu.lyy.course.client;

import com.zjsu.lyy.course.dto.CourseDto;
import org.springframework.stereotype.Component;

@Component
public class CatalogClientFallback implements CatalogClient {

    @Override
    public CourseDto getCourse(String code) {
        return null; // 表示服务不可用
    }
}