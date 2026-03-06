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
@Table(name = "Sexo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sexo {
    
    @Id
    @Column(name = "CodSexo")
    private Integer codSexo;
    
    @Column(name = "Nombre", length = 20)
    private String nombre;
}
