// ===================================================================
// Admin Portal JavaScript Engine
// ===================================================================

const AdminApp = {
    async loadDashboard() {
        try {
            const stats = await API.get('/admin/dashboard');

            const setVal = (id, val) => {
                const el = document.getElementById(id);
                if (el) el.textContent = val != null ? val : '0';
            };

            setVal('total-users-val', stats.totalUsers);
            setVal('total-students-val', stats.totalStudents);
            setVal('total-teachers-val', stats.totalTeachers);
            setVal('total-quizzes-val', stats.totalQuizzes);
            setVal('total-attempts-val', stats.totalAttempts);
            setVal('total-subjects-val', stats.totalSubjects);
            setVal('total-topics-val', stats.totalTopics);
            setVal('total-questions-val', stats.totalQuestions);
            setVal('total-materials-val', stats.totalMaterials);

            this.renderSystemOverviewChart(stats);
            this.renderWeakTopicAnalyticsChart(stats.weakTopicsFrequency);
        } catch (err) {
            console.error('Failed to load admin dashboard:', err);
        }
    },

    renderSystemOverviewChart(stats) {
        const ctx = document.getElementById('systemOverviewChart');
        if (!ctx) return;

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Students', 'Teachers', 'Subjects', 'Topics', 'Quizzes', 'Attempts', 'Questions', 'Materials'],
                datasets: [{
                    label: 'System Count',
                    data: [stats.totalStudents, stats.totalTeachers, stats.totalSubjects, stats.totalTopics, stats.totalQuizzes, stats.totalAttempts, stats.totalQuestions, stats.totalMaterials],
                    backgroundColor: ['#4f46e5', '#0ea5e9', '#10b981', '#f59e0b', '#8b5cf6', '#ec4899', '#06b6d4', '#84cc16'],
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

    renderWeakTopicAnalyticsChart(freqMap) {
        const ctx = document.getElementById('weakTopicAnalyticsChart');
        if (!ctx || !freqMap) return;

        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: Object.keys(freqMap),
                datasets: [{
                    data: Object.values(freqMap),
                    backgroundColor: ['#ef4444', '#f97316', '#f59e0b', '#eab308', '#84cc16', '#06b6d4']
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

    async loadUsersTable() {
        const tbody = document.getElementById('users-table-body');
        if (!tbody) return;

        try {
            const users = await API.get('/admin/users');
            tbody.innerHTML = users.map(u => `
                <tr>
                    <td>#${u.id}</td>
                    <td class="fw-semibold">${u.name}</td>
                    <td>${u.email}</td>
                    <td><span class="badge ${u.role === 'ADMIN' ? 'bg-danger' : (u.role === 'TEACHER' ? 'bg-primary' : 'bg-success')}">${u.role}</span></td>
                    <td>
                        <span class="badge ${u.active ? 'bg-success' : 'bg-secondary'}">${u.active ? 'ACTIVE' : 'INACTIVE'}</span>
                    </td>
                    <td>${Utils.formatDate(u.createdAt)}</td>
                    <td class="text-end">
                        <button class="btn btn-sm ${u.active ? 'btn-outline-warning' : 'btn-outline-success'} me-1" onclick="AdminApp.toggleUser(${u.id})">
                            ${u.active ? '<i class="bi bi-pause-fill"></i> Deactivate' : '<i class="bi bi-play-fill"></i> Activate'}
                        </button>
                        <button class="btn btn-sm btn-outline-danger" onclick="AdminApp.deleteUser(${u.id})">
                            <i class="bi bi-trash"></i>
                        </button>
                    </td>
                </tr>
            `).join('');
        } catch (err) {
            console.error('Failed to load users:', err);
        }
    },

    async toggleUser(userId) {
        try {
            await API.put(`/admin/users/${userId}/toggle-status`, {});
            this.loadUsersTable();
        } catch (err) {
            alert('Failed to update status: ' + err.message);
        }
    },

    async deleteUser(userId) {
        if (!confirm('Are you sure you want to delete this user?')) return;
        try {
            await API.delete(`/admin/users/${userId}`);
            this.loadUsersTable();
        } catch (err) {
            alert('Failed to delete user: ' + err.message);
        }
    },

    async loadStudents() {
        return await API.get('/students');
    },

    async loadTeachers() {
        return await API.get('/teachers');
    },

    async loadSubjects() {
        return await API.get('/subjects');
    },

    async deleteSubject(id) {
        if (!confirm('Are you sure you want to delete this subject?')) return false;
        await API.delete(`/subjects/${id}`);
        return true;
    },

    async loadTopics() {
        return await API.get('/topics');
    },

    async deleteTopic(id) {
        if (!confirm('Are you sure you want to delete this topic?')) return false;
        await API.delete(`/topics/${id}`);
        return true;
    },

    async loadQuizzes() {
        return await API.get('/quizzes');
    },

    async deleteQuiz(id) {
        if (!confirm('Are you sure you want to delete this quiz?')) return false;
        await API.delete(`/quizzes/${id}`);
        return true;
    },

    async loadQuestions() {
        return await API.get('/questions');
    },

    async deleteQuestion(id) {
        if (!confirm('Are you sure you want to delete this question?')) return false;
        await API.delete(`/questions/${id}`);
        return true;
    },

    async loadMaterials() {
        return await API.get('/materials');
    },

    async deleteMaterial(id) {
        if (!confirm('Are you sure you want to delete this material?')) return false;
        await API.delete(`/materials/${id}`);
        return true;
    }
};
