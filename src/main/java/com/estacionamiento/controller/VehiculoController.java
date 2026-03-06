/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.controller;
import com.estacionamiento.dto.VehiculoDTO;
import com.estacionamiento.service.VehiculoService;
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
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class VehiculoController {
    
    private final VehiculoService vehiculoService;
    
    @GetMapping
    public ResponseEntity<List<VehiculoDTO>> getAllVehiculos() {
        return ResponseEntity.ok(vehiculoService.getAllVehiculos());
    }
    
    @GetMapping("/{placa}")
    public ResponseEntity<VehiculoDTO> getVehiculoByPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(vehiculoService.getVehiculoByPlaca(placa));
    }
    
    @PostMapping
    public ResponseEntity<VehiculoDTO> createVehiculo(@RequestBody VehiculoDTO vehiculoDTO) {
        return new ResponseEntity<>(vehiculoService.createVehiculo(vehiculoDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{placa}")
    public ResponseEntity<VehiculoDTO> updateVehiculo(
            @PathVariable String placa,
            @RequestBody VehiculoDTO vehiculoDTO) {
        return ResponseEntity.ok(vehiculoService.updateVehiculo(placa, vehiculoDTO));
    }
    
    @DeleteMapping("/{placa}")
    public ResponseEntity<Void> deleteVehiculo(@PathVariable String placa) {
        vehiculoService.deleteVehiculo(placa);
        return ResponseEntity.noContent().build();
    }
}
