# SpringBoot-Course-Microservices



# V1.0.0

```markdown
> 课程：校园选课与教学资源管理平台  
> 版本：v1.0.0  
> 阶段：微服务架构（初次拆分）
```

---

## 1. 项目简介

- **名称**：course-cloud  
- **版本**：v1.0.0  
- **来源**：基于 hw04b 单体应用拆分为三个微服务  
- **架构**：Spring Boot + MySQL + Docker Compose + RestTemplate  

**服务拆分示意** ：

单体( course-system ) 

├─Course 

├─Student 

└─Enrollment 

↓ 拆分

 user-service(8081) ┐

 catalog-service(8082) ├─> enrollment-service(8083) 

enrollment-service(8083) ┘

## 2. 架构图

```text
客户端
├─→ user-service (8081) → user_db (3306)
│   └── 学生/用户管理
├─→ catalog-service (8082) → catalog_db (3307)
│   └── 课程管理
└─→ enrollment-service (8083) → enrollment_db (3308)
    ├── 选课管理
    ├── HTTP → user-service (验证学生)
    └── HTTP → catalog-service (验证课程)
```

## 3.技术栈：

| 组件             | 版本   |
| ---------------- | ------ |
| Spring Boot      | 3.5.7  |
| Java             | 21     |
| MySQL            | 8.0    |
| Docker & Compose | 20.10+ |
| RestTemplate     | 内置   |

## 4.环境要求：

- JDK 21+
- Maven 3.8+
- Docker 20.10+
- Docker Compose 1.29+

## 5. 构建与运行

### 5.1 克隆代码

```bash
git clone https://github.com/LLL3993/SpringBoot-Course-Microservices.git
cd course-cloud
```

### 5.2 一键构建 & 启动

```bash
# 构建镜像并后台启动
docker-compose up -d --build
```

### 5.3 等待健康检查全部 `healthy`

```bash
watch docker-compose ps
```

所有服务状态为 `Up (healthy)` 即可继续测试。

## 6. API 文档

### 6.1 用户服务（user-service）

| 方法   | 路径                                  | 描述         |
| :----- | :------------------------------------ | :----------- |
| GET    | `/api/students`                       | 获取所有学生 |
| GET    | `/api/students/{id}`                  | 按主键查询   |
| GET    | `/api/students/studentId/{studentId}` | 按学号查询   |
| POST   | `/api/students`                       | 创建学生     |
| PUT    | `/api/students/{id}`                  | 更新学生     |
| DELETE | `/api/students/{id}`                  | 删除学生     |

### 6.2 课程目录服务（catalog-service）

| 方法   | 方法                       | 描述           |
| :----- | :------------------------- | :------------- |
| GET    | `/api/courses`             | 获取所有课程   |
| GET    | `/api/courses/{id}`        | 按主键查询     |
| GET    | `/api/courses/code/{code}` | 按课程代码查询 |
| POST   | `/api/courses`             | 创建课程       |
| PUT    | `/api/courses/code/{code}` | 更新课程       |
| DELETE | `/api/courses/code/{code}` | 删除课程       |

### 6.3 选课服务（enrollment-service）

| 方法   | 路径                                   | 描述         |
| :----- | :------------------------------------- | :----------- |
| GET    | `/api/enrollments`                     | 所有选课记录 |
| GET    | `/api/enrollments/course/{courseId}`   | 按课程查询   |
| GET    | `/api/enrollments/student/{studentId}` | 按学生查询   |
| POST   | `/api/enrollments`                     | 学生选课     |
| DELETE | `/api/enrollments/{id}`                | 退课         |

## 7. 测试示例

一键执行：

```bash
chmod +x test-services.sh
./test-services.sh
```

手动逐步测试：

### 7.1 创建学生

```bash
curl -X POST http://localhost:8081/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "2024001",
    "name": "张三",
    "major": "计算机科学与技术",
    "grade": 2024,
    "email": "zhangsan@example.edu.cn"
  }'
```

### 7.2 创建课程

```bash
curl -X POST http://localhost:8082/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "code": "CS101",
    "title": "计算机科学导论",
    "instructor": {
      "id": "T001",
      "name": "张教授",
      "email": "zhang@example.edu.cn"
    },
    "schedule": {
      "dayOfWeek": "MONDAY",
      "startTime": "08:00",
      "endTime": "10:00",
      "expectedAttendance": 50
    },
    "capacity": 60,
    "enrolled": 0
  }'
```

### 7.3 选课（服务间通信）

```bash
curl -X POST http://localhost:8083/api/enrollments \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": "CS101",
    "studentId": "2024001"
  }'
```

**期望返回**

```json
{"code":201,"message":"Enrolled","data":{...}}
```

### 7.4 查询选课记录

```bash
curl http://localhost:8083/api/enrollments
```

## 8. 遇到的问题与解决方案

| 问题                             | 原因                        | 解决                                  |
| :------------------------------- | :-------------------------- | :------------------------------------ |
| 服务间调用 404                   | 容器内 `localhost` 指向自己 | 改用服务名 `http://user-service:8081` |
| 选课失败“Course not found: null” | DTO 字段名与 JSON 不匹配    | 统一用 `courseId`                     |
| 数据库启动失败                   | 健康检查超时                | 增加 `interval/retries`               |
| 端口冲突                         | 本地已占用                  | 修改 `docker-compose.yml` 端口映射    |