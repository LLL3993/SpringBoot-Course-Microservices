package com.zjsu.lyy.course.service; // 注意包名建议改为 course

import com.zjsu.lyy.course.model.Student;
import com.zjsu.lyy.course.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("学生不存在"));
    }

    public Student create(Student student) {
        if (student.getStudentId() == null || student.getStudentId().isBlank()) {
            throw new IllegalArgumentException("学号不能为空");
        }
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new IllegalArgumentException("学号已存在");
        }
        if (student.getEmail() == null || !student.getEmail().contains("@")) {
            throw new IllegalArgumentException("邮箱格式错误");
        }

        student.setCreatedAt(LocalDateTime.now());
        return studentRepository.save(student);
    }

    public Student update(String studentId, Student newStudent) {
        Student oldStudent = getByStudentId(studentId);

        // 如果修改了学号，检查新学号是否已存在
        if (newStudent.getStudentId() != null &&
            !newStudent.getStudentId().equals(oldStudent.getStudentId())) {
            if (studentRepository.existsByStudentId(newStudent.getStudentId())) {
                throw new IllegalArgumentException("新学号已存在");
            }
            // 删除旧记录（如果学号是主键）
            studentRepository.deleteById(studentId);
            oldStudent.setStudentId(newStudent.getStudentId());
        }

        // 更新其他字段
        if (newStudent.getName() != null) oldStudent.setName(newStudent.getName());
        if (newStudent.getMajor() != null) oldStudent.setMajor(newStudent.getMajor());
        if (newStudent.getGrade() != null) oldStudent.setGrade(newStudent.getGrade());
        if (newStudent.getEmail() != null) {
            if (!newStudent.getEmail().contains("@")) {
                throw new IllegalArgumentException("邮箱格式错误");
            }
            oldStudent.setEmail(newStudent.getEmail());
        }

        return studentRepository.save(oldStudent);
    }

    public void delete(String studentId) {
        if (!studentRepository.existsByStudentId(studentId)) {
            throw new IllegalArgumentException("学生不存在");
        }

        // ❗️注意：微服务中不再检查选课记录
        // 删除学生由 user-service 自己决定
        // 是否允许删除应由 enrollment-service 提供接口判断（后续通过 HTTP 调用）

        studentRepository.deleteByStudentId(studentId);
    }
}