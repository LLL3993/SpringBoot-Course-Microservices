-- 课程表 -----------------------------------------------------------------
INSERT INTO courses (
    id, code, title,
    instructor_id, instructor_name, instructor_email,
    schedule_day_of_week, schedule_start_time, schedule_end_time, schedule_expected_attendance,
    capacity, enrolled, version
) VALUES (
    'c201', 'CS201', '数据结构',
    'T001', '张教授', 'zhang@zjgsu.edu.cn',
    'MONDAY', '08:00', '10:00', 50,
    3, 0, 0   -- capacity=3, enrolled=0, version=0
);

-- 学生表 -----------------------------------------------------------------
INSERT INTO students (
    id, student_id, name, major, grade, email, created_at
) VALUES (
    's201', 'stu201', 'john', 'computer', 1, 'john@gmai.com', NOW()
);