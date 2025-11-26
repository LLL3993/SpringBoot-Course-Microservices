package com.zjsu.lyy.course.repository;

import com.zjsu.lyy.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, String> {
    Optional<Course> findByCode(String code);
    boolean existsByCode(String code);
    void deleteByCode(String code);
}