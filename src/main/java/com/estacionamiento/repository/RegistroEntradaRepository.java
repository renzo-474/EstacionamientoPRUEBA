/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.repository;
import com.estacionamiento.entity.RegistroEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * @author user
 */


@Repository
public interface RegistroEntradaRepository extends JpaRepository<RegistroEntrada, String> {
    
    @Query("SELECT re FROM RegistroEntrada re WHERE NOT EXISTS " +
           "(SELECT rs FROM RegistroSalida rs WHERE rs.registroEntrada = re)")
    List<RegistroEntrada> findEntradasSinSalida();
    
    @Query("SELECT COUNT(re) FROM RegistroEntrada re WHERE NOT EXISTS " +
           "(SELECT rs FROM RegistroSalida rs WHERE rs.registroEntrada = re)")
    Long countEntradasSinSalida();
    
    @Query("SELECT re.nivel, COUNT(re) FROM RegistroEntrada re GROUP BY re.nivel")
    List<Object[]> countEntradasPorNivel();
    
    @Query("SELECT HOUR(re.horaIngreso), COUNT(re), " +
           "(SELECT COUNT(rs) FROM RegistroSalida rs WHERE HOUR(rs.horaSalida) = HOUR(re.horaIngreso)) " +
           "FROM RegistroEntrada re WHERE re.fechaIngreso >= :desde " +
           "GROUP BY HOUR(re.horaIngreso)")
    List<Object[]> getOcupacionPorHora(@Param("desde") LocalDateTime desde);
}