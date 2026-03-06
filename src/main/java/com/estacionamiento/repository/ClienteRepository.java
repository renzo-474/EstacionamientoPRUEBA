/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.repository;

import com.estacionamiento.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author user
 */


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    
    Optional<Cliente> findByNumDoc(String numDoc);
    
    List<Cliente> findByNombresContainingOrApellidosContaining(String nombres, String apellidos);
    
    @Query("SELECT c FROM Cliente c WHERE c.correo = ?1")
    Optional<Cliente> findByCorreo(String correo);
    
    @Query("SELECT COUNT(c) FROM Cliente c")
    Long countTotalClientes();
}