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
public class VehiculoDTO {
    private String nroPlaca;
    private Integer codColor;
    private String color;
    private Integer codTipoVhc;
    private String tipoVehiculo;
    private Integer codMarcaModelo;
    private String marcaModelo;
    private String lunasPolarizadas;
}
