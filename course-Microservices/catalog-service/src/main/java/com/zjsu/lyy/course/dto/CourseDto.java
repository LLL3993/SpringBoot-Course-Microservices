package com.zjsu.lyy.course.dto;   // 注意包名用你的

public class CourseDto {
    private String code;
    private String title;
    private Integer capacity;
    private Integer enrolled;

    // 必须补全 get/set，否则 JSON 会失败
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getEnrolled() { return enrolled; }
    public void setEnrolled(Integer enrolled) { this.enrolled = enrolled; }
}