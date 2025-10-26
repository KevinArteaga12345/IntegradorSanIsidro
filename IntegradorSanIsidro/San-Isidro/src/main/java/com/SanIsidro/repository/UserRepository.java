package com.SanIsidro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SanIsidro.model.Usuario;  // usa la ruta exacta a tu clase Usuario

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por nombre de usuario
    Optional<Usuario> findByUsername(String username);

}
