package com.estacionamiento.controller;

import com.estacionamiento.entity.Empleado;
import com.estacionamiento.repository.EmpleadoRepository;
import com.estacionamiento.repository.NacionalidadRepository;
import com.estacionamiento.repository.SexoRepository;
import com.estacionamiento.repository.DistritoRepository;
import com.estacionamiento.util.JwtUtil;
import com.estacionamiento.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8080")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NacionalidadRepository nacionalidadRepository;

    @Autowired  
    private SexoRepository sexoRepository;

    @Autowired
    private DistritoRepository distritoRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        System.out.println("=== INTENTANDO AUTENTICAR ===");
        System.out.println("Datos recibidos completos: " + loginRequest);
        
        String idEmpleado = loginRequest.get("idEmpleado");
        if (idEmpleado == null) {
            idEmpleado = loginRequest.get("username");
        }
        
        String password = loginRequest.get("password");
        
        // Validar que no contenga caracteres especiales
        if (idEmpleado != null && !idEmpleado.matches("^[a-zA-Z0-9]+$")) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "El ID de empleado contiene caracteres no permitidos");
            errorResponse.put("details", "Solo se permiten letras y números");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        System.out.println("Username recibido: " + idEmpleado);

        if (idEmpleado == null || idEmpleado.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Faltan credenciales");
            errorResponse.put("details", "Se requiere ID de empleado y contraseña");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            Empleado empleado = empleadoRepository.findByIdEmpleado(idEmpleado)
                    .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));
            
            System.out.println("Empleado encontrado: " + empleado.getIdEmpleado());
            
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(idEmpleado, password)
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(idEmpleado);
            final String jwt = jwtUtil.generateToken(userDetails);

            System.out.println("=== AUTENTICACIÓN EXITOSA ===");

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("idEmpleado", empleado.getIdEmpleado());
            response.put("nombre", empleado.getNombres() + " " + empleado.getApellidos());
            response.put("rol", "EMPLEADO");
            response.put("success", true);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            System.out.println("=== ERROR DE AUTENTICACIÓN ===");
            System.out.println("Mensaje: " + e.getMessage());
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Credenciales inválidas");
            errorResponse.put("details", "Usuario o contraseña incorrectos");
            errorResponse.put("success", "false");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            System.out.println("=== ERROR INESPERADO ===");
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error en el servidor");
            errorResponse.put("details", e.getMessage());
            errorResponse.put("success", "false");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> registerRequest) {
        System.out.println("=== INTENTANDO REGISTRAR ===");
        System.out.println("Datos recibidos: " + registerRequest);
        
        try {
            String idEmpleado = (String) registerRequest.get("idEmpleado");
            String password = (String) registerRequest.get("password");
            String nombres = (String) registerRequest.get("nombres");
            String apellidos = (String) registerRequest.get("apellidos");
            String correo = (String) registerRequest.get("correo");
            String numDoc = (String) registerRequest.get("numDoc");
            String direccion = (String) registerRequest.get("direccion");
            
            // Validar formato de ID empleado
            if (idEmpleado != null && !idEmpleado.matches("^[a-zA-Z0-9]+$")) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "El ID de empleado contiene caracteres no permitidos");
                errorResponse.put("message", "Solo se permiten letras y números");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            // Validar formato de correo
            if (correo != null && !correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Formato de correo inválido");
                errorResponse.put("message", "Ingrese un correo válido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            Integer telefono = null;
            Object telefonoObj = registerRequest.get("telefono");
            if (telefonoObj != null) {
                if (telefonoObj instanceof Integer) {
                    telefono = (Integer) telefonoObj;
                } else if (telefonoObj instanceof String) {
                    try {
                        telefono = Integer.parseInt((String) telefonoObj);
                    } catch (NumberFormatException e) {
                        telefono = 0;
                    }
                } else if (telefonoObj instanceof Double) {
                    telefono = ((Double) telefonoObj).intValue();
                }
            }
            
            if (idEmpleado == null || password == null || nombres == null || apellidos == null || correo == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Faltan campos obligatorios");
                errorResponse.put("message", "Se requieren: idEmpleado, password, nombres, apellidos, correo");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            if (empleadoRepository.findByIdEmpleado(idEmpleado).isPresent()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "El ID de empleado ya existe");
                errorResponse.put("message", "Ya existe un empleado con el ID: " + idEmpleado);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            if (empleadoRepository.findByCorreo(correo).isPresent()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "El correo ya está registrado");
                errorResponse.put("message", "Ya existe un empleado con el correo: " + correo);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            if (numDoc != null && !numDoc.trim().isEmpty() && empleadoRepository.findByNumDoc(numDoc).isPresent()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "El número de documento ya está registrado");
                errorResponse.put("message", "Ya existe un empleado con el documento: " + numDoc);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
            Empleado nuevoEmpleado = new Empleado();
            nuevoEmpleado.setIdEmpleado(idEmpleado);
            nuevoEmpleado.setPassword(passwordEncoder.encode(password)); 
            nuevoEmpleado.setNombres(nombres);
            nuevoEmpleado.setApellidos(apellidos);
            nuevoEmpleado.setCorreo(correo);
            nuevoEmpleado.setNumDoc(numDoc);
            nuevoEmpleado.setTelefono(telefono);
            nuevoEmpleado.setDireccion(direccion);
            
            try {
                var nacionalidad = nacionalidadRepository.findById(1L)
                    .orElse(nacionalidadRepository.findAll().stream().findFirst().orElse(null));
                nuevoEmpleado.setNacionalidad(nacionalidad);
                
                var sexo = sexoRepository.findById(1L)
                    .orElse(sexoRepository.findAll().stream().findFirst().orElse(null));
                nuevoEmpleado.setSexo(sexo);
                
                var distrito = distritoRepository.findById(1L)
                    .orElse(distritoRepository.findAll().stream().findFirst().orElse(null));
                nuevoEmpleado.setDistrito(distrito);
                
                System.out.println("FK asignadas correctamente");
            } catch (Exception e) {
                System.out.println("Advertencia: No se pudieron asignar valores por defecto a las FK: " + e.getMessage());
                
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Error en configuración de datos maestros");
                errorResponse.put("message", "No se encontraron datos de nacionalidad, sexo o distrito. Contacte al administrador.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
            
            Empleado empleadoGuardado = empleadoRepository.save(nuevoEmpleado);
            
            System.out.println("=== EMPLEADO REGISTRADO EXITOSAMENTE ===");
            System.out.println("ID: " + empleadoGuardado.getIdEmpleado());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Empleado registrado exitosamente");
            response.put("idEmpleado", empleadoGuardado.getIdEmpleado());
            response.put("nombre", empleadoGuardado.getNombres() + " " + empleadoGuardado.getApellidos());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            System.out.println("=== ERROR AL REGISTRAR ===");
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno del servidor");
            errorResponse.put("message", "Error al procesar la solicitud: " + e.getMessage());
            errorResponse.put("details", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        System.out.println("=== VERIFICANDO TOKEN ===");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("Token no proporcionado o formato inválido");
                response.put("valid", false);
                response.put("message", "Token no proporcionado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String jwt = authHeader.substring(7);
            System.out.println("Token recibido: " + jwt.substring(0, Math.min(jwt.length(), 20)) + "...");

            if (jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.extractUsername(jwt);
                System.out.println("Token válido para usuario: " + username);
                
                Empleado empleado = empleadoRepository.findByIdEmpleado(username)
                        .orElseThrow(() -> new Exception("Usuario no encontrado"));

                response.put("valid", true);
                response.put("idEmpleado", empleado.getIdEmpleado());
                response.put("nombre", empleado.getNombres() + " " + empleado.getApellidos());
                response.put("rol", "EMPLEADO");

                return ResponseEntity.ok(response);
            } else {
                System.out.println("Token inválido o expirado");
                response.put("valid", false);
                response.put("message", "Token inválido o expirado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            System.out.println("Error al verificar token: " + e.getMessage());
            e.printStackTrace();
            response.put("valid", false);
            response.put("message", "Error al verificar token");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Endpoint de autenticación funcionando");
        response.put("status", "OK");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "AuthController funcionando correctamente");
        response.put("status", "OK");
        response.put("endpoints", "/login, /register, /verify, /check, /test");
        return ResponseEntity.ok(response);
    }
}