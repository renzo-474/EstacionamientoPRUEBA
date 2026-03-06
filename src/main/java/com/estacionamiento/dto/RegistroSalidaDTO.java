/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;
/**
 *
 * @author user
 */

@Data
public class RegistroSalidaDTO {
    private String idSalida;
    private String idEntrada;
    private String idCliente;
    private String nombreCliente;
    private String idEmpleado;
    private String nombreEmpleado;
    private Integer codTipoVhc;
    private LocalDateTime fechaSalida;
    private LocalTime horaSalida;
    private Double montoTotal;
}
