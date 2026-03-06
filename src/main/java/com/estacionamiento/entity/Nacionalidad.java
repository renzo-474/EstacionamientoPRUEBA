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
@Table(name = "Nacionalidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nacionalidad {
    
    @Id
    @Column(name = "CodNacio")
    private Integer codNacio;
    
    @Column(name = "Nombre", length = 50)
    private String nombre;
}
