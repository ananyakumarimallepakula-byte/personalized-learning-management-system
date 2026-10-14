# Database Setup & Configuration Guide

## Database Name
`personalized_learning_db`

## Scripts Included
1. **`schema.sql`**: Full DDL schema for MySQL 8.0+. Contains 14 relational tables, foreign key constraints, indexes, and utf8mb4 collation.
2. **`sample_data.sql`**: Comprehensive demo dataset pre-configured with:
   - 3 System Roles (Admin, Teacher, Student) with BCrypt-hashed credentials.
   - 4 Subjects (Java Programming, DBMS, Operating Systems, Computer Networks).
   - 20 Curriculum Topics (5 topics per subject).
   - Quizzes, Questions with 4 options each, Explanations, and Study Materials.
   - Exact benchmark topic performance metrics matching project specification:
     - Java OOP: 82% (STRONG)
     - Java Inheritance: 45% (WEAK -> HIGH RISK)
     - Java Collections: 78% (STRONG)
     - DBMS SQL: 52% (WEAK -> HIGH RISK)
     - DBMS Normalization: 76% (STRONG)
     - Operating Systems: 58% (WEAK -> MEDIUM RISK)
     - Computer Networks: 85% (STRONG)

## Step-by-Step Execution in MySQL Workbench
1. Open **MySQL Workbench** and connect to your local MySQL Server instance.
2. Open `schema.sql` (`File -> Open SQL Script...`) and execute the entire script (click the lightning bolt icon).
3. Open `sample_data.sql` and execute it.
4. Verify the tables by executing:
   ```sql
   USE personalized_learning_db;
   SHOW TABLES;
   SELECT * FROM users;
   SELECT * FROM topic_performance;
   ```

## Demo User Credentials
| Role | Email | Password | Description |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin@learning.com` | `admin123` | Full administrative control & system-wide analytics |
| **TEACHER** | `teacher@learning.com` | `teacher123` | Curriculum authoring, quizzes, student analytics |
| **STUDENT** | `student@learning.com` | `student123` | Personalized dashboard, tests, weak-topic predictions |
