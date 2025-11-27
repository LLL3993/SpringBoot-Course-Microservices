package com.zjsu.lyy.course.repository;

import com.zjsu.lyy.course.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByStudentId(String studentId);
    boolean existsByStudentId(String studentId);
    void deleteByStudentId(String studentId);
}