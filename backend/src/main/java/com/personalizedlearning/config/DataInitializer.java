package com.personalizedlearning.config;

import com.personalizedlearning.entity.*;
import com.personalizedlearning.repository.*;
import com.personalizedlearning.service.PerformanceAnalysisService;
import com.personalizedlearning.service.WeakTopicPredictionService;
import com.personalizedlearning.service.RecommendationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final StudyMaterialRepository studyMaterialRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final TopicPerformanceRepository topicPerformanceRepository;
    private final WeakTopicPredictionRepository predictionRepository;
    private final RecommendationRepository recommendationRepository;
    private final PasswordEncoder passwordEncoder;
    private final PerformanceAnalysisService performanceAnalysisService;
    private final WeakTopicPredictionService predictionService;
    private final RecommendationService recommendationService;

    public DataInitializer(UserRepository userRepository,
                           StudentProfileRepository studentProfileRepository,
                           SubjectRepository subjectRepository,
                           TopicRepository topicRepository,
                           StudyMaterialRepository studyMaterialRepository,
                           QuizRepository quizRepository,
                           QuestionRepository questionRepository,
                           QuestionOptionRepository questionOptionRepository,
                           QuizAttemptRepository quizAttemptRepository,
                           TopicPerformanceRepository topicPerformanceRepository,
                           WeakTopicPredictionRepository predictionRepository,
                           RecommendationRepository recommendationRepository,
                           PasswordEncoder passwordEncoder,
                           PerformanceAnalysisService performanceAnalysisService,
                           WeakTopicPredictionService predictionService,
                           RecommendationService recommendationService) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.studyMaterialRepository = studyMaterialRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.questionOptionRepository = questionOptionRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.topicPerformanceRepository = topicPerformanceRepository;
        this.predictionRepository = predictionRepository;
        this.recommendationRepository = recommendationRepository;
        this.passwordEncoder = passwordEncoder;
        this.performanceAnalysisService = performanceAnalysisService;
        this.predictionService = predictionService;
        this.recommendationService = recommendationService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // Data already exists
        }

        System.out.println("Initializing sample data for Personalized Learning System...");

        // 1. Create Users
        User admin = new User("System Administrator", "admin@learning.com", passwordEncoder.encode("admin123"), "ADMIN", "9876543210");
        userRepository.save(admin);

        User teacher = new User("Dr. Robert Smith", "teacher@learning.com", passwordEncoder.encode("teacher123"), "TEACHER", "9876543211");
        userRepository.save(teacher);

        User student = new User("Alex Johnson", "student@learning.com", passwordEncoder.encode("student123"), "STUDENT", "9876543212");
        User savedStudent = userRepository.save(student);

        StudentProfile profile = new StudentProfile(savedStudent, "National Institute of Technology", "Computer Science & Engineering", 3, "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150");
        studentProfileRepository.save(profile);
        savedStudent.setStudentProfile(profile);

        // 2. Create Subjects
        Subject subJava = subjectRepository.save(new Subject("Java Programming", "Core Java, OOP principles, collections, streams, and multithreading.", teacher.getId()));
        Subject subDbms = subjectRepository.save(new Subject("Database Management Systems", "Relational database concepts, SQL queries, normalization, and ACID properties.", teacher.getId()));
        Subject subOs = subjectRepository.save(new Subject("Operating Systems", "Process scheduling, thread concurrency, memory paging, and deadlock mitigation.", teacher.getId()));
        Subject subCn = subjectRepository.save(new Subject("Computer Networks", "Network layering, IP addressing, TCP/UDP protocols, routing, and network security.", teacher.getId()));

        // 3. Create Topics
        // Java Topics
        Topic tJavaOop = topicRepository.save(new Topic(subJava, "OOP Concepts", "Fundamental paradigms: Encapsulation, Abstraction, Polymorphism, and Inheritance.", "EASY"));
        Topic tJavaInheritance = topicRepository.save(new Topic(subJava, "Java Inheritance", "Extending classes, method overriding, super keyword, and polymorphic dispatch.", "HARD"));
        Topic tJavaCollections = topicRepository.save(new Topic(subJava, "Collections Framework", "Lists, Sets, Maps, Queues, Iterators, and sorting with Comparable/Comparator.", "MEDIUM"));
        Topic tJavaExceptions = topicRepository.save(new Topic(subJava, "Exception Handling", "Checked vs unchecked exceptions, try-catch-finally, and custom exceptions.", "MEDIUM"));
        Topic tJavaThreads = topicRepository.save(new Topic(subJava, "Multithreading & Concurrency", "Thread life cycle, synchronization, locks, and ExecutorService.", "HARD"));

        // DBMS Topics
        Topic tDbmsSql = topicRepository.save(new Topic(subDbms, "SQL Queries & Joins", "DML, DDL, INNER/OUTER/CROSS joins, subqueries, and aggregate functions.", "MEDIUM"));
        Topic tDbmsNorm = topicRepository.save(new Topic(subDbms, "Database Normalization", "1NF, 2NF, 3NF, BCNF, functional dependencies, and lossless decomposition.", "HARD"));
        Topic tDbmsTxn = topicRepository.save(new Topic(subDbms, "Transactions & Concurrency", "ACID properties, serializability, two-phase locking, and isolation levels.", "HARD"));
        Topic tDbmsIndexing = topicRepository.save(new Topic(subDbms, "Indexing & B-Trees", "Clustered vs non-clustered indexes, B+ Tree search, and query optimization.", "MEDIUM"));
        Topic tDbmsEr = topicRepository.save(new Topic(subDbms, "ER Modeling", "Entities, attributes, relationships, cardinalities, and relational mapping.", "EASY"));

        // OS Topics
        Topic tOsDeadlocks = topicRepository.save(new Topic(subOs, "Deadlocks & Prevention", "Coffman conditions, Banker's algorithm, resource allocation graphs, and detection.", "HARD"));
        Topic tOsProcess = topicRepository.save(new Topic(subOs, "Process Scheduling", "FCFS, SJF, Round Robin, priority scheduling, context switching, and PCB.", "MEDIUM"));
        Topic tOsMemory = topicRepository.save(new Topic(subOs, "Virtual Memory & Paging", "Page tables, TLB, demand paging, FIFO, LRU page replacement policies.", "HARD"));
        Topic tOsSync = topicRepository.save(new Topic(subOs, "Process Synchronization", "Critical section problem, semaphores, mutexes, monitors, and race conditions.", "HARD"));
        Topic tOsFiles = topicRepository.save(new Topic(subOs, "File Systems", "File allocation tables, inodes, contiguous vs linked allocation, and directory structures.", "EASY"));

        // CN Topics
        Topic tCnRouting = topicRepository.save(new Topic(subCn, "Routing Algorithms", "Distance vector routing, link-state routing (Dijkstra), OSPF, and BGP.", "MEDIUM"));
        Topic tCnOsi = topicRepository.save(new Topic(subCn, "OSI & TCP/IP Models", "7-layer OSI model, encapsulation, packet framing, and protocol stacks.", "EASY"));
        Topic tCnIp = topicRepository.save(new Topic(subCn, "IP Addressing & Subnetting", "IPv4 classes, CIDR notation, subnet masks, and NAT translation.", "MEDIUM"));
        Topic tCnTransport = topicRepository.save(new Topic(subCn, "Transport Layer Protocols", "TCP three-way handshake, flow control, congestion window, and UDP.", "MEDIUM"));
        Topic tCnSecurity = topicRepository.save(new Topic(subCn, "Network Security & Cryptography", "Symmetric vs asymmetric encryption, RSA, SSL/TLS handshakes, and firewalls.", "HARD"));

        // 4. Create Study Materials
        createStudyMaterial(tJavaInheritance, "Comprehensive Java Inheritance Guide & Notes", "Key principles of class extension, method overriding, super() call semantics, and diamond problem resolution in Java interfaces.", "NOTES", "https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html", "Inheritance allows a new class to inherit attributes and methods of an existing class. In Java, use the 'extends' keyword. Multiple class inheritance is prohibited to avoid ambiguity (the diamond problem). Interfaces allow multiple inheritance of type.", "MEDIUM", teacher.getId());
        createStudyMaterial(tJavaInheritance, "Mastering Polymorphism & Inheritance Video Lecture", "High-yield video walkthrough explaining runtime polymorphism, dynamic method dispatch, and abstract classes.", "VIDEO", "https://www.youtube.com/watch?v=4bm_H1gTj58", "Visual explanation of stack and heap allocation during polymorphic method calls.", "HARD", teacher.getId());
        createStudyMaterial(tJavaInheritance, "Inheritance & Polymorphism Cheat Sheet (PDF)", "Printable summary of OOP inheritance rules, casting rules, and best practices.", "PDF", "https://example.com/materials/java-inheritance-cheatsheet.pdf", "Summary PDF covering upcasting, downcasting, and instanceof operator.", "EASY", teacher.getId());

        createStudyMaterial(tDbmsSql, "SQL Complex Joins and Aggregations Manual", "In-depth tutorial on INNER, LEFT, RIGHT, FULL OUTER joins and GROUP BY/HAVING clauses with sample schemas.", "NOTES", "https://www.w3schools.com/sql/sql_join.asp", "JOIN clause is used to combine rows from two or more tables based on a related column between them. Learn how NULL handles in outer joins.", "MEDIUM", teacher.getId());
        createStudyMaterial(tDbmsSql, "Advanced SQL Query Optimization Video", "Step-by-step video on subquery restructuring, EXPLAIN plans, and indexes.", "VIDEO", "https://www.youtube.com/watch?v=BHwzDkyfdNE", "Video detailing how database query planners choose execution paths.", "HARD", teacher.getId());

        createStudyMaterial(tOsDeadlocks, "Deadlock Conditions & Banker's Algorithm Notes", "Clear breakdown of Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait, and Banker's safe state verification.", "NOTES", "https://www.geeksforgeeks.org/bankers-algorithm-in-operating-system-2/", "Deadlock occurs when every process is waiting for a resource held by another waiting process. Banker's Algorithm determines if allocating resources maintains a safe state.", "HARD", teacher.getId());

        // 5. Create Quizzes and Questions
        // Quiz 1: Java Inheritance
        Quiz qInheritance = createQuiz(subJava, tJavaInheritance, "Java Inheritance Mastery Quiz", "10-question evaluation on inheritance hierarchies, overriding, and keywords.", "MEDIUM", 15, 10, teacher.getId());
        addQuestion(qInheritance, tJavaInheritance, "Which keyword is used in Java to inherit a class?", "EASY", 1, "The 'extends' keyword specifies that a class is derived from a parent class.", "extends", true, "implements", false, "inherits", false, "instanceof", false);
        addQuestion(qInheritance, tJavaInheritance, "What happens if a subclass defines a method with the exact same name and signature as its superclass?", "MEDIUM", 1, "This is known as method overriding (runtime polymorphism).", "Method Overriding", true, "Method Overloading", false, "Compile Error", false, "Operator Hiding", false);
        addQuestion(qInheritance, tJavaInheritance, "Can a class extend more than one class directly in Java?", "EASY", 1, "Java does not support multiple class inheritance to avoid the diamond problem.", "No, Java supports single class inheritance only", true, "Yes, using comma separation", false, "Yes, up to 3 classes", false, "Only if all parent classes are abstract", false);
        addQuestion(qInheritance, tJavaInheritance, "Which keyword refers to the direct superclass constructor from inside a subclass constructor?", "MEDIUM", 1, "super() invokes the parent class constructor.", "super()", true, "this()", false, "parent()", false, "base()", false);
        addQuestion(qInheritance, tJavaInheritance, "What is the access level required for an overriding method in a subclass?", "HARD", 1, "An overriding method cannot reduce the visibility of the parent method.", "Equal or more permissive than the superclass method", true, "Equal or less permissive than the superclass method", false, "Must always be public", false, "Must match exactly", false);
        addQuestion(qInheritance, tJavaInheritance, "Which of the following classes cannot be subclassed in Java?", "MEDIUM", 1, "A 'final' class cannot be extended by any other class.", "A class declared as final", true, "An abstract class", false, "A static nested class", false, "A public class", false);
        addQuestion(qInheritance, tJavaInheritance, "What is the return type restriction when overriding a method in modern Java (Java 5+)?", "HARD", 1, "Java allows covariant return types where the subclass method can return a subtype.", "Can be the same or a subtype (covariant return type)", true, "Must be an identical type strictly", false, "Can be any Object type", false, "Must return void", false);
        addQuestion(qInheritance, tJavaInheritance, "What is the default superclass of all classes in Java?", "EASY", 1, "java.lang.Object is the root of the class hierarchy.", "java.lang.Object", true, "java.lang.Class", false, "java.lang.System", false, "java.lang.Base", false);
        addQuestion(qInheritance, tJavaInheritance, "Can constructors be inherited in Java?", "MEDIUM", 1, "Constructors are not inherited by subclasses, though they can be called via super().", "No, constructors are not inherited", true, "Yes, automatically", false, "Only default constructors are inherited", false, "Only public constructors are inherited", false);
        addQuestion(qInheritance, tJavaInheritance, "What does dynamic method dispatch achieve in Java?", "HARD", 1, "It resolves polymorphic method calls at runtime based on the actual object type.", "Runtime Polymorphism resolution", true, "Compile-time macro inlining", false, "Memory deallocation", false, "Thread synchronization", false);

        // Quiz 2: DBMS SQL & Joins
        Quiz qSql = createQuiz(subDbms, tDbmsSql, "SQL Queries & Database Joins Quiz", "Test your knowledge of SQL queries, grouping, and relational joins.", "MEDIUM", 15, 10, teacher.getId());
        addQuestion(qSql, tDbmsSql, "Which SQL join returns all rows from both tables, filling with NULL where matches don't exist?", "MEDIUM", 1, "FULL OUTER JOIN preserves unmatched rows from both sides.", "FULL OUTER JOIN", true, "INNER JOIN", false, "LEFT JOIN", false, "NATURAL JOIN", false);
        addQuestion(qSql, tDbmsSql, "Which clause is used to filter groups created by GROUP BY?", "EASY", 1, "HAVING filters aggregated groups, whereas WHERE filters individual rows.", "HAVING", true, "WHERE", false, "FILTER", false, "ORDER BY", false);
        addQuestion(qSql, tDbmsSql, "What is the default sorting order of the ORDER BY clause?", "EASY", 1, "ASC (Ascending) is the default order.", "ASC (Ascending)", true, "DESC (Descending)", false, "Random", false, "Insertion Order", false);
        addQuestion(qSql, tDbmsSql, "Which aggregate function ignores NULL values by default?", "MEDIUM", 1, "Aggregate functions like AVG(), SUM(), and COUNT(column) ignore NULL values.", "AVG() and SUM()", true, "COUNT(*)", false, "All aggregate functions include NULL", false, "None", false);
        addQuestion(qSql, tDbmsSql, "What type of join is produced when two tables are joined without any ON or WHERE condition?", "HARD", 1, "Cartesian product or CROSS JOIN.", "Cartesian Product (CROSS JOIN)", true, "SELF JOIN", false, "EQUI JOIN", false, "ANTI JOIN", false);

        // Quiz 3: OS Deadlocks
        Quiz qDeadlocks = createQuiz(subOs, tOsDeadlocks, "Deadlocks & Banker's Algorithm Quiz", "Evaluating Coffman conditions, safe state algorithms, and prevention.", "HARD", 15, 10, teacher.getId());
        addQuestion(qDeadlocks, tOsDeadlocks, "Which of the following is NOT one of the four Coffman deadlock conditions?", "MEDIUM", 1, "Preemption is the solution/prevention, whereas NO PREEMPTION is the deadlock condition.", "Preemption", true, "Mutual Exclusion", false, "Hold and Wait", false, "Circular Wait", false);
        addQuestion(qDeadlocks, tOsDeadlocks, "What is the primary objective of Banker's Algorithm in an Operating System?", "HARD", 1, "Banker's Algorithm prevents deadlocks by ensuring the system remains in a safe state.", "Deadlock Avoidance by verifying Safe State", true, "Deadlock Detection after occurrence", false, "CPU Process Scheduling", false, "Disk Cache Optimization", false);
        addQuestion(qDeadlocks, tOsDeadlocks, "A system is in a safe state if:", "HARD", 1, "A safe sequence exists where all processes can finish without deadlock.", "There exists at least one safe execution sequence", true, "No resources are currently allocated", false, "All processes are blocked", false, "Deadlock has already occurred", false);

        // 6. Seed Historical Performance Data matching Section 28:
        // Java OOP: 82% (STRONG)
        // Java Inheritance: 45% (WEAK -> HIGH RISK)
        // Java Collections: 78% (STRONG)
        // DBMS SQL: 52% (WEAK -> HIGH RISK)
        // DBMS Normalization: 76% (STRONG)
        // Operating Systems: 58% (WEAK -> MEDIUM RISK)
        // Computer Networks: 85% (STRONG)
        seedPerformance(savedStudent, tJavaOop, 3, 30, 25, 5, 8.2, 82.0, "IMPROVING");
        seedPerformance(savedStudent, tJavaInheritance, 2, 20, 9, 11, 4.5, 45.0, "DECLINING");
        seedPerformance(savedStudent, tJavaCollections, 3, 30, 23, 7, 7.8, 78.0, "STABLE");
        seedPerformance(savedStudent, tDbmsSql, 2, 20, 10, 10, 5.2, 52.0, "DECLINING");
        seedPerformance(savedStudent, tDbmsNorm, 3, 30, 23, 7, 7.6, 76.0, "IMPROVING");
        seedPerformance(savedStudent, tOsDeadlocks, 2, 20, 12, 8, 5.8, 58.0, "DECLINING");
        seedPerformance(savedStudent, tCnRouting, 3, 30, 26, 4, 8.5, 85.0, "IMPROVING");

        // Seed a sample quiz attempt for history
        QuizAttempt sampleAttempt = new QuizAttempt(savedStudent, qInheritance, LocalDateTime.now().minusDays(2));
        sampleAttempt.setSubmittedAt(LocalDateTime.now().minusDays(2));
        sampleAttempt.setTotalQuestions(10);
        sampleAttempt.setCorrectAnswers(4);
        sampleAttempt.setWrongAnswers(6);
        sampleAttempt.setScore(4.0);
        sampleAttempt.setPercentage(40.0);
        sampleAttempt.setTimeTakenSeconds(420);
        quizAttemptRepository.save(sampleAttempt);

        // Run prediction and recommendation calculations for seeded data
        predictionService.recalculateForStudentAndTopic(savedStudent, tJavaInheritance);
        predictionService.recalculateForStudentAndTopic(savedStudent, tDbmsSql);
        predictionService.recalculateForStudentAndTopic(savedStudent, tOsDeadlocks);
        predictionService.recalculateForStudentAndTopic(savedStudent, tJavaOop);

        recommendationService.generateRecommendations(savedStudent, tJavaInheritance, "HIGH", "Performance in Inheritance is 45% (High Risk)");
        recommendationService.generateRecommendations(savedStudent, tDbmsSql, "HIGH", "Performance in SQL Queries is 52% (High Risk)");
        recommendationService.generateRecommendations(savedStudent, tOsDeadlocks, "MEDIUM", "Performance in Deadlocks is 58% (Medium Risk)");

        System.out.println("Sample data initialization completed successfully!");
    }

    private StudyMaterial createStudyMaterial(Topic topic, String title, String desc, String type, String url, String content, String diff, Long teacherId) {
        StudyMaterial m = new StudyMaterial(topic, title, desc, type, url, content, diff, teacherId);
        return studyMaterialRepository.save(m);
    }

    private Quiz createQuiz(Subject subject, Topic topic, String title, String desc, String diff, int duration, int marks, Long teacherId) {
        Quiz q = new Quiz(subject, topic, title, desc, diff, duration, marks, teacherId);
        return quizRepository.save(q);
    }

    private void addQuestion(Quiz quiz, Topic topic, String text, String diff, int marks, String explanation,
                             String opt1, boolean c1, String opt2, boolean c2, String opt3, boolean c3, String opt4, boolean c4) {
        Question q = new Question(quiz, topic, text, diff, marks, explanation);
        q.addOption(new QuestionOption(q, opt1, c1));
        q.addOption(new QuestionOption(q, opt2, c2));
        q.addOption(new QuestionOption(q, opt3, c3));
        q.addOption(new QuestionOption(q, opt4, c4));
        questionRepository.save(q);
    }

    private void seedPerformance(User student, Topic topic, int attempts, int totalQ, int correct, int wrong, double avgScore, double avgPct, String trend) {
        TopicPerformance tp = new TopicPerformance(student, topic);
        tp.setTotalAttempts(attempts);
        tp.setTotalQuestions(totalQ);
        tp.setCorrectAnswers(correct);
        tp.setWrongAnswers(wrong);
        tp.setAverageScore(avgScore);
        tp.setAveragePercentage(avgPct);
        tp.setTrend(trend);
        tp.setLastAttemptAt(LocalDateTime.now().minusDays(3));
        topicPerformanceRepository.save(tp);
    }
}
