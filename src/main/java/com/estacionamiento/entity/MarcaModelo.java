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
@Table(name = "Marca_Modelo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarcaModelo {
    
    @Id
    @Column(name = "CodMarcaModelo")
    private Integer codMarcaModelo;
    
    @Column(name = "Nombre", length = 100)
    private String nombre;
}
