/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author user
 */
@Entity
@Table(name = "Empleado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado implements UserDetails {
    
    @Id
    @Column(name = "IdEmpleado", length = 10)
    private String idEmpleado;
    
    @ManyToOne
    @JoinColumn(name = "CodNacio")
    private Nacionalidad nacionalidad;
    
    @ManyToOne
    @JoinColumn(name = "CodSexo")
    private Sexo sexo;
    
    @ManyToOne
    @JoinColumn(name = "CodDistrito")
    private Distrito distrito;
    
    @Column(name = "Nombres", length = 15)
    private String nombres;
    
    @Column(name = "Apellidos", length = 50)
    private String apellidos;
    
    @Column(name = "Password", length = 15)
    private String password;
    
    @Column(name = "NumDoc", length = 15)
    private String numDoc;
    
    @Column(name = "Telefono")
    private Integer telefono;
    
    @Column(name = "Correo", length = 50)
    private String correo;
    
    @Column(name = "Direccion", length = 100)
    private String direccion;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_EMPLEADO"));
    }
    
    @Override
    public String getUsername() {
        return idEmpleado;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getNombre() {
        return nombres;  
    }
    
    public String getApellido() {
        return apellidos;  
    }
}