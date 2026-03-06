/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.service;

import com.estacionamiento.dto.LoginRequest;
import com.estacionamiento.dto.LoginResponse;
import com.estacionamiento.dto.RegisterRequest;
import com.estacionamiento.dto.RegisterResponse;
import com.estacionamiento.entity.Empleado;
import com.estacionamiento.entity.Nacionalidad;  
import com.estacionamiento.entity.Sexo;         
import com.estacionamiento.entity.Distrito;     
import com.estacionamiento.repository.NacionalidadRepository;  
import com.estacionamiento.repository.SexoRepository;          
import com.estacionamiento.repository.DistritoRepository;  
import com.estacionamiento.repository.EmpleadoRepository;
import com.estacionamiento.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */

@Service
@RequiredArgsConstructor
public class AuthService{
    
    private final EmpleadoRepository empleadoRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final NacionalidadRepository nacionalidadRepository;
    private final SexoRepository sexoRepository;
    private final DistritoRepository distritoRepository;
    
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    
public LoginResponse authenticate(LoginRequest request) {
    System.out.println("=== INTENTANDO AUTENTICAR ===");
    System.out.println("Username recibido: " + request.getUsername());
    System.out.println("Password recibido: " + request.getPassword());
    
    var empleadoOpt = empleadoRepository.findByIdEmpleado(request.getUsername());
    if (empleadoOpt.isPresent()) {
        var emp = empleadoOpt.get();
        System.out.println("Empleado encontrado: " + emp.getIdEmpleado());
        System.out.println("Password en BD: " + emp.getPassword());
        System.out.println("Password length: " + emp.getPassword().length());
        
        boolean matches = passwordEncoder.matches(request.getPassword(), emp.getPassword());
        System.out.println("¿Password coincide manualmente? " + matches);
    } else {
        System.out.println("❌ Empleado NO encontrado en BD");
        return null;
    }
    
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );
    
    var empleado = empleadoRepository.findByIdEmpleado(request.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    
    var jwtToken = jwtUtil.generateToken(empleado);
    
    return LoginResponse.builder()
        .token(jwtToken)
        .idEmpleado(empleado.getIdEmpleado())
        .nombre(empleado.getNombres() + " " + empleado.getApellidos())
        .build();
}


public RegisterResponse register(RegisterRequest request) {
   
    if (empleadoRepository.findByIdEmpleado(request.getIdEmpleado()).isPresent()) {
        throw new RuntimeException("El ID de empleado ya existe");
    }
    
    if (empleadoRepository.findByCorreo(request.getCorreo()).isPresent()) {
        throw new RuntimeException("El correo ya está registrado");
    }
    
    if (empleadoRepository.findByNumDoc(request.getNumDoc()).isPresent()) {
        throw new RuntimeException("El número de documento ya está registrado");
    }
    

    var empleado = new Empleado();
    empleado.setIdEmpleado(request.getIdEmpleado());
    empleado.setPassword(passwordEncoder.encode(request.getPassword()));
    empleado.setNombres(request.getNombres());
    empleado.setApellidos(request.getApellidos());
    empleado.setNumDoc(request.getNumDoc());
    empleado.setTelefono(request.getTelefono());
    empleado.setCorreo(request.getCorreo());
    empleado.setDireccion(request.getDireccion());
   
    Nacionalidad nacionalidadDefault = nacionalidadRepository.findById(1L)
        .orElse(nacionalidadRepository.findAll().get(0)); 
    empleado.setNacionalidad(nacionalidadDefault);
    
    Sexo sexoDefault = sexoRepository.findById(1L)
        .orElse(sexoRepository.findAll().get(0)); 
    empleado.setSexo(sexoDefault);
    
    Distrito distritoDefault = distritoRepository.findById(1L)
        .orElse(distritoRepository.findAll().get(0)); 
    empleado.setDistrito(distritoDefault);
    
    var savedEmpleado = empleadoRepository.save(empleado);
    
    return RegisterResponse.builder()
        .message("Empleado registrado exitosamente")
        .idEmpleado(savedEmpleado.getIdEmpleado())
        .nombre(savedEmpleado.getNombres() + " " + savedEmpleado.getApellidos())
        .build();
}
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return empleadoRepository.findByIdEmpleado(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}