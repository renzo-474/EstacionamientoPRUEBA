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
@Table(name = "Color")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Color {
    
    @Id
    @Column(name = "CodColor")
    private Integer codColor;
    
    @Column(name = "Nombre", length = 30)
    private String nombre;
}
