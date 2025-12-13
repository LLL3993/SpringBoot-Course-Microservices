package com.zjsu.lyy.course.service;

import com.zjsu.lyy.course.client.UserClient;
import com.zjsu.lyy.course.client.CatalogClient;
import com.zjsu.lyy.course.dto.StudentDto;
import com.zjsu.lyy.course.dto.CourseDto;
import com.zjsu.lyy.course.model.Enrollment;
import com.zjsu.lyy.course.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserClient userClient;
    private final CatalogClient catalogClient;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             UserClient userClient,
                             CatalogClient catalogClient) {
        this.enrollmentRepository = enrollmentRepository;
        this.userClient = userClient;
        this.catalogClient = catalogClient;
    }

    public Enrollment enroll(String courseId, String studentId) {
        // 1. 验证学生（Feign 自动 fallback）
        StudentDto student = userClient.getStudent(studentId);
        if (student == null) {
            throw new IllegalArgumentException("用户服务不可用，请稍后再试");
        }

        // 2. 验证课程（Feign 自动 fallback）
        CourseDto course = catalogClient.getCourse(courseId);
        if (course == null) {
            throw new IllegalArgumentException("课程服务不可用，请稍后再试");
        }

        // 3. 容量检查
        Integer capacity = course.getCapacity();
        Integer enrolled = course.getEnrolled();
        if (capacity == null || enrolled == null) {
            throw new IllegalArgumentException("Course data is incomplete");
        }
        if (enrolled >= capacity) {
            throw new IllegalArgumentException("Course is full");
        }

        // 4. 重复选课检查
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new IllegalArgumentException("Already enrolled in this course");
        }

        // 5. 创建选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(studentId);
        enrollment.setStatus(Enrollment.Status.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());
        return enrollmentRepository.save(enrollment);
    }

    /* ---------- 原有接口，一字未动 ---------- */

    public List<Enrollment> getAll() {
        return enrollmentRepository.findAll();
    }

    public List<Enrollment> getByCourse(String courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public List<Enrollment> getByStudent(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public void drop(String id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + id));
        enrollmentRepository.delete(enrollment);
    }
}