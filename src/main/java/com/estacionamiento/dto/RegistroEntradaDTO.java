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
public class RegistroEntradaDTO {
    private String idEntrada;
    private String idCliente;
    private String nombreCliente;
    private String idEmpleado;
    private String nombreEmpleado;
    private Integer codDocPaga;
    private String tipoDocPago;
    private String nroPlaca;
    private Integer nroDocumento;
    private Integer nivel;
    private String zona;
    private LocalDateTime fechaIngreso;
    private LocalTime horaIngreso;
    private boolean tieneSalida;
}
