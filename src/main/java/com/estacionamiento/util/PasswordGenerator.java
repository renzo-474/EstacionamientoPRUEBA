/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.estacionamiento.util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/**
 *
 * @author user
 */

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Contraseña encriptada: " + encodedPassword);
        System.out.println("\nCopia este SQL y ejecútalo en SSMS:");
        System.out.println("INSERT INTO Empleado (IdEmpleado, CodNacio, CodSexo, CodDistrito, Nombres, Apellidos, Password, NumDoc, Telefono, Correo, Direccion)");
        System.out.println("VALUES ('admin', 1, 1, 1, 'Admin', 'Sistema', '" + encodedPassword + "', '12345678', 999999999, 'admin@parking.com', 'Av. Principal 123');");
    }
}

