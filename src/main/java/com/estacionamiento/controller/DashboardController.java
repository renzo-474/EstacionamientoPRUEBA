
package com.estacionamiento.controller;

import com.estacionamiento.dto.DashboardDTO;
import com.estacionamiento.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")  
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class DashboardController {
    
    private final DashboardService dashboardService;
    
    @GetMapping("/stats")
    public ResponseEntity<DashboardDTO> getDashboardStats() {
        System.out.println("=== DASHBOARD CONTROLLER: Obteniendo estadísticas ===");
        DashboardDTO stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
}