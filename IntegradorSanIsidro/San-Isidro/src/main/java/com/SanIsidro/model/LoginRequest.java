package com.SanIsidro.model;

// Usamos Lombok si lo tienes (es opcional pero simplifica)
// Si no usas Lombok, debes escribir los getters, setters y constructor manualmente.

// import lombok.Data; 

// @Data // Si usas Lombok, descomenta esta línea
public class LoginRequest {
    
    private String username;
    private String password;

    // Si NO usas Lombok, añade el constructor vacío, getters y setters:

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}