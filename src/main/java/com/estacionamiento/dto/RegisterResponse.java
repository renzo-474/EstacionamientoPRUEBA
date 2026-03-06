/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.dto;

import lombok.Builder;
import lombok.Data;
/**
 *
 * @author Ryzen
 */

@Data
@Builder
public class RegisterResponse {
    private String message;
    private String idEmpleado;
    private String nombre;

}
