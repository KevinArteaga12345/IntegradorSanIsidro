package com.SanIsidro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 1. ESTO ES FUNDAMENTAL: Indica que la clase maneja peticiones REST
@RestController
public class SaludoController {

    // 2. ESTO ES FUNDAMENTAL: Mapea la petición GET al path /api/saludo
    @GetMapping("/api/saludo")
    public String saludarAPI() {
        return "¡La API de Spring Boot FUNCIONA!";
    }

    // Opcional, pero para probar la raíz también:
    @GetMapping("/")
    public String inicio() {
        return "Servidor SanIsidro en la raíz.";
    }
}