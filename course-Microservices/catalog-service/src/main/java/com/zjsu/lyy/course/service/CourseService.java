package com.zjsu.lyy.course.service;

import com.zjsu.lyy.course.model.Course;
import com.zjsu.lyy.course.repository.CourseRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository repo;

    public CourseService(CourseRepository repo) { this.repo = repo; }

    public List<Course> getAll() { return repo.findAll(); }

    public Course getByCode(String code) {
        return repo.findByCode(code)
                   .orElseThrow(() -> new IllegalArgumentException("课程不存在"));
    }

    public Course create(Course course) {
        if (course.getCode() == null || course.getTitle() == null)
            throw new IllegalArgumentException("code / title 不能为空");
        if (course.getInstructor() == null || course.getInstructor().getName() == null)
            throw new IllegalArgumentException("授课教师信息不完整");
        if (course.getSchedule() == null)
            throw new IllegalArgumentException("课程时间信息不完整");
        if (course.getCapacity() == null || course.getCapacity() <= 0)
            throw new IllegalArgumentException("课容量必须 > 0");
        return repo.save(course);
    }

    public Course update(String code, Course newCourse) {
        Course old = getByCode(code);
        if (newCourse.getCode() != null) old.setCode(newCourse.getCode());
        if (newCourse.getTitle() != null) old.setTitle(newCourse.getTitle());
        if (newCourse.getInstructor() != null) old.setInstructor(newCourse.getInstructor());
        if (newCourse.getSchedule() != null) {
            old.setSchedule(newCourse.getSchedule());
        }
        if (newCourse.getCapacity() != null) {
            old.setCapacity(newCourse.getCapacity());
        }
        return repo.save(old);
    }

    @Transactional
    public void delete(String code) {
        if (!repo.existsByCode(code)) throw new IllegalArgumentException("课程不存在");
        repo.deleteByCode(code);
    }
}