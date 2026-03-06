/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.service;

import com.estacionamiento.dto.VehiculoDTO;
import com.estacionamiento.entity.Vehiculo;
import com.estacionamiento.repository.VehiculoRepository;
import com.estacionamiento.repository.ColorRepository;
import com.estacionamiento.repository.TipoVehiculoRepository;
import com.estacionamiento.repository.MarcaModeloRepository;
import lombok.RequiredArgsConstructor;
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
public class VehiculoService {
    
    private final VehiculoRepository vehiculoRepository;
    private final ColorRepository colorRepository;
    private final TipoVehiculoRepository tipoVehiculoRepository;
    private final MarcaModeloRepository marcaModeloRepository;
    
    public List<VehiculoDTO> getAllVehiculos() {
        return vehiculoRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public VehiculoDTO getVehiculoByPlaca(String placa) {
        Vehiculo vehiculo = vehiculoRepository.findById(placa)
            .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        return convertToDTO(vehiculo);
    }
    
    public VehiculoDTO createVehiculo(VehiculoDTO vehiculoDTO) {
       
        if (vehiculoRepository.existsById(vehiculoDTO.getNroPlaca())) {
            throw new RuntimeException("La placa ya está registrada");
        }
        
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setNroPlaca(vehiculoDTO.getNroPlaca().toUpperCase());
        updateVehiculoFromDTO(vehiculo, vehiculoDTO);
        
        Vehiculo savedVehiculo = vehiculoRepository.save(vehiculo);
        return convertToDTO(savedVehiculo);
    }
    
    public VehiculoDTO updateVehiculo(String placa, VehiculoDTO vehiculoDTO) {
        Vehiculo vehiculo = vehiculoRepository.findById(placa)
            .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        
        updateVehiculoFromDTO(vehiculo, vehiculoDTO);
        
        Vehiculo updatedVehiculo = vehiculoRepository.save(vehiculo);
        return convertToDTO(updatedVehiculo);
    }
    
    public void deleteVehiculo(String placa) {
        if (!vehiculoRepository.existsById(placa)) {
            throw new RuntimeException("Vehículo no encontrado");
        }
        
        vehiculoRepository.deleteById(placa);
    }
    
    private VehiculoDTO convertToDTO(Vehiculo vehiculo) {
        VehiculoDTO dto = new VehiculoDTO();
        dto.setNroPlaca(vehiculo.getNroPlaca());
        dto.setLunasPolarizadas(vehiculo.getLunasPolarizadas());
        
        if (vehiculo.getColor() != null) {
            dto.setCodColor(vehiculo.getColor().getCodColor());
            dto.setColor(vehiculo.getColor().getNombre());
        }
        
        if (vehiculo.getTipoVehiculo() != null) {
            dto.setCodTipoVhc(vehiculo.getTipoVehiculo().getCodTipoVhc());
            dto.setTipoVehiculo(vehiculo.getTipoVehiculo().getNombre());
        }
        
        if (vehiculo.getMarcaModelo() != null) {
            dto.setCodMarcaModelo(vehiculo.getMarcaModelo().getCodMarcaModelo());
            dto.setMarcaModelo(vehiculo.getMarcaModelo().getNombre());
        }
        
        return dto;
    }
    
    private void updateVehiculoFromDTO(Vehiculo vehiculo, VehiculoDTO dto) {
        vehiculo.setLunasPolarizadas(dto.getLunasPolarizadas());
        
        if (dto.getCodColor() != null) {
            vehiculo.setColor(colorRepository.findById(dto.getCodColor())
                .orElseThrow(() -> new RuntimeException("Color no encontrado")));
        }
        
        if (dto.getCodTipoVhc() != null) {
            vehiculo.setTipoVehiculo(tipoVehiculoRepository.findById(dto.getCodTipoVhc())
                .orElseThrow(() -> new RuntimeException("Tipo de vehículo no encontrado")));
        }
        
        if (dto.getCodMarcaModelo() != null) {
            vehiculo.setMarcaModelo(marcaModeloRepository.findById(dto.getCodMarcaModelo())
                .orElseThrow(() -> new RuntimeException("Marca/Modelo no encontrado")));
        }
    }
}