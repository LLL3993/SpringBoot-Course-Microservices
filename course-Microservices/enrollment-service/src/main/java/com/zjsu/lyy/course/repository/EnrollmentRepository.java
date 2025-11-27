package com.zjsu.lyy.course.repository;

import com.zjsu.lyy.course.model.Enrollment;
import com.zjsu.lyy.course.model.Enrollment.Status;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
    boolean existsByCourseIdAndStudentId(String courseId, String studentId);
    List<Enrollment> findByCourseId(String courseId);
    List<Enrollment> findByStudentId(String studentId);
    int countByCourseIdAndStatus(String code, Status active);
    boolean existsByStudentIdAndStatus(String studentId, Enrollment.Status status);
}