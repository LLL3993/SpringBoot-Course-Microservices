package com.zjsu.lyy.course.service;

import com.zjsu.lyy.course.model.Enrollment;
import com.zjsu.lyy.course.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final RestTemplate restTemplate;

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Value("${catalog-service.url}")
    private String catalogServiceUrl;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, RestTemplate restTemplate) {
        this.enrollmentRepository = enrollmentRepository;
        this.restTemplate = restTemplate;
    }

    public Enrollment enroll(String courseId, String studentId) {
        // 1. 验证学生是否存在
        String userUrl = userServiceUrl + "/api/students/studentId/" + studentId;
        try {
            restTemplate.getForObject(userUrl, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }

        // 2. 验证课程是否存在
        String courseUrl = catalogServiceUrl + "/api/courses/" + courseId;
        Map<String, Object> courseResponse;
        try {
            courseResponse = restTemplate.getForObject(courseUrl, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Course not found: " + courseId);
        }

        // 3. 提取课程容量和已选人数
        Map<String, Object> courseData = (Map<String, Object>) courseResponse.get("data");
        Integer capacity = (Integer) courseData.get("capacity");
        Integer enrolled = (Integer) courseData.get("enrolled");

        if (enrolled == null || capacity == null) {
            throw new IllegalArgumentException("Course data is incomplete");
        }

        // 4. 检查课程容量
        if (enrolled >= capacity) {
            throw new IllegalArgumentException("Course is full");
        }

        // 5. 检查是否已选课
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new IllegalArgumentException("Already enrolled in this course");
        }

        // 6. 创建选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(studentId);
        enrollment.setStatus(Enrollment.Status.ACTIVE);
        enrollment.setEnrolledAt(LocalDateTime.now());

        Enrollment saved = enrollmentRepository.save(enrollment);

        // 7. 更新课程已选人数（非阻塞）
        try {
            updateCourseEnrolledCount(courseId, enrolled + 1);
        } catch (Exception e) {
            System.err.println("Failed to update course enrolled count: " + e.getMessage());
        }

        return saved;
    }

    private void updateCourseEnrolledCount(String courseId, int newCount) {
        String url = catalogServiceUrl + "/api/courses/" + courseId;
        Map<String, Object> updateData = Map.of("enrolled", newCount);
        restTemplate.put(url, updateData);
    }

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

        // 可选：减少课程已选人数
        try {
            String courseUrl = catalogServiceUrl + "/api/courses/" + enrollment.getCourseId();
            Map<String, Object> courseResponse = restTemplate.getForObject(courseUrl, Map.class);
            Map<String, Object> courseData = (Map<String, Object>) courseResponse.get("data");
            Integer enrolled = (Integer) courseData.get("enrolled");
            updateCourseEnrolledCount(enrollment.getCourseId(), enrolled - 1);
        } catch (Exception e) {
            System.err.println("Failed to update course enrolled count on drop: " + e.getMessage());
        }

        enrollmentRepository.delete(enrollment);
    }
}