/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;
/**
 *
 * @author user
 */

@Data
public class DashboardDTO {
    private Long totalEntradas;
    private Long totalSalidas;
    private Long vehiculosActivos;
    private Double ingresosDia;
    private Map<String, Long> entradasPorNivel;
    private List<Map<String, Object>> ocupacionPorHora;
}
