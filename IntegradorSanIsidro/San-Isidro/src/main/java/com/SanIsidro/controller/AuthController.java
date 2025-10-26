package com.SanIsidro.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SanIsidro.model.LoginRequest;
import com.SanIsidro.service.AuthService;

// Habilitamos CORS para que React (puerto 3000) pueda hablar con Spring Boot (puerto 8080)
@CrossOrigin(origins = "http://localhost:3000") 
@RestController
@RequestMapping("/api/auth") // Prefijo base para todas las rutas de autenticación
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint de Login: Maneja la petición POST a /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        
        // El servicio ahora devuelve un BOOLEAN (true: éxito, false: fallo).
        boolean isAuthenticated = authService.authenticate(loginRequest);
        
        Map<String, Object> response = new HashMap<>();
        
        if (isAuthenticated) {
            // Login exitoso: Devolvemos estado 200 OK
            response.put("success", true);
            // Devolvemos un token ficticio para que React pueda simular la sesión
            response.put("token", "fake-jwt-token"); 
            response.put("message", "Login exitoso. ¡Bienvenido!");
            return ResponseEntity.ok(response);
        } else {
            // Login fallido: Devolvemos estado 401 Unauthorized
            response.put("success", false);
            response.put("message", "Usuario o contraseña incorrectos");
            // Usamos UNATHORIZED para indicar fallo de credenciales
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); 
        }
    }

    // Endpoint opcional para verificar si el servidor está funcionando
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Auth service funcionando correctamente");
        return ResponseEntity.ok(response);
    }
}
