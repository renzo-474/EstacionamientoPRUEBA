/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.dto;
import lombok.Data;
/**
 *
 * @author user
 */
@Data
public class ClienteDTO {
    private String idCliente;
    private Integer codNacio;
    private String nacionalidad;
    private Integer codSexo;
    private String sexo;
    private Integer codDistrito;
    private String distrito;
    private String nombres;
    private String apellidos;
    private String numDoc;
    private Integer nroLicencia;
    private Integer telefono;
    private String correo;
    private String direccion;
}
