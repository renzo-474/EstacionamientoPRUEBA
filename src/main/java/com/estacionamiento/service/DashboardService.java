/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.service;
import com.estacionamiento.dto.DashboardDTO;
import com.estacionamiento.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
/**
 *
 * @author user
 */


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {
    
    private final RegistroEntradaRepository registroEntradaRepository;
    private final RegistroSalidaRepository registroSalidaRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;
    
    public DashboardDTO getDashboardStats() {
        DashboardDTO dashboard = new DashboardDTO();
  
        dashboard.setTotalEntradas(registroEntradaRepository.count());
 
        dashboard.setTotalSalidas(registroSalidaRepository.count());
        
        dashboard.setVehiculosActivos(registroEntradaRepository.countEntradasSinSalida());
  
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);
        Double ingresosDia = registroSalidaRepository.calcularIngresosPorPeriodo(inicioDia, finDia);
        dashboard.setIngresosDia(ingresosDia != null ? ingresosDia : 0.0);
    
        Map<String, Long> entradasPorNivel = new HashMap<>();
        List<Object[]> niveles = registroEntradaRepository.countEntradasPorNivel();
        for (Object[] nivel : niveles) {
            entradasPorNivel.put("Nivel " + nivel[0], (Long) nivel[1]);
        }
        dashboard.setEntradasPorNivel(entradasPorNivel);

        List<Map<String, Object>> ocupacionPorHora = new ArrayList<>();
        LocalDateTime hace24Horas = LocalDateTime.now().minusHours(24);
        List<Object[]> ocupacion = registroEntradaRepository.getOcupacionPorHora(hace24Horas);
        
        for (Object[] hora : ocupacion) {
            Map<String, Object> horaMap = new HashMap<>();
            horaMap.put("hora", hora[0]);
            horaMap.put("entradas", hora[1]);
            horaMap.put("salidas", hora[2]);
            ocupacionPorHora.add(horaMap);
        }
        dashboard.setOcupacionPorHora(ocupacionPorHora);
        
        return dashboard;
    }
}