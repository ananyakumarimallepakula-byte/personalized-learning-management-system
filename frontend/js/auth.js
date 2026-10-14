// ===================================================================
// Authentication & Session Management
// ===================================================================

const Auth = {
    getUser() {
        const u = localStorage.getItem('user_session');
        return u ? JSON.parse(u) : null;
    },

    setUser(userData, token) {
        localStorage.setItem('user_session', JSON.stringify(userData));
        if (token) {
            localStorage.setItem('auth_token', token);
        }
    },

    isLoggedIn() {
        return this.getUser() !== null;
    },

    async login(email, password) {
        const res = await API.post('/auth/login', { email, password });
        this.setUser({
            id: res.id,
            name: res.name,
            email: res.email,
            role: res.role
        }, res.token);
        return res;
    },

    async register(data) {
        return await API.post('/auth/register', data);
    },

    logout() {
        localStorage.removeItem('user_session');
        localStorage.removeItem('auth_token');
        window.location.href = '/login.html';
    },

    requireRole(expectedRole) {
        const user = this.getUser();
        if (!user) {
            // Determine relative path to login.html
            const isSubfolder = window.location.pathname.includes('/student/') || 
                                window.location.pathname.includes('/teacher/') || 
                                window.location.pathname.includes('/admin/');
            window.location.href = isSubfolder ? '../login.html' : 'login.html';
            return false;
        }

        if (expectedRole && user.role.toUpperCase() !== expectedRole.toUpperCase() && user.role.toUpperCase() !== 'ADMIN') {
            alert(`Unauthorized! This section requires ${expectedRole} privileges.`);
            window.location.href = user.role === 'TEACHER' ? '/teacher/dashboard.html' : '/student/dashboard.html';
            return false;
        }
        return true;
    },

    initNavbar() {
        const user = this.getUser();
        const navContainer = document.getElementById('navbar-auth-controls');
        if (!navContainer) return;

        if (user) {
            let dashboardUrl = '/student/dashboard.html';
            if (user.role === 'TEACHER') dashboardUrl = '/teacher/dashboard.html';
            if (user.role === 'ADMIN') dashboardUrl = '/admin/dashboard.html';

            navContainer.innerHTML = `
                <div class="d-flex align-items-center gap-3">
                    <a href="${dashboardUrl}" class="btn btn-outline-primary btn-sm">
                        <i class="bi bi-speedometer2 me-1"></i>Dashboard
                    </a>
                    <span class="badge bg-secondary text-uppercase">${user.role}</span>
                    <span class="fw-semibold text-dark">${user.name}</span>
                    <button class="btn btn-outline-danger btn-sm" onclick="Auth.logout()">
                        <i class="bi bi-box-arrow-right me-1"></i>Logout
                    </button>
                </div>
            `;
        } else {
            navContainer.innerHTML = `
                <div class="d-flex gap-2">
                    <a href="/login.html" class="btn btn-outline-primary btn-sm">Sign In</a>
                    <a href="/register.html" class="btn btn-primary btn-sm">Sign Up Free</a>
                </div>
            `;
        }
    }
};

document.addEventListener('DOMContentLoaded', () => {
    Auth.initNavbar();
});
