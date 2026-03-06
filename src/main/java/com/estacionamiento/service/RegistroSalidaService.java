/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.service;
import com.estacionamiento.dto.RegistroSalidaDTO;
import com.estacionamiento.entity.RegistroEntrada;
import com.estacionamiento.entity.RegistroSalida;
import com.estacionamiento.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author user
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RegistroSalidaService {
    
    private final RegistroSalidaRepository registroSalidaRepository;
    private final RegistroEntradaRepository registroEntradaRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;

    private static final double TARIFA_AUTO = 5.0;
    private static final double TARIFA_MOTO = 3.0;
    private static final double TARIFA_CAMIONETA = 7.0;
    
    public List<RegistroSalidaDTO> getAllSalidas() {
        return registroSalidaRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public RegistroSalidaDTO getSalidaById(String id) {
        RegistroSalida salida = registroSalidaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro de salida no encontrado"));
        return convertToDTO(salida);
    }
    
    public RegistroSalidaDTO createSalida(RegistroSalidaDTO salidaDTO) {
        RegistroEntrada entrada = registroEntradaRepository.findById(salidaDTO.getIdEntrada())
            .orElseThrow(() -> new RuntimeException("Registro de entrada no encontrado"));
        
        if (registroSalidaRepository.existsByRegistroEntrada(entrada)) {
            throw new RuntimeException("Esta entrada ya tiene una salida registrada");
        }
        
        String idSalida = generarIdSalida();
        
        RegistroSalida salida = new RegistroSalida();
        salida.setIdSalida(idSalida);
        salida.setRegistroEntrada(entrada);
        salida.setFechaSalida(LocalDateTime.now());
        salida.setHoraSalida(LocalTime.now());
        
        updateSalidaFromDTO(salida, salidaDTO);
        
        RegistroSalida savedSalida = registroSalidaRepository.save(salida);
        return convertToDTO(savedSalida);
    }
    
    public RegistroSalidaDTO updateSalida(String id, RegistroSalidaDTO salidaDTO) {
        RegistroSalida salida = registroSalidaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro de salida no encontrado"));
        
        updateSalidaFromDTO(salida, salidaDTO);
        
        RegistroSalida updatedSalida = registroSalidaRepository.save(salida);
        return convertToDTO(updatedSalida);
    }
    
    public void deleteSalida(String id) {
        if (!registroSalidaRepository.existsById(id)) {
            throw new RuntimeException("Registro de salida no encontrado");
        }
        
        registroSalidaRepository.deleteById(id);
    }
    
    private RegistroSalidaDTO convertToDTO(RegistroSalida salida) {
        RegistroSalidaDTO dto = new RegistroSalidaDTO();
        dto.setIdSalida(salida.getIdSalida());
        dto.setFechaSalida(salida.getFechaSalida());
        dto.setHoraSalida(salida.getHoraSalida());
        dto.setCodTipoVhc(salida.getCodTipoVhc());
        
        if (salida.getRegistroEntrada() != null) {
            dto.setIdEntrada(salida.getRegistroEntrada().getIdEntrada());
  
            LocalDateTime fechaEntrada = salida.getRegistroEntrada().getFechaIngreso();
            LocalDateTime fechaSalida = salida.getFechaSalida();
            double montoTotal = calcularMonto(fechaEntrada, fechaSalida, salida.getCodTipoVhc());
            dto.setMontoTotal(montoTotal);
        }
        
        if (salida.getCliente() != null) {
            dto.setIdCliente(salida.getCliente().getIdCliente());
            dto.setNombreCliente(salida.getCliente().getNombres() + " " + 
                               salida.getCliente().getApellidos());
        }
        
        if (salida.getEmpleado() != null) {
            dto.setIdEmpleado(salida.getEmpleado().getIdEmpleado());
            dto.setNombreEmpleado(salida.getEmpleado().getNombres() + " " + 
                                 salida.getEmpleado().getApellidos());
        }
        
        return dto;
    }
    
    private void updateSalidaFromDTO(RegistroSalida salida, RegistroSalidaDTO dto) {
        salida.setCodTipoVhc(dto.getCodTipoVhc());
        
        if (dto.getIdCliente() != null) {
            salida.setCliente(clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado")));
        }
        
        if (dto.getIdEmpleado() != null) {
            salida.setEmpleado(empleadoRepository.findById(dto.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado")));
        }
    }
    
    private double calcularMonto(LocalDateTime entrada, LocalDateTime salida, Integer tipoVehiculo) {
        Duration duracion = Duration.between(entrada, salida);
        long horas = duracion.toHours();
        if (duracion.toMinutes() % 60 > 0) {
            horas++; 
        }
        
        double tarifa = TARIFA_AUTO; 
        if (tipoVehiculo != null) {
            switch (tipoVehiculo) {
                case 1: tarifa = TARIFA_AUTO; break;
                case 2: tarifa = TARIFA_MOTO; break;
                case 3: tarifa = TARIFA_CAMIONETA; break;
            }
        }
        
        return horas * tarifa;
    }
    
    private String generarIdSalida() {
        Long count = registroSalidaRepository.count();
        return String.format("SAL%07d", count + 1);
    }
}