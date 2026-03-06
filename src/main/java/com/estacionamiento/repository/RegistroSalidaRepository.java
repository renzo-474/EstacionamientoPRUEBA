/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.repository;

import com.estacionamiento.entity.RegistroEntrada;
import com.estacionamiento.entity.RegistroSalida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
/**
 *
 * @author user
 */

@Repository
public interface RegistroSalidaRepository extends JpaRepository<RegistroSalida, String> {
    
    boolean existsByRegistroEntrada(RegistroEntrada registroEntrada);
    
    @Query("SELECT SUM(DATEDIFF(hour, re.fechaIngreso, rs.fechaSalida) * 5.0) " +
           "FROM RegistroSalida rs JOIN rs.registroEntrada re " +
           "WHERE rs.fechaSalida BETWEEN :inicio AND :fin")
    Double calcularIngresosPorPeriodo(@Param("inicio") LocalDateTime inicio, 
                                     @Param("fin") LocalDateTime fin);
}
