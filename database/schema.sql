-- =====================================================================
-- DATABASE SCHEMA: Personalized Student Learning & Weak-Topic Prediction
-- Target Database: MySQL 8.0+
-- =====================================================================

CREATE DATABASE IF NOT EXISTS personalized_learning_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE personalized_learning_db;

-- 1. USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL, -- STUDENT, TEACHER, ADMIN
    phone VARCHAR(20),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_email (email),
    INDEX idx_user_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. STUDENT PROFILES TABLE
CREATE TABLE IF NOT EXISTS student_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    college_name VARCHAR(200),
    course VARCHAR(100),
    year_of_study INT,
    profile_image VARCHAR(500),
    CONSTRAINT fk_student_profile_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. SUBJECTS TABLE
CREATE TABLE IF NOT EXISTS subjects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    created_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX idx_subject_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. TOPICS TABLE
CREATE TABLE IF NOT EXISTS topics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subject_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    difficulty VARCHAR(20) DEFAULT 'MEDIUM', -- EASY, MEDIUM, HARD
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_topic_subject FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE,
    INDEX idx_topic_subject (subject_id),
    INDEX idx_topic_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. STUDY MATERIALS TABLE
CREATE TABLE IF NOT EXISTS study_materials (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    topic_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    material_type VARCHAR(30) NOT NULL, -- NOTES, PDF, VIDEO, ARTICLE, LINK
    content_url VARCHAR(500),
    content TEXT,
    difficulty VARCHAR(20) DEFAULT 'MEDIUM',
    created_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_material_topic FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE,
    INDEX idx_material_topic (topic_id),
    INDEX idx_material_type (material_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. QUIZZES TABLE
CREATE TABLE IF NOT EXISTS quizzes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subject_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    difficulty VARCHAR(20) DEFAULT 'MEDIUM',
    duration_minutes INT DEFAULT 15,
    total_marks INT DEFAULT 10,
    created_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_quiz_subject FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE,
    CONSTRAINT fk_quiz_topic FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE,
    INDEX idx_quiz_topic (topic_id),
    INDEX idx_quiz_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. QUESTIONS TABLE
CREATE TABLE IF NOT EXISTS questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quiz_id BIGINT NULL,
    topic_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    difficulty VARCHAR(20) DEFAULT 'MEDIUM',
    marks INT DEFAULT 1,
    explanation TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_question_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes (id) ON DELETE SET NULL,
    CONSTRAINT fk_question_topic FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE,
    INDEX idx_question_quiz (quiz_id),
    INDEX idx_question_topic (topic_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. QUESTION OPTIONS TABLE
CREATE TABLE IF NOT EXISTS question_options (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT NOT NULL,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_option_question FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE,
    INDEX idx_option_question (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. QUIZ ATTEMPTS TABLE
CREATE TABLE IF NOT EXISTS quiz_attempts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    quiz_id BIGINT NOT NULL,
    started_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    submitted_at DATETIME,
    total_questions INT NOT NULL DEFAULT 0,
    correct_answers INT NOT NULL DEFAULT 0,
    wrong_answers INT NOT NULL DEFAULT 0,
    score DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    time_taken_seconds INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_attempt_student FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_attempt_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes (id) ON DELETE CASCADE,
    INDEX idx_attempt_student (student_id),
    INDEX idx_attempt_quiz (quiz_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. STUDENT ANSWERS TABLE
CREATE TABLE IF NOT EXISTS student_answers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    attempt_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option_id BIGINT,
    correct BOOLEAN NOT NULL DEFAULT FALSE,
    marks_obtained DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_student_ans_attempt FOREIGN KEY (attempt_id) REFERENCES quiz_attempts (id) ON DELETE CASCADE,
    CONSTRAINT fk_student_ans_question FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE,
    INDEX idx_student_ans_attempt (attempt_id),
    INDEX idx_student_ans_question (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 11. TOPIC PERFORMANCE TABLE
CREATE TABLE IF NOT EXISTS topic_performance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    total_attempts INT NOT NULL DEFAULT 0,
    total_questions INT NOT NULL DEFAULT 0,
    correct_answers INT NOT NULL DEFAULT 0,
    wrong_answers INT NOT NULL DEFAULT 0,
    average_score DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    average_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    last_attempt_at DATETIME,
    trend VARCHAR(20) DEFAULT 'STABLE', -- IMPROVING, DECLINING, STABLE
    CONSTRAINT fk_topic_perf_student FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_topic_perf_topic FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE,
    UNIQUE KEY uq_student_topic_perf (student_id, topic_id),
    INDEX idx_topic_perf_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 12. WEAK TOPIC PREDICTIONS TABLE
CREATE TABLE IF NOT EXISTS weak_topic_predictions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    prediction_score DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    risk_level VARCHAR(20) NOT NULL DEFAULT 'LOW', -- LOW, MEDIUM, HIGH
    reason TEXT,
    predicted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pred_student FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_pred_topic FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE,
    INDEX idx_pred_student (student_id),
    INDEX idx_pred_risk (risk_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 13. RECOMMENDATIONS TABLE
CREATE TABLE IF NOT EXISTS recommendations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    material_id BIGINT NULL,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM', -- HIGH, MEDIUM, LOW
    reason TEXT,
    recommended_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_rec_student FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_rec_topic FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE,
    CONSTRAINT fk_rec_material FOREIGN KEY (material_id) REFERENCES study_materials (id) ON DELETE SET NULL,
    INDEX idx_rec_student (student_id),
    INDEX idx_rec_priority (priority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 14. RECOMMENDED QUESTIONS TABLE
CREATE TABLE IF NOT EXISTS recommended_questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    reason TEXT,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rec_q_student FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_rec_q_question FOREIGN KEY (question_id) REFERENCES questions (id) ON DELETE CASCADE,
    CONSTRAINT fk_rec_q_topic FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE,
    INDEX idx_rec_q_student (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
