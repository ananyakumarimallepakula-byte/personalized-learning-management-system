package com.personalizedlearning.controller;

import com.personalizedlearning.dto.DashboardResponse;
import com.personalizedlearning.dto.SystemStatsResponse;
import com.personalizedlearning.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/students/{id}/dashboard")
    public ResponseEntity<DashboardResponse> getStudentDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardService.getStudentDashboard(id));
    }

    @GetMapping("/teachers/{id}/dashboard")
    public ResponseEntity<SystemStatsResponse> getTeacherDashboard(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardService.getSystemStats());
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<SystemStatsResponse> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getSystemStats());
    }
}
