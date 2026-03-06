/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.service;

import com.estacionamiento.dto.ClienteDTO;
import com.estacionamiento.entity.Cliente;
import com.estacionamiento.entity.Distrito;
import com.estacionamiento.entity.Nacionalidad;
import com.estacionamiento.entity.Sexo;
import com.estacionamiento.repository.ClienteRepository;
import com.estacionamiento.repository.DistritoRepository;
import com.estacionamiento.repository.NacionalidadRepository;
import com.estacionamiento.repository.SexoRepository;
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
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final DistritoRepository distritoRepository;
    private final NacionalidadRepository nacionalidadRepository;
    private final SexoRepository sexoRepository;
    
    public List<ClienteDTO> getAllClientes() {
        return clienteRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public ClienteDTO getClienteById(String id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return convertToDTO(cliente);
    }
    
    public ClienteDTO createCliente(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(generarIdCliente());
        updateClienteFromDTO(cliente, clienteDTO);
        
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }
    
    public ClienteDTO updateCliente(String id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        updateClienteFromDTO(cliente, clienteDTO);
        Cliente updatedCliente = clienteRepository.save(cliente);
        return convertToDTO(updatedCliente);
    }
    
    public void deleteCliente(String id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }
    
    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setNombres(cliente.getNombres());
        dto.setApellidos(cliente.getApellidos());
        dto.setNumDoc(cliente.getNumDoc());
        dto.setNroLicencia(cliente.getNroLicencia());
        dto.setTelefono(cliente.getTelefono());
        dto.setCorreo(cliente.getCorreo());
        dto.setDireccion(cliente.getDireccion());
        
        if (cliente.getNacionalidad() != null) {
            dto.setCodNacio(cliente.getNacionalidad().getCodNacio().intValue());
            dto.setNacionalidad(cliente.getNacionalidad().getNombre());
        }
        
        if (cliente.getSexo() != null) {
            dto.setCodSexo(cliente.getSexo().getCodSexo().intValue());
            dto.setSexo(cliente.getSexo().getNombre());
        }
        
        if (cliente.getDistrito() != null) {
            dto.setCodDistrito(cliente.getDistrito().getCodDistrito().intValue());
            dto.setDistrito(cliente.getDistrito().getNombre());
        }
        
        return dto;
    }
    
    private void updateClienteFromDTO(Cliente cliente, ClienteDTO dto) {
        cliente.setNombres(dto.getNombres());
        cliente.setApellidos(dto.getApellidos());
        cliente.setNumDoc(dto.getNumDoc());
        cliente.setNroLicencia(dto.getNroLicencia());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCorreo(dto.getCorreo());
        cliente.setDireccion(dto.getDireccion());
        
        if (dto.getCodNacio() != null) {
            Nacionalidad nacionalidad = nacionalidadRepository.findById(dto.getCodNacio().longValue())
                .orElseThrow(() -> new RuntimeException("Nacionalidad no encontrada"));
            cliente.setNacionalidad(nacionalidad);
        }
        
        if (dto.getCodSexo() != null) {
            Sexo sexo = sexoRepository.findById(dto.getCodSexo().longValue())
                .orElseThrow(() -> new RuntimeException("Sexo no encontrado"));
            cliente.setSexo(sexo);
        }
        
        if (dto.getCodDistrito() != null) {
            Distrito distrito = distritoRepository.findById(dto.getCodDistrito().longValue())
                .orElseThrow(() -> new RuntimeException("Distrito no encontrado"));
            cliente.setDistrito(distrito);
        }
    }
    
    private String generarIdCliente() {
        Long count = clienteRepository.count();
        return String.format("CLI%07d", count + 1);
    }
}