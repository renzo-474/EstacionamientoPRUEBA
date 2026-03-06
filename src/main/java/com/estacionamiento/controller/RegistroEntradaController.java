/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.controller;
import com.estacionamiento.dto.RegistroEntradaDTO;
import com.estacionamiento.service.RegistroEntradaService;
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
@RequestMapping("/api/entradas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class RegistroEntradaController {
    
    private final RegistroEntradaService registroEntradaService;
    
    @GetMapping
    public ResponseEntity<List<RegistroEntradaDTO>> getAllEntradas() {
        return ResponseEntity.ok(registroEntradaService.getAllEntradas());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RegistroEntradaDTO> getEntradaById(@PathVariable String id) {
        return ResponseEntity.ok(registroEntradaService.getEntradaById(id));
    }
    
    @PostMapping
    public ResponseEntity<RegistroEntradaDTO> createEntrada(@RequestBody RegistroEntradaDTO entradaDTO) {
        return new ResponseEntity<>(registroEntradaService.createEntrada(entradaDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RegistroEntradaDTO> updateEntrada(
            @PathVariable String id,
            @RequestBody RegistroEntradaDTO entradaDTO) {
        return ResponseEntity.ok(registroEntradaService.updateEntrada(id, entradaDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrada(@PathVariable String id) {
        registroEntradaService.deleteEntrada(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/activas")
    public ResponseEntity<List<RegistroEntradaDTO>> getEntradasActivas() {
        return ResponseEntity.ok(registroEntradaService.getEntradasSinSalida());
    }
}