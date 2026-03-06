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
@Table(name = "TipoDocumentoPago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoPago {
    
    @Id
    @Column(name = "CodDocPaga")
    private Integer codDocPaga;
    
    @Column(name = "Nombre", length = 50)
    private String nombre;
}
