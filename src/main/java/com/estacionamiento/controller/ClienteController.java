/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.controller;

import com.estacionamiento.dto.ClienteDTO;
import com.estacionamiento.service.ClienteService;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        return ResponseEntity.ok(clienteService.getAllClientes());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable String id) {
        return ResponseEntity.ok(clienteService.getClienteById(id));
    }
    
    @PostMapping
    public ResponseEntity<ClienteDTO> createCliente(@RequestBody ClienteDTO clienteDTO) {
        return new ResponseEntity<>(clienteService.createCliente(clienteDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateCliente(
            @PathVariable String id,
            @RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.ok(clienteService.updateCliente(id, clienteDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable String id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }
}