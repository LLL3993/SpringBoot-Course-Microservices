package com.zjsu.lyy.course.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.persistence.Id;

@Entity   // 告诉 JPA 这是张表
@Table(name = "courses")

public class Course {

    @Id
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    private String title;
    private Instructor instructor;
    private ScheduleSlot schedule;
    private Integer capacity;
    private Integer enrolled = 0;
    
    @Version
    private Integer version;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }
    public ScheduleSlot getSchedule() { return schedule; }
    public void setSchedule(ScheduleSlot schedule) { this.schedule = schedule; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public Integer getEnrolled() { return enrolled; }
    public void setEnrolled(Integer enrolled) { this.enrolled = enrolled; }
}