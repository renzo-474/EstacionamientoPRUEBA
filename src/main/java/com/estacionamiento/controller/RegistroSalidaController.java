/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.controller;
import com.estacionamiento.dto.RegistroSalidaDTO;
import com.estacionamiento.service.RegistroSalidaService;
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
@RequestMapping("/api/salidas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class RegistroSalidaController {
    
    private final RegistroSalidaService registroSalidaService;
    
    @GetMapping
    public ResponseEntity<List<RegistroSalidaDTO>> getAllSalidas() {
        return ResponseEntity.ok(registroSalidaService.getAllSalidas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RegistroSalidaDTO> getSalidaById(@PathVariable String id) {
        return ResponseEntity.ok(registroSalidaService.getSalidaById(id));
    }
    
    @PostMapping
    public ResponseEntity<RegistroSalidaDTO> createSalida(@RequestBody RegistroSalidaDTO salidaDTO) {
        return new ResponseEntity<>(registroSalidaService.createSalida(salidaDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RegistroSalidaDTO> updateSalida(
            @PathVariable String id,
            @RequestBody RegistroSalidaDTO salidaDTO) {
        return ResponseEntity.ok(registroSalidaService.updateSalida(id, salidaDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalida(@PathVariable String id) {
        registroSalidaService.deleteSalida(id);
        return ResponseEntity.noContent().build();
    }
}
