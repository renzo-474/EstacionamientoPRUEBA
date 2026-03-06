/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.controller;
import com.estacionamiento.dto.EmpleadoDTO;
import com.estacionamiento.service.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 *
 * @author user
 */

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class EmpleadoController {
    
    private final EmpleadoService empleadoService;
    
    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> getAllEmpleados() {
        return ResponseEntity.ok(empleadoService.getAllEmpleados());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> getEmpleadoById(@PathVariable String id) {
        return ResponseEntity.ok(empleadoService.getEmpleadoById(id));
    }
    
    @PostMapping
    public ResponseEntity<EmpleadoDTO> createEmpleado(@RequestBody EmpleadoDTO empleadoDTO) {
        return new ResponseEntity<>(empleadoService.createEmpleado(empleadoDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> updateEmpleado(
            @PathVariable String id,
            @RequestBody EmpleadoDTO empleadoDTO) {
        return ResponseEntity.ok(empleadoService.updateEmpleado(id, empleadoDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable String id) {
        empleadoService.deleteEmpleado(id);
        return ResponseEntity.noContent().build();
    }
}