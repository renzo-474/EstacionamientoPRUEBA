/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.dto;

import lombok.Data;
/**
 *
 * @author Ryzen
 */

@Data
public class RegisterRequest {
    private String idEmpleado;
    private String password;
    private String nombres;
    private String apellidos;
    private String numDoc;
    private Integer telefono;
    private String correo;
    private String direccion;
    private Long codNacio;   
    private Long codSexo;    
    private Long codDistrito;     
   
}