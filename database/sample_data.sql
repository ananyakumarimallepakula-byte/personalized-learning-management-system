-- =====================================================================
-- SAMPLE DATA: Personalized Student Learning & Weak-Topic Prediction
-- Target Database: MySQL 8.0+
-- Passwords: BCrypt hashed 'admin123', 'teacher123', 'student123'
-- =====================================================================

USE personalized_learning_db;

-- 1. SEED USERS
-- Password for all: admin123 / teacher123 / student123
INSERT INTO users (id, name, email, password, role, phone, active, created_at) VALUES
(1, 'System Administrator', 'admin@learning.com', '$2a$10$wT/tYp4.Yq2L2Y23MvYdEOzYqTqfS4xO5q12U79M.Fv1.y2B0k1cW', 'ADMIN', '9876543210', TRUE, NOW()),
(2, 'Dr. Robert Smith', 'teacher@learning.com', '$2a$10$QjJtT43m6v.jO1Yx/l.N8OC6p/1NZyVp1W0B5Z8Z6P3Z4Z9Q8Y5eW', 'TEACHER', '9876543211', TRUE, NOW()),
(3, 'Alex Johnson', 'student@learning.com', '$2a$10$Y1r1H2Z1e4J7N3K2V9X8Ce5q9O7B4G1K0M2P3Q4R5S6T7U8V9W0Xy', 'STUDENT', '9876543212', TRUE, NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 2. SEED STUDENT PROFILE
INSERT INTO student_profiles (id, user_id, college_name, course, year_of_study, profile_image) VALUES
(1, 3, 'National Institute of Technology', 'Computer Science & Engineering', 3, 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150')
ON DUPLICATE KEY UPDATE college_name=VALUES(college_name);

-- 3. SEED SUBJECTS
INSERT INTO subjects (id, name, description, created_by, created_at, active) VALUES
(1, 'Java Programming', 'Core Java, OOP principles, collections, streams, and multithreading.', 2, NOW(), TRUE),
(2, 'Database Management Systems', 'Relational database concepts, SQL queries, normalization, and ACID properties.', 2, NOW(), TRUE),
(3, 'Operating Systems', 'Process scheduling, thread concurrency, memory paging, and deadlock mitigation.', 2, NOW(), TRUE),
(4, 'Computer Networks', 'Network layering, IP addressing, TCP/UDP protocols, routing, and network security.', 2, NOW(), TRUE)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 4. SEED TOPICS
INSERT INTO topics (id, subject_id, name, description, difficulty, created_at, active) VALUES
-- Java Topics
(1, 1, 'OOP Concepts', 'Fundamental paradigms: Encapsulation, Abstraction, Polymorphism, and Inheritance.', 'EASY', NOW(), TRUE),
(2, 1, 'Java Inheritance', 'Extending classes, method overriding, super keyword, and polymorphic dispatch.', 'HARD', NOW(), TRUE),
(3, 1, 'Collections Framework', 'Lists, Sets, Maps, Queues, Iterators, and sorting with Comparable/Comparator.', 'MEDIUM', NOW(), TRUE),
(4, 1, 'Exception Handling', 'Checked vs unchecked exceptions, try-catch-finally, and custom exceptions.', 'MEDIUM', NOW(), TRUE),
(5, 1, 'Multithreading & Concurrency', 'Thread life cycle, synchronization, locks, and ExecutorService.', 'HARD', NOW(), TRUE),

-- DBMS Topics
(6, 2, 'SQL Queries & Joins', 'DML, DDL, INNER/OUTER/CROSS joins, subqueries, and aggregate functions.', 'MEDIUM', NOW(), TRUE),
(7, 2, 'Database Normalization', '1NF, 2NF, 3NF, BCNF, functional dependencies, and lossless decomposition.', 'HARD', NOW(), TRUE),
(8, 2, 'Transactions & Concurrency', 'ACID properties, serializability, two-phase locking, and isolation levels.', 'HARD', NOW(), TRUE),
(9, 2, 'Indexing & B-Trees', 'Clustered vs non-clustered indexes, B+ Tree search, and query optimization.', 'MEDIUM', NOW(), TRUE),
(10, 2, 'ER Modeling', 'Entities, attributes, relationships, cardinalities, and relational mapping.', 'EASY', NOW(), TRUE),

-- OS Topics
(11, 3, 'Deadlocks & Prevention', 'Coffman conditions, Banker''s algorithm, resource allocation graphs, and detection.', 'HARD', NOW(), TRUE),
(12, 3, 'Process Scheduling', 'FCFS, SJF, Round Robin, priority scheduling, context switching, and PCB.', 'MEDIUM', NOW(), TRUE),
(13, 3, 'Virtual Memory & Paging', 'Page tables, TLB, demand paging, FIFO, LRU page replacement policies.', 'HARD', NOW(), TRUE),
(14, 3, 'Process Synchronization', 'Critical section problem, semaphores, mutexes, monitors, and race conditions.', 'HARD', NOW(), TRUE),
(15, 3, 'File Systems', 'File allocation tables, inodes, contiguous vs linked allocation, and directory structures.', 'EASY', NOW(), TRUE),

-- CN Topics
(16, 4, 'Routing Algorithms', 'Distance vector routing, link-state routing (Dijkstra), OSPF, and BGP.', 'MEDIUM', NOW(), TRUE),
(17, 4, 'OSI & TCP/IP Models', '7-layer OSI model, encapsulation, packet framing, and protocol stacks.', 'EASY', NOW(), TRUE),
(18, 4, 'IP Addressing & Subnetting', 'IPv4 classes, CIDR notation, subnet masks, and NAT translation.', 'MEDIUM', NOW(), TRUE),
(19, 4, 'Transport Layer Protocols', 'TCP three-way handshake, flow control, congestion window, and UDP.', 'MEDIUM', NOW(), TRUE),
(20, 4, 'Network Security & Cryptography', 'Symmetric vs asymmetric encryption, RSA, SSL/TLS handshakes, and firewalls.', 'HARD', NOW(), TRUE)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 5. SEED STUDY MATERIALS
INSERT INTO study_materials (id, topic_id, title, description, material_type, content_url, content, difficulty, created_by, created_at, active) VALUES
(1, 2, 'Comprehensive Java Inheritance Guide & Notes', 'Key principles of class extension, method overriding, super() call semantics, and diamond problem resolution in Java interfaces.', 'NOTES', 'https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html', 'Inheritance allows a new class to inherit attributes and methods of an existing class. In Java, use the extends keyword. Multiple class inheritance is prohibited to avoid ambiguity (the diamond problem). Interfaces allow multiple inheritance of type.', 'MEDIUM', 2, NOW(), TRUE),
(2, 2, 'Mastering Polymorphism & Inheritance Video Lecture', 'High-yield video walkthrough explaining runtime polymorphism, dynamic method dispatch, and abstract classes.', 'VIDEO', 'https://www.youtube.com/watch?v=4bm_H1gTj58', 'Visual explanation of stack and heap allocation during polymorphic method calls.', 'HARD', 2, NOW(), TRUE),
(3, 2, 'Inheritance & Polymorphism Cheat Sheet (PDF)', 'Printable summary of OOP inheritance rules, casting rules, and best practices.', 'PDF', 'https://example.com/materials/java-inheritance-cheatsheet.pdf', 'Summary PDF covering upcasting, downcasting, and instanceof operator.', 'EASY', 2, NOW(), TRUE),
(4, 6, 'SQL Complex Joins and Aggregations Manual', 'In-depth tutorial on INNER, LEFT, RIGHT, FULL OUTER joins and GROUP BY/HAVING clauses with sample schemas.', 'NOTES', 'https://www.w3schools.com/sql/sql_join.asp', 'JOIN clause is used to combine rows from two or more tables based on a related column between them. Learn how NULL handles in outer joins.', 'MEDIUM', 2, NOW(), TRUE),
(5, 6, 'Advanced SQL Query Optimization Video', 'Step-by-step video on subquery restructuring, EXPLAIN plans, and indexes.', 'VIDEO', 'https://www.youtube.com/watch?v=BHwzDkyfdNE', 'Video detailing how database query planners choose execution paths.', 'HARD', 2, NOW(), TRUE),
(6, 11, 'Deadlock Conditions & Banker''s Algorithm Notes', 'Clear breakdown of Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait, and Banker''s safe state verification.', 'NOTES', 'https://www.geeksforgeeks.org/bankers-algorithm-in-operating-system-2/', 'Deadlock occurs when every process is waiting for a resource held by another waiting process. Banker''s Algorithm determines if allocating resources maintains a safe state.', 'HARD', 2, NOW(), TRUE)
ON DUPLICATE KEY UPDATE title=VALUES(title);

-- 6. SEED QUIZZES
INSERT INTO quizzes (id, subject_id, topic_id, title, description, difficulty, duration_minutes, total_marks, created_by, created_at, active) VALUES
(1, 1, 2, 'Java Inheritance Mastery Quiz', '10-question evaluation on inheritance hierarchies, overriding, and keywords.', 'MEDIUM', 15, 10, 2, NOW(), TRUE),
(2, 2, 6, 'SQL Queries & Database Joins Quiz', 'Test your knowledge of SQL queries, grouping, and relational joins.', 'MEDIUM', 15, 10, 2, NOW(), TRUE),
(3, 3, 11, 'Deadlocks & Banker''s Algorithm Quiz', 'Evaluating Coffman conditions, safe state algorithms, and prevention.', 'HARD', 15, 10, 2, NOW(), TRUE)
ON DUPLICATE KEY UPDATE title=VALUES(title);

-- 7. SEED QUESTIONS
INSERT INTO questions (id, quiz_id, topic_id, question_text, difficulty, marks, explanation, created_at) VALUES
(1, 1, 2, 'Which keyword is used in Java to inherit a class?', 'EASY', 1, 'The extends keyword specifies that a class is derived from a parent class.', NOW()),
(2, 1, 2, 'What happens if a subclass defines a method with the exact same name and signature as its superclass?', 'MEDIUM', 1, 'This is known as method overriding (runtime polymorphism).', NOW()),
(3, 1, 2, 'Can a class extend more than one class directly in Java?', 'EASY', 1, 'Java does not support multiple class inheritance to avoid the diamond problem.', NOW()),
(4, 1, 2, 'Which keyword refers to the direct superclass constructor from inside a subclass constructor?', 'MEDIUM', 1, 'super() invokes the parent class constructor.', NOW()),
(5, 1, 2, 'What is the access level required for an overriding method in a subclass?', 'HARD', 1, 'An overriding method cannot reduce the visibility of the parent method.', NOW()),
(6, 1, 2, 'Which of the following classes cannot be subclassed in Java?', 'MEDIUM', 1, 'A final class cannot be extended by any other class.', NOW()),
(7, 1, 2, 'What is the return type restriction when overriding a method in modern Java (Java 5+)?', 'HARD', 1, 'Java allows covariant return types where the subclass method can return a subtype.', NOW()),
(8, 1, 2, 'What is the default superclass of all classes in Java?', 'EASY', 1, 'java.lang.Object is the root of the class hierarchy.', NOW()),
(9, 1, 2, 'Can constructors be inherited in Java?', 'MEDIUM', 1, 'Constructors are not inherited by subclasses, though they can be called via super().', NOW()),
(10, 1, 2, 'What does dynamic method dispatch achieve in Java?', 'HARD', 1, 'It resolves polymorphic method calls at runtime based on the actual object type.', NOW()),

(11, 2, 6, 'Which SQL join returns all rows from both tables, filling with NULL where matches don''t exist?', 'MEDIUM', 1, 'FULL OUTER JOIN preserves unmatched rows from both sides.', NOW()),
(12, 2, 6, 'Which clause is used to filter groups created by GROUP BY?', 'EASY', 1, 'HAVING filters aggregated groups, whereas WHERE filters individual rows.', NOW()),
(13, 2, 6, 'What is the default sorting order of the ORDER BY clause?', 'EASY', 1, 'ASC (Ascending) is the default order.', NOW()),
(14, 2, 6, 'Which aggregate function ignores NULL values by default?', 'MEDIUM', 1, 'Aggregate functions like AVG(), SUM(), and COUNT(column) ignore NULL values.', NOW()),
(15, 2, 6, 'What type of join is produced when two tables are joined without any ON or WHERE condition?', 'HARD', 1, 'Cartesian product or CROSS JOIN.', NOW()),

(16, 3, 11, 'Which of the following is NOT one of the four Coffman deadlock conditions?', 'MEDIUM', 1, 'Preemption is the prevention strategy, whereas NO PREEMPTION is the deadlock condition.', NOW()),
(17, 3, 11, 'What is the primary objective of Banker''s Algorithm in an Operating System?', 'HARD', 1, 'Banker''s Algorithm prevents deadlocks by ensuring the system remains in a safe state.', NOW()),
(18, 3, 11, 'A system is in a safe state if:', 'HARD', 1, 'A safe sequence exists where all processes can finish without deadlock.', NOW())
ON DUPLICATE KEY UPDATE question_text=VALUES(question_text);

-- 8. SEED QUESTION OPTIONS (4 per question)
INSERT INTO question_options (id, question_id, option_text, is_correct) VALUES
(1, 1, 'extends', TRUE), (2, 1, 'implements', FALSE), (3, 1, 'inherits', FALSE), (4, 1, 'instanceof', FALSE),
(5, 2, 'Method Overriding', TRUE), (6, 2, 'Method Overloading', FALSE), (7, 2, 'Compile Error', FALSE), (8, 2, 'Operator Hiding', FALSE),
(9, 3, 'No, Java supports single class inheritance only', TRUE), (10, 3, 'Yes, using comma separation', FALSE), (11, 3, 'Yes, up to 3 classes', FALSE), (12, 3, 'Only if all parent classes are abstract', FALSE),
(13, 4, 'super()', TRUE), (14, 4, 'this()', FALSE), (15, 4, 'parent()', FALSE), (16, 4, 'base()', FALSE),
(17, 5, 'Equal or more permissive than the superclass method', TRUE), (18, 5, 'Equal or less permissive than the superclass method', FALSE), (19, 5, 'Must always be public', FALSE), (20, 5, 'Must match exactly', FALSE),
(21, 6, 'A class declared as final', TRUE), (22, 6, 'An abstract class', FALSE), (23, 6, 'A static nested class', FALSE), (24, 6, 'A public class', FALSE),
(25, 7, 'Can be the same or a subtype (covariant return type)', TRUE), (26, 7, 'Must be an identical type strictly', FALSE), (27, 7, 'Can be any Object type', FALSE), (28, 7, 'Must return void', FALSE),
(29, 8, 'java.lang.Object', TRUE), (30, 8, 'java.lang.Class', FALSE), (31, 8, 'java.lang.System', FALSE), (32, 8, 'java.lang.Base', FALSE),
(33, 9, 'No, constructors are not inherited', TRUE), (34, 9, 'Yes, automatically', FALSE), (35, 9, 'Only default constructors are inherited', FALSE), (36, 9, 'Only public constructors are inherited', FALSE),
(37, 10, 'Runtime Polymorphism resolution', TRUE), (38, 10, 'Compile-time macro inlining', FALSE), (39, 10, 'Memory deallocation', FALSE), (40, 10, 'Thread synchronization', FALSE),

(41, 11, 'FULL OUTER JOIN', TRUE), (42, 11, 'INNER JOIN', FALSE), (43, 11, 'LEFT JOIN', FALSE), (44, 11, 'NATURAL JOIN', FALSE),
(45, 12, 'HAVING', TRUE), (46, 12, 'WHERE', FALSE), (47, 12, 'FILTER', FALSE), (48, 12, 'ORDER BY', FALSE),
(49, 13, 'ASC (Ascending)', TRUE), (50, 13, 'DESC (Descending)', FALSE), (51, 13, 'Random', FALSE), (52, 13, 'Insertion Order', FALSE),
(53, 14, 'AVG() and SUM()', TRUE), (54, 14, 'COUNT(*)', FALSE), (55, 14, 'All aggregate functions include NULL', FALSE), (56, 14, 'None', FALSE),
(57, 15, 'Cartesian Product (CROSS JOIN)', TRUE), (58, 15, 'SELF JOIN', FALSE), (59, 15, 'EQUI JOIN', FALSE), (60, 15, 'ANTI JOIN', FALSE),

(61, 16, 'Preemption', TRUE), (62, 16, 'Mutual Exclusion', FALSE), (63, 16, 'Hold and Wait', FALSE), (64, 16, 'Circular Wait', FALSE),
(65, 17, 'Deadlock Avoidance by verifying Safe State', TRUE), (66, 17, 'Deadlock Detection after occurrence', FALSE), (67, 17, 'CPU Process Scheduling', FALSE), (68, 17, 'Disk Cache Optimization', FALSE),
(69, 18, 'There exists at least one safe execution sequence', TRUE), (70, 18, 'No resources are currently allocated', FALSE), (71, 18, 'All processes are blocked', FALSE), (72, 18, 'Deadlock has already occurred', FALSE)
ON DUPLICATE KEY UPDATE option_text=VALUES(option_text);

-- 9. SEED STUDENT TOPIC PERFORMANCE (Matching section 28 requirement)
-- Java OOP              82% (STRONG)
-- Java Inheritance      45% (WEAK)
-- Java Collections      78% (STRONG)
-- DBMS SQL              52% (WEAK)
-- DBMS Normalization    76% (STRONG)
-- Operating Systems     58% (WEAK)
-- Computer Networks     85% (STRONG)
INSERT INTO topic_performance (student_id, topic_id, total_attempts, total_questions, correct_answers, wrong_answers, average_score, average_percentage, last_attempt_at, trend) VALUES
(3, 1, 3, 30, 25, 5, 8.20, 82.00, DATE_SUB(NOW(), INTERVAL 2 DAY), 'IMPROVING'),
(3, 2, 2, 20, 9, 11, 4.50, 45.00, DATE_SUB(NOW(), INTERVAL 1 DAY), 'DECLINING'),
(3, 3, 3, 30, 23, 7, 7.80, 78.00, DATE_SUB(NOW(), INTERVAL 3 DAY), 'STABLE'),
(3, 6, 2, 20, 10, 10, 5.20, 52.00, DATE_SUB(NOW(), INTERVAL 4 DAY), 'DECLINING'),
(3, 7, 3, 30, 23, 7, 7.60, 76.00, DATE_SUB(NOW(), INTERVAL 5 DAY), 'IMPROVING'),
(3, 11, 2, 20, 12, 8, 5.80, 58.00, DATE_SUB(NOW(), INTERVAL 6 DAY), 'DECLINING'),
(3, 16, 3, 30, 26, 4, 8.50, 85.00, DATE_SUB(NOW(), INTERVAL 7 DAY), 'IMPROVING')
ON DUPLICATE KEY UPDATE average_percentage=VALUES(average_percentage);

-- 10. SEED WEAK TOPIC PREDICTIONS
INSERT INTO weak_topic_predictions (student_id, topic_id, prediction_score, risk_level, reason, predicted_at) VALUES
(3, 2, 78.00, 'HIGH', 'Historical average is low (45.0%); Performance trend is declining over recent attempts; High wrong answer frequency (11 wrong out of 20)', NOW()),
(3, 6, 72.50, 'HIGH', 'Historical average is low (52.0%); Performance trend is declining over recent attempts; High wrong answer frequency (10 wrong out of 20)', NOW()),
(3, 11, 58.50, 'MEDIUM', 'Historical average is below threshold (58.0%); Performance trend is declining', NOW()),
(3, 1, 22.00, 'LOW', 'Consistent steady progress observed on this topic', NOW())
ON DUPLICATE KEY UPDATE prediction_score=VALUES(prediction_score);

-- 11. SEED RECOMMENDATIONS
INSERT INTO recommendations (student_id, topic_id, material_id, priority, reason, recommended_at, completed) VALUES
(3, 2, 1, 'HIGH', 'Recommended due to HIGH risk in Java Inheritance', NOW(), FALSE),
(3, 2, 2, 'HIGH', 'Recommended due to HIGH risk in Java Inheritance', NOW(), FALSE),
(3, 6, 4, 'HIGH', 'Recommended due to HIGH risk in SQL Queries & Joins', NOW(), FALSE),
(3, 11, 6, 'MEDIUM', 'Recommended due to MEDIUM risk in Deadlocks & Prevention', NOW(), FALSE)
ON DUPLICATE KEY UPDATE priority=VALUES(priority);

-- 12. SEED RECOMMENDED PRACTICE QUESTIONS
INSERT INTO recommended_questions (student_id, question_id, topic_id, priority, reason, completed, created_at) VALUES
(3, 5, 2, 'HIGH', 'Personalized practice for weak topic Java Inheritance', FALSE, NOW()),
(3, 7, 2, 'HIGH', 'Personalized practice for weak topic Java Inheritance', FALSE, NOW()),
(3, 10, 2, 'HIGH', 'Personalized practice for weak topic Java Inheritance', FALSE, NOW()),
(3, 11, 6, 'HIGH', 'Personalized practice for weak topic SQL Queries & Joins', FALSE, NOW()),
(3, 15, 6, 'HIGH', 'Personalized practice for weak topic SQL Queries & Joins', FALSE, NOW())
ON DUPLICATE KEY UPDATE priority=VALUES(priority);
