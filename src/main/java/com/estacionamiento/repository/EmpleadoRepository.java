/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.repository;

import com.estacionamiento.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 *
 * @author user
 */


@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, String> {
    
    Optional<Empleado> findByIdEmpleado(String idEmpleado);
    
    Optional<Empleado> findByCorreo(String correo);
    
    Optional<Empleado> findByNumDoc(String numDoc);
}