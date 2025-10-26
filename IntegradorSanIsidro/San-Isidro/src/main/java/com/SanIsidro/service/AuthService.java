package com.SanIsidro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SanIsidro.model.LoginRequest;
import com.SanIsidro.model.Usuario; // Importa tu modelo de Usuario (Entidad JPA)
import com.SanIsidro.repository.UserRepository; // Importa tu repositorio (Acceso a BD)

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository; // Inyecta el repositorio

    // *** La autenticación simple devuelve un booleano (true/false) ***
    public boolean authenticate(LoginRequest loginRequest) {
        
        // 1. Buscar al usuario en la base de datos por el nombre de usuario
        Usuario user = userRepository.findByUsername(loginRequest.getUsername())
                                    .orElse(null); 

        if (user == null) {
            // Usuario no encontrado en la BD
            return false;
        }

        // 2. Comparación de la contraseña en texto plano
        // ADVERTENCIA: Esta es la parte INSEGURA, se compara la clave enviada 
        // con la clave almacenada en la BD.
        if (loginRequest.getPassword().equals(user.getPassword())) {
            // Contraseña correcta
            return true;
        }

        // 3. Contraseña incorrecta
        return false; 
    }
}
