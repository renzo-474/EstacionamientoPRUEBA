/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.controller;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author Ryzen
 */


@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @GetMapping("/ingresos")
    public Map<String, Object> obtenerReporteIngresos(
            @RequestParam String desde,
            @RequestParam String hasta
    ) {
        // En producción, harías una consulta a la base de datos
        Map<String, Object> datos = new HashMap<>();
        datos.put("totalPeriodo", 15750.00);
        datos.put("diasOperacion", 30);
        datos.put("promedioDiario", 525.00);
        datos.put("mejorDia", Map.of("fecha", "2024-06-15", "monto", 892.50));
        datos.put("peorDia", Map.of("fecha", "2024-06-22", "monto", 125.00));
        datos.put("ingresosPorTipo", List.of(
                Map.of("tipo", "Automóvil", "monto", 10500.00, "porcentaje", 66.7),
                Map.of("tipo", "Motocicleta", "monto", 2250.00, "porcentaje", 14.3),
                Map.of("tipo", "Camioneta", "monto", 3000.00, "porcentaje", 19.0)
        ));
        return datos;
    }

    @GetMapping("/exportar")
    public void exportarReporte(
            @RequestParam String tipo,
            @RequestParam String formato,
            @RequestParam String desde,
            @RequestParam String hasta,
            HttpServletResponse response
    ) throws IOException {

        String filename = "reporte_" + tipo + "_" + desde + "_a_" + hasta + "." + formato;
        response.setContentType(
            formato.equals("pdf") ? "application/pdf" :
            formato.equals("xlsx") ? "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" :
            "application/octet-stream"
        );
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        OutputStream out = response.getOutputStream();

        // Simulación de contenido exportado
        if (formato.equals("pdf")) {
            out.write(("PDF generado para " + tipo).getBytes());
        } else if (formato.equals("xlsx")) {
            out.write(("Excel generado para " + tipo).getBytes());
        } else {
            out.write("Formato no soportado".getBytes());
        }

        out.flush();
        out.close();
    }
}
