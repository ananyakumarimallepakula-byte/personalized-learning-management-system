// ===================================================================
// Teacher Portal JavaScript Engine
// ===================================================================

const TeacherApp = {
    currentTeacher() {
        return Auth.getUser();
    },

    async loadDashboard() {
        const teacher = this.currentTeacher();
        if (!teacher) return;

        try {
            const stats = await API.get(`/teachers/${teacher.id}/dashboard`);
            
            const teacherNameElem = document.getElementById('teacher-name-display');
            if (teacherNameElem) teacherNameElem.textContent = teacher.name;

            const setVal = (id, val) => {
                const el = document.getElementById(id);
                if (el) el.textContent = val != null ? val : '0';
            };

            setVal('total-students-val', stats.totalStudents);
            setVal('total-quizzes-val', stats.totalQuizzes);
            setVal('total-attempts-val', stats.totalAttempts);
            setVal('total-topics-val', stats.totalTopics);
            setVal('total-questions-val', stats.totalQuestions);
            setVal('total-materials-val', stats.totalMaterials);

            // Render Charts
            this.renderWeakTopicChart(stats.weakTopicsFrequency);
            this.renderSubjectPerformanceChart(stats.subjectAverages);
        } catch (err) {
            console.error('Failed to load teacher dashboard:', err);
        }
    },

    renderWeakTopicChart(freqMap) {
        const ctx = document.getElementById('weakTopicFrequencyChart');
        if (!ctx || !freqMap) return;

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: Object.keys(freqMap),
                datasets: [{
                    label: 'Students Struggling (Weak)',
                    data: Object.values(freqMap),
                    backgroundColor: '#ef4444',
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: { beginAtZero: true, ticks: { precision: 0 } }
                }
            }
        });
    },

    renderSubjectPerformanceChart(avgMap) {
        const ctx = document.getElementById('subjectAvgChart');
        if (!ctx || !avgMap) return;

        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: Object.keys(avgMap),
                datasets: [{
                    data: Object.values(avgMap),
                    backgroundColor: ['#4f46e5', '#0ea5e9', '#10b981', '#f59e0b', '#8b5cf6', '#ec4899']
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: 'bottom' }
                }
            }
        });
    },

    async loadSubjects() {
        return await API.get('/subjects');
    },

    async createSubject(name, description) {
        const teacher = this.currentTeacher();
        const data = await API.post(`/subjects?createdBy=${teacher.id}`, { name, description, active: true });
        return data;
    },

    async deleteSubject(id) {
        if (!confirm('Are you sure you want to delete this subject? All associated topics, questions, and quizzes may be affected.')) return false;
        await API.delete(`/subjects/${id}`);
        return true;
    },

    async loadTopics(subjectId = null) {
        if (subjectId) {
            return await API.get(`/topics?subjectId=${subjectId}`);
        }
        return await API.get('/topics');
    },

    async createTopic(subjectId, name, description, difficulty) {
        const data = await API.post('/topics', { subjectId, name, description, difficulty, active: true });
        return data;
    },

    async deleteTopic(id) {
        if (!confirm('Are you sure you want to delete this topic?')) return false;
        await API.delete(`/topics/${id}`);
        return true;
    },

    async loadQuizzes() {
        return await API.get('/quizzes');
    },

    async createQuiz(payload) {
        const teacher = this.currentTeacher();
        const data = await API.post(`/quizzes?createdBy=${teacher.id}`, payload);
        return data;
    },

    async deleteQuiz(id) {
        if (!confirm('Are you sure you want to delete this quiz?')) return false;
        await API.delete(`/quizzes/${id}`);
        return true;
    },

    async loadQuestions(topicId = null) {
        if (topicId) {
            return await API.get(`/questions/topic/${topicId}`);
        }
        return await API.get('/questions');
    },

    async createQuestion(payload) {
        return await API.post('/questions', payload);
    },

    async deleteQuestion(id) {
        if (!confirm('Are you sure you want to delete this question?')) return false;
        await API.delete(`/questions/${id}`);
        return true;
    },

    async loadMaterials(topicId = null) {
        if (topicId) {
            return await API.get(`/materials?topicId=${topicId}`);
        }
        return await API.get('/materials');
    },

    async createMaterial(payload) {
        const teacher = this.currentTeacher();
        return await API.post(`/materials?createdBy=${teacher.id}`, payload);
    },

    async deleteMaterial(id) {
        if (!confirm('Are you sure you want to delete this study material?')) return false;
        await API.delete(`/materials/${id}`);
        return true;
    },

    async loadStudents() {
        return await API.get('/students');
    }
};
