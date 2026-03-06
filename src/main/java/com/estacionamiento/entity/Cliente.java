/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
/**
 *
 * @author user
 */

@Entity 
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    @Id
    @Column(name = "IdCliente", length = 10)
    private String idCliente;
    
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
    
    @Column(name = "Apellidos", length = 15)
    private String apellidos;
    
    @Column(name = "NumDoc", length = 15)
    private String numDoc;
    
    @Column(name = "NroLicencia")
    private Integer nroLicencia;
    
    @Column(name = "Telefono")
    private Integer telefono;
    
    @Column(name = "Correo", length = 20)
    private String correo;
    
    @Column(name = "Direccion", length = 20)
    private String direccion;
}
