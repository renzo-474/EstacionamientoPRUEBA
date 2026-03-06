/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.service;

import com.estacionamiento.dto.EmpleadoDTO;
import com.estacionamiento.entity.Empleado;
import com.estacionamiento.entity.Distrito;
import com.estacionamiento.entity.Nacionalidad;
import com.estacionamiento.entity.Sexo;
import com.estacionamiento.repository.EmpleadoRepository;
import com.estacionamiento.repository.DistritoRepository;
import com.estacionamiento.repository.NacionalidadRepository;
import com.estacionamiento.repository.SexoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author user
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmpleadoService {
    
    private final EmpleadoRepository empleadoRepository;
    private final DistritoRepository distritoRepository;
    private final NacionalidadRepository nacionalidadRepository;
    private final SexoRepository sexoRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<EmpleadoDTO> getAllEmpleados() {
        return empleadoRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public EmpleadoDTO getEmpleadoById(String id) {
        Empleado empleado = empleadoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        return convertToDTO(empleado);
    }
    
    public EmpleadoDTO createEmpleado(EmpleadoDTO empleadoDTO) {
        if (empleadoRepository.existsById(empleadoDTO.getIdEmpleado())) {
            throw new RuntimeException("El ID de empleado ya existe");
        }
        
        Empleado empleado = new Empleado();
        empleado.setIdEmpleado(empleadoDTO.getIdEmpleado());
        updateEmpleadoFromDTO(empleado, empleadoDTO);
        
        // Encriptar contraseña
        if (empleadoDTO.getPassword() != null && !empleadoDTO.getPassword().isEmpty()) {
            empleado.setPassword(passwordEncoder.encode(empleadoDTO.getPassword()));
        }
        
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        return convertToDTO(savedEmpleado);
    }
    
    public EmpleadoDTO updateEmpleado(String id, EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        
        updateEmpleadoFromDTO(empleado, empleadoDTO);
        
        // Solo actualizar contraseña si se proporciona una nueva
        if (empleadoDTO.getPassword() != null && !empleadoDTO.getPassword().isEmpty()) {
            empleado.setPassword(passwordEncoder.encode(empleadoDTO.getPassword()));
        }
        
        Empleado updatedEmpleado = empleadoRepository.save(empleado);
        return convertToDTO(updatedEmpleado);
    }
    
    public void deleteEmpleado(String id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado");
        }
        
        empleadoRepository.deleteById(id);
    }
    
    private EmpleadoDTO convertToDTO(Empleado empleado) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setIdEmpleado(empleado.getIdEmpleado());
        dto.setNombres(empleado.getNombres());
        dto.setApellidos(empleado.getApellidos());
        dto.setNumDoc(empleado.getNumDoc());
        dto.setTelefono(empleado.getTelefono());
        dto.setCorreo(empleado.getCorreo());
        dto.setDireccion(empleado.getDireccion());
        // No incluir password en el DTO por seguridad
        
        if (empleado.getNacionalidad() != null) {
            dto.setCodNacio(empleado.getNacionalidad().getCodNacio().intValue());
            dto.setNacionalidad(empleado.getNacionalidad().getNombre());
        }
        
        if (empleado.getSexo() != null) {
            dto.setCodSexo(empleado.getSexo().getCodSexo().intValue());
            dto.setSexo(empleado.getSexo().getNombre());
        }
        
        if (empleado.getDistrito() != null) {
            dto.setCodDistrito(empleado.getDistrito().getCodDistrito().intValue());
            dto.setDistrito(empleado.getDistrito().getNombre());
        }
        
        return dto;
    }
    
    private void updateEmpleadoFromDTO(Empleado empleado, EmpleadoDTO dto) {
        empleado.setNombres(dto.getNombres());
        empleado.setApellidos(dto.getApellidos());
        empleado.setNumDoc(dto.getNumDoc());
        empleado.setTelefono(dto.getTelefono());
        empleado.setCorreo(dto.getCorreo());
        empleado.setDireccion(dto.getDireccion());
        
        if (dto.getCodNacio() != null) {
            Nacionalidad nacionalidad = nacionalidadRepository.findById(dto.getCodNacio().longValue())
                .orElseThrow(() -> new RuntimeException("Nacionalidad no encontrada"));
            empleado.setNacionalidad(nacionalidad);
        }
        
        if (dto.getCodSexo() != null) {
            Sexo sexo = sexoRepository.findById(dto.getCodSexo().longValue())
                .orElseThrow(() -> new RuntimeException("Sexo no encontrado"));
            empleado.setSexo(sexo);
        }
        
        if (dto.getCodDistrito() != null) {
            Distrito distrito = distritoRepository.findById(dto.getCodDistrito().longValue())
                .orElseThrow(() -> new RuntimeException("Distrito no encontrado"));
            empleado.setDistrito(distrito);
        }
    }
}