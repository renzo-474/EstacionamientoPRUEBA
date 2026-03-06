/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.service;

import com.estacionamiento.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/**
 *
 * @author Ryzen
 */

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final EmpleadoRepository empleadoRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println("=== UserDetailsService: Buscando usuario: " + username + " ===");
    var empleado = empleadoRepository.findByIdEmpleado(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    
    System.out.println("Usuario encontrado: " + empleado.getIdEmpleado());
    System.out.println("Password desde UserDetails: " + empleado.getPassword());
    
    return empleado;
    }
}