package com.isac.ecommerce.controllers;

import com.isac.ecommerce.models.payload.response.SuccessResponse;
import com.isac.ecommerce.models.payload.response.dashboard.AdminDashboard;
import com.isac.ecommerce.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/dashboard")
public class Dashboard {


    private final DashboardService dashboardService;

    public Dashboard(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<?>> getAdminDashboard() {
        return ResponseEntity.ok(new SuccessResponse<>(dashboardService.getAdminDashboard()));
    }

}
