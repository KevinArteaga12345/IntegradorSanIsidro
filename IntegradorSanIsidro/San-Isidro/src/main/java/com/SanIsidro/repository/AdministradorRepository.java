package com.SanIsidro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sanisidro.model.Administrador;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
}
