/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.service;

import com.estacionamiento.dto.RegistroEntradaDTO;
import com.estacionamiento.entity.RegistroEntrada;
import com.estacionamiento.entity.RegistroSalida;
import com.estacionamiento.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author user
 */

@Service
@RequiredArgsConstructor
@Transactional
public class RegistroEntradaService {
    
    private final RegistroEntradaRepository registroEntradaRepository;
    private final RegistroSalidaRepository registroSalidaRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final TipoDocumentoPagoRepository tipoDocumentoPagoRepository;
    
    public List<RegistroEntradaDTO> getAllEntradas() {
        return registroEntradaRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public RegistroEntradaDTO getEntradaById(String id) {
        RegistroEntrada entrada = registroEntradaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro de entrada no encontrado"));
        return convertToDTO(entrada);
    }
    
    public RegistroEntradaDTO createEntrada(RegistroEntradaDTO entradaDTO) {
 
        String idEntrada = generarIdEntrada();
        
        RegistroEntrada entrada = new RegistroEntrada();
        entrada.setIdEntrada(idEntrada);
        entrada.setFechaIngreso(LocalDateTime.now());
        entrada.setHoraIngreso(LocalTime.now());
        
        updateEntradaFromDTO(entrada, entradaDTO);
        
        RegistroEntrada savedEntrada = registroEntradaRepository.save(entrada);
        return convertToDTO(savedEntrada);
    }
    
    public RegistroEntradaDTO updateEntrada(String id, RegistroEntradaDTO entradaDTO) {
        RegistroEntrada entrada = registroEntradaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro de entrada no encontrado"));
        
        updateEntradaFromDTO(entrada, entradaDTO);
        
        RegistroEntrada updatedEntrada = registroEntradaRepository.save(entrada);
        return convertToDTO(updatedEntrada);
    }
    
    public void deleteEntrada(String id) {
        RegistroEntrada entrada = registroEntradaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registro de entrada no encontrado"));

        boolean tieneSalida = registroSalidaRepository.existsByRegistroEntrada(entrada);
        if (tieneSalida) {
            throw new RuntimeException("No se puede eliminar una entrada que tiene salida registrada");
        }
        
        registroEntradaRepository.deleteById(id);
    }
    
    public List<RegistroEntradaDTO> getEntradasSinSalida() {
        return registroEntradaRepository.findEntradasSinSalida().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    private RegistroEntradaDTO convertToDTO(RegistroEntrada entrada) {
        RegistroEntradaDTO dto = new RegistroEntradaDTO();
        dto.setIdEntrada(entrada.getIdEntrada());
        dto.setNivel(entrada.getNivel());
        dto.setZona(entrada.getZona());
        dto.setFechaIngreso(entrada.getFechaIngreso());
        dto.setHoraIngreso(entrada.getHoraIngreso());
        dto.setNroDocumento(entrada.getNroDocumento());
        
        if (entrada.getCliente() != null) {
            dto.setIdCliente(entrada.getCliente().getIdCliente());
            dto.setNombreCliente(entrada.getCliente().getNombres() + " " + 
                               entrada.getCliente().getApellidos());
        }
        
        if (entrada.getEmpleado() != null) {
            dto.setIdEmpleado(entrada.getEmpleado().getIdEmpleado());
            dto.setNombreEmpleado(entrada.getEmpleado().getNombres() + " " + 
                                 entrada.getEmpleado().getApellidos());
        }
        
        if (entrada.getVehiculo() != null) {
            dto.setNroPlaca(entrada.getVehiculo().getNroPlaca());
        }
        
        if (entrada.getTipoDocumentoPago() != null) {
            dto.setCodDocPaga(entrada.getTipoDocumentoPago().getCodDocPaga());
            dto.setTipoDocPago(entrada.getTipoDocumentoPago().getNombre());
        }
        boolean tieneSalida = registroSalidaRepository.existsByRegistroEntrada(entrada);
        dto.setTieneSalida(tieneSalida);
        
        return dto;
    }
    
    private void updateEntradaFromDTO(RegistroEntrada entrada, RegistroEntradaDTO dto) {
        entrada.setNivel(dto.getNivel());
        entrada.setZona(dto.getZona());
        entrada.setNroDocumento(dto.getNroDocumento());
        
        if (dto.getIdCliente() != null) {
            entrada.setCliente(clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado")));
        }
        
        if (dto.getIdEmpleado() != null) {
            entrada.setEmpleado(empleadoRepository.findById(dto.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado")));
        }
        
        if (dto.getNroPlaca() != null) {
            entrada.setVehiculo(vehiculoRepository.findById(dto.getNroPlaca())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado")));
        }
        
        if (dto.getCodDocPaga() != null) {
            entrada.setTipoDocumentoPago(tipoDocumentoPagoRepository.findById(dto.getCodDocPaga())
                .orElseThrow(() -> new RuntimeException("Tipo de documento de pago no encontrado")));
        }
    }
    
    private String generarIdEntrada() {
        Long count = registroEntradaRepository.count();
        return String.format("ENT%07d", count + 1);
    }
}
