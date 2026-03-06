/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalTime;
/**
 *
 * @author user
 */

@Entity
@Table(name = "Registro_Salida")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroSalida {
    
    @Id
    @Column(name = "IdSalida", length = 10)
    private String idSalida;
    
    @ManyToOne
    @JoinColumn(name = "IdEntrada")
    private RegistroEntrada registroEntrada;
    
    @ManyToOne
    @JoinColumn(name = "IdCliente2")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "IdEmpleado")
    private Empleado empleado;
    
    @Column(name = "CodTipoVhc")
    private Integer codTipoVhc;
    
    @Column(name = "FechaSalida")
    private LocalDateTime fechaSalida;
    
    @Column(name = "HoraSalida")
    private LocalTime horaSalida;
}
