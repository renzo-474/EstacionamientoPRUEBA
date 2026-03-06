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
@Table(name = "Vehiculos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {
    
    @Id
    @Column(name = "NroPlaca", length = 10)
    private String nroPlaca;
    
    @ManyToOne
    @JoinColumn(name = "CodColor")
    private Color color;
    
    @ManyToOne
    @JoinColumn(name = "CodTipoVhc")
    private TipoVehiculo tipoVehiculo;
    
    @ManyToOne
    @JoinColumn(name = "CodMarcaModelo")
    private MarcaModelo marcaModelo;
    
    @Column(name = "Lunas_Polarizadas", length = 10)
    private String lunasPolarizadas;
}
