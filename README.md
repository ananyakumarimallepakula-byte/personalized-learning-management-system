# Personalized Student Learning and Weak-Topic Prediction System

A complete, production-grade Java Full Stack web application that analyzes student quiz performance, evaluation history, and learning behavior to identify weak topics, predict academic risk scores using a multi-factor regression model, and generate personalized remedial study recommendations.

---

## 1. Project Overview

Modern learning management platforms often serve uniform content regardless of individual student mastery. This project solves that bottleneck by creating an automated **Learning Diagnostics & Remediation Feedback Loop**:

1. **Diagnostic Assessment**: Students take modular, topic-specific quizzes.
2. **Automatic Evaluation & Performance Tracking**: Each submission updates cumulative accuracy, question logs, and topic mastery status (`STRONG` $\ge 75\%$, `AVERAGE` $60-75\%$, `WEAK` $< 60\%$).
3. **Multi-Factor Predictive Risk Modeling**: A rule-based 5-factor regression model calculates an exact **Risk Score (0–100)** to forecast whether a student is likely to fail future assessments on that topic (`LOW`, `MEDIUM`, `HIGH`).
4. **Personalized Recommendations Engine**: Dynamically curates remedial study materials (notes, video lectures, articles) and focused practice questions targeting high-priority weak areas.
5. **Multi-Role Portals**: Complete, role-governed portals for **Students**, **Teachers**, and **Administrators**.

---

## 2. Technology Stack

* **Backend**: Java 17+, Spring Boot 3.2.5
* **Persistence & ORM**: Spring Data JPA, Hibernate 6
* **Database**: MySQL 8.0+ (`personalized_learning_db`)
* **Security**: Spring Security 6 (Stateless, BCrypt password hashing, CORS enabled)
* **Design Patterns**: Controller-Service-Repository, Strategy Pattern (`PredictionStrategy`), Data Transfer Objects (DTO), Custom Global Exception Handling
* **Frontend**: HTML5, Modern CSS3, JavaScript (ES6+ Fetch API), Bootstrap 5.3, Chart.js, Bootstrap Icons
* **Build System**: Apache Maven 3.9+

---

## 3. System Architecture & Component Design

```
+-----------------------------------------------------------------------------------+
|                                 FRONTEND TIER                                     |
|  - Student Portal (Dashboard, Quizzes, Weak Topics, Risk Engine, Recommendations) |
|  - Teacher Portal (Classroom Mastery, Question Bank, Quiz Authoring, Analytics)   |
|  - Admin Console  (User Governance, Catalog Oversight, Algorithm Parameter Tuning)|
+------------------------------------------+----------------------------------------+
                                           | HTTP / RESTful JSON
                                           v
+-----------------------------------------------------------------------------------+
|                             SPRING BOOT REST CONTROLLERS                          |
|  AuthController | StudentController | TeacherController | AdminController         |
|  SubjectController | TopicController | QuizController | QuestionController        |
|  PerformanceController | PredictionController | RecommendationController          |
+------------------------------------------+----------------------------------------+
                                           | Service Layer
                                           v
+-----------------------------------------------------------------------------------+
|                                  BUSINESS LOGIC                                   |
|  - PerformanceCalculator: Aggregates accuracy, assigns STRONG/AVERAGE/WEAK        |
|  - PredictionCalculator: 5-Factor Weighted Risk Score (0-100)                     |
|  - Strategy Pattern: PredictionStrategy -> RuleBasedPredictionStrategy            |
|  - RecommendationEngine: High/Medium priority content & targeted question practice|
+------------------------------------------+----------------------------------------+
                                           | Spring Data JPA / Hibernate
                                           v
+-----------------------------------------------------------------------------------+
|                                 PERSISTENCE TIER                                  |
|  MySQL Database (`personalized_learning_db`) - 14 Relational Tables with FKs      |
+-----------------------------------------------------------------------------------+
```

---

## 4. Multi-Factor Weak-Topic Prediction Algorithm

The predictive engine does not rely solely on the latest quiz score. It evaluates 5 weighted parameters to assign an academic risk score:

$$\text{Risk Score} = (0.40 \times F_{\text{hist}}) + (0.25 \times F_{\text{recent}}) + (0.15 \times F_{\text{wrong}}) + (0.10 \times F_{\text{trend}}) + (0.10 \times F_{\text{inact}})$$

### Factor Breakdown:
1. **$F_{\text{hist}}$ (Historical Average Score - 40%)**:
   - $F_{\text{hist}} = 100 - \text{Historical Average Percentage}$
2. **$F_{\text{recent}}$ (Most Recent Quiz Score - 25%)**:
   - $F_{\text{recent}} = 100 - \text{Latest Quiz Percentage}$
3. **$F_{\text{wrong}}$ (Wrong Answers Ratio - 15%)**:
   - $F_{\text{wrong}} = \left(\frac{\text{Total Wrong Answers}}{\text{Total Questions Attempted}}\right) \times 100$
4. **$F_{\text{trend}}$ (Score Progression Trend - 10%)**:
   - Measures change across sequential attempts. A steep drop yields 85–100 risk, flat performance yields 50 risk, improvement drops risk to 15.
5. **$F_{\text{inact}}$ (Inactivity & Days Gap - 10%)**:
   - Calculates elapsed days since last practice. Gaps $> 30$ days yield 100 risk; 14–30 days yield 60 risk; $< 7$ days yield 10 risk.

### Risk Classification:
* **HIGH RISK**: $\text{Risk Score} \ge 70$ (Urgent remedial action needed)
* **MEDIUM RISK**: $50 \le \text{Risk Score} < 70$ (Monitoring & targeted revision)
* **LOW RISK**: $\text{Risk Score} < 50$ (Adequate mastery)

---

## 5. Database Schema (14 Relational Tables)

1. `users`: Core authentication identity table (name, email, password_hash, role, active).
2. `student_profiles`: Academic details (college, course, year_of_study).
3. `subjects`: Academic subjects (Data Structures, DBMS, Java, etc.).
4. `topics`: Granular curriculum topics under each subject.
5. `study_materials`: Learning resources (Notes, Videos, PDFs, Articles).
6. `quizzes`: Timed diagnostic evaluations per topic.
7. `questions`: Multiple-choice questions mapped to quizzes and topics.
8. `question_options`: Answer choices (A, B, C, D) with correctness indicator.
9. `quiz_attempts`: Student submission sessions (score, percentage, time spent).
10. `student_answers`: Granular answer choices recorded per question.
11. `topic_performance`: Cumulative student mastery stats per topic.
12. `weak_topic_predictions`: Computed 5-factor risk scores and risk levels.
13. `recommendations`: Remedial action items assigned to students.
14. `recommended_questions`: Specific questions prescribed for practice.

---

## 6. Pre-Configured Demo Accounts

| Role | Email | Password |
|---|---|---|
| **Admin** | `admin@learning.com` | `admin123` |
| **Teacher** | `teacher@learning.com` | `teacher123` |
| **Student (Benchmark Demo)** | `student@learning.com` | `student123` |

---

## 7. Setup & Execution Guide

### Prerequisites
* **Java**: JDK 17 or higher
* **Build Tool**: Apache Maven 3.8+
* **Database**: MySQL Server 8.0+
* **MySQL Workbench** or MySQL CLI

---

### Step 1: Database Initialization
1. Open MySQL Workbench or your MySQL terminal.
2. Run the SQL schema script:
   ```sql
   SOURCE database/schema.sql;
   ```
3. Run the sample dataset script:
   ```sql
   SOURCE database/sample_data.sql;
   ```
4. Verify that tables are created under the database `personalized_learning_db`.

---

### Step 2: Configure Database Credentials
Edit `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/personalized_learning_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```
*(Note: If MySQL is not running locally during development or testing, the project defaults gracefully to in-memory H2 mode for zero-configuration verification).*

---

### Step 3: Run the Application

#### Option A: Running with Maven (Command Line)
```bash
# Navigate to backend directory
cd backend

# Build and run the Spring Boot application
mvn spring-boot:run
```

#### Option B: Running in IDE (IntelliJ IDEA / Eclipse / VS Code)
1. Open the project root or the `backend` folder in IntelliJ IDEA or Eclipse.
2. Allow Maven to import dependencies.
3. Locate `PersonalizedLearningApplication.java` under `src/main/java/com/personalizedlearning/`.
4. Right-click and choose **Run 'PersonalizedLearningApplication'**.

---

### Step 4: Accessing the Application

* **Unified Web App (via Spring Boot)**:
  Open your web browser and navigate to:
  `http://localhost:8080/`
* **Direct Portal URLs**:
  - Home / Landing: `http://localhost:8080/index.html`
  - Login: `http://localhost:8080/login.html`
  - Student Dashboard: `http://localhost:8080/student/dashboard.html`
  - Teacher Dashboard: `http://localhost:8080/teacher/dashboard.html`
  - Admin Console: `http://localhost:8080/admin/dashboard.html`

* **Decoupled Frontend**:
  You can also serve the `frontend/` directory with VS Code Live Server or Nginx on any port (e.g. `http://localhost:5500`), and it will seamlessly communicate with the backend at `http://localhost:8080/api` through CORS.

---

## 8. REST API Specification

| HTTP Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/login` | Authenticate user & return role profile |
| `POST` | `/api/auth/register` | Register new student or teacher |
| `GET` | `/api/students/{id}/dashboard` | Retrieve complete student diagnostic dashboard |
| `GET` | `/api/students/{id}/performance` | Retrieve student's topic mastery ledger |
| `GET` | `/api/students/{id}/predictions` | Retrieve student's weak-topic predictions |
| `GET` | `/api/students/{id}/recommendations` | Retrieve personalized remedial recommendations |
| `GET` | `/api/quizzes` | List all available diagnostic quizzes |
| `GET` | `/api/quizzes/{id}` | Retrieve quiz details and questions |
| `POST` | `/api/quizzes/{id}/submit` | Submit quiz answers and trigger prediction loop |
| `POST` | `/api/quizzes?createdBy={id}` | Author and publish a new quiz with questions |
| `GET` | `/api/subjects` | List academic subjects |
| `GET` | `/api/topics` | List curriculum topics |
| `GET` | `/api/materials` | List study materials and notes |
| `GET` | `/api/teachers/{id}/dashboard` | Retrieve teacher classroom analytics |
| `GET` | `/api/admin/dashboard` | Retrieve platform-wide health metrics |
| `GET` | `/api/admin/users` | List all users |
| `PUT` | `/api/admin/users/{id}/toggle-status`| Activate or deactivate a user account |

---

## 9. Academic Viva Presentation Highlights

* **Q: Why not use a simple percentage threshold instead of a predictive model?**
  * *A*: A single quiz score only reflects one point in time and can be noisy (e.g. guessing or temporary distraction). Our 5-factor regression model weighs historical consistency (40%), recent performance (25%), wrong answer frequency (15%), learning trend (10%), and inactivity gaps (10%) to predict genuine comprehension failure risk.
* **Q: How does the strategy pattern enhance extensibility?**
  * *A*: The `PredictionStrategy` interface decouples calculation logic from database persistence. The system currently executes `RuleBasedPredictionStrategy`. In the future, a machine-learning model (e.g. Python microservice or ONNX runtime) can be plugged in without changing service or controller code.
* **Q: How is database consistency maintained during quiz submissions?**
  * *A*: The entire quiz evaluation pipeline runs inside a single `@Transactional` method in `QuizAttemptService`. If an error occurs during prediction calculation or recommendation creation, the transaction rolls back cleanly, preventing corrupted or incomplete records.
