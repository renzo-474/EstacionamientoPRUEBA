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
@Table(name = "Registro_Entrada")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroEntrada {
    
    @Id
    @Column(name = "IdEntrada", length = 10)
    private String idEntrada;
    
    @ManyToOne
    @JoinColumn(name = "IdCliente")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "IdEmpleado")
    private Empleado empleado;
    
    @ManyToOne
    @JoinColumn(name = "CodDocPaga")
    private TipoDocumentoPago tipoDocumentoPago;
    
    @ManyToOne
    @JoinColumn(name = "NroPlaca")
    private Vehiculo vehiculo;
    
    @Column(name = "NroDocumento")
    private Integer nroDocumento;
    
    @Column(name = "Nivel")
    private Integer nivel;
    
    @Column(name = "Zona", length = 20)
    private String zona;
    
    @Column(name = "FechaIngreso")
    private LocalDateTime fechaIngreso;
    
    @Column(name = "HoraIngreso")
    private LocalTime horaIngreso;
}