package com.SanIsidro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SanIsidro.model.LoginRequest;
import com.SanIsidro.service.AuthService;

// Habilitamos CORS para que React (puerto 3000) pueda hablar con Spring Boot (puerto 8080)
@CrossOrigin(origins = "http://localhost:3000") 
@RestController // Marca esta clase para manejar peticiones REST
@RequestMapping("/api/admin") // Prefijo para todas las rutas de este controlador
public class AdminController {

    @Autowired
    private AuthService authService;

    // Método para manejar la petición POST a /api/admin/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        
        // Usamos el servicio para verificar las credenciales
        // NOTA: El servicio ahora devuelve TRUE (éxito) o FALSE (falla).
        if (authService.authenticate(loginRequest)) {
            // Si es exitoso, devolvemos un estado 200 OK y un mensaje.
            // En un sistema real, devolverías un token JWT aquí.
            return ResponseEntity.ok(
                "{\"message\": \"Login exitoso\", \"token\": \"fake-jwt-token\"}"
            );
        } else {
            // Si falla, devolvemos un estado 401 Unauthorized y un error.
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("{\"message\": \"Credenciales inválidas\"}");
        }
    }
}
