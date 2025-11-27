package com.zjsu.lyy.course.service;

import com.zjsu.lyy.course.model.Enrollment;
import com.zjsu.lyy.course.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepo;
    private final RestTemplate restTemplate;

    public EnrollmentService(EnrollmentRepository enrollmentRepo, RestTemplate restTemplate) {
        this.enrollmentRepo = enrollmentRepo;
        this.restTemplate = restTemplate;
    }

    public Enrollment enroll(String courseCode, String studentId) {
        // 检查课程是否存在
        String courseUrl = "http://localhost:8082/api/courses/code/" + courseCode;
        try {
            restTemplate.getForObject(courseUrl, Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("课程不存在");
        }

        // 检查学生是否存在
        String studentUrl = "http://localhost:8081/api/students/" + studentId;
        try {
            restTemplate.getForObject(studentUrl, Map.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("学生不存在");
        }

        // 检查是否已选课
        if (enrollmentRepo.existsByCourseIdAndStudentId(courseCode, studentId)) {
            throw new IllegalArgumentException("重复选课");
        }

        // 创建选课记录
        Enrollment e = new Enrollment();
        e.setCourseId(courseCode);
        e.setStudentId(studentId);
        e.setStatus(Enrollment.Status.ACTIVE);
        return enrollmentRepo.save(e);
    }

    public void drop(String id) {
        Enrollment e = enrollmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("选课记录不存在"));
        enrollmentRepo.deleteById(id);
    }

    public List<Enrollment> getAll() {
        return enrollmentRepo.findAll();
    }

    public List<Enrollment> getByCourse(String courseCode) {
        return enrollmentRepo.findByCourseId(courseCode);
    }

    public List<Enrollment> getByStudent(String studentId) {
        return enrollmentRepo.findByStudentId(studentId);
    }
}