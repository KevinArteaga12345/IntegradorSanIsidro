package com.SanIsidro.repository;

import com.SanIsidro.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Producto - Implementa patrón DAO
 * Extiende JpaRepository para operaciones CRUD básicas
 * Implementa principio de Inversión de Dependencias (SOLID)
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    /**
     * Buscar productos disponibles
     * Implementa principio de responsabilidad única
     */
    List<Producto> findByDisponibleTrue();
    
    /**
     * Buscar productos por categoría
     */
    List<Producto> findByCategoriaAndDisponibleTrue(String categoria);
    
    /**
     * Buscar productos por rango de precio
     */
    List<Producto> findByPrecioBetweenAndDisponibleTrue(BigDecimal precioMin, BigDecimal precioMax);
    
    /**
     * Buscar productos por nombre (búsqueda parcial)
     */
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND p.disponible = true")
    List<Producto> findByNombreContainingIgnoreCaseAndDisponibleTrue(@Param("nombre") String nombre);
    
    /**
     * Obtener todas las categorías disponibles
     */
    @Query("SELECT DISTINCT p.categoria FROM Producto p WHERE p.disponible = true ORDER BY p.categoria")
    List<String> findDistinctCategorias();
    
    /**
     * Buscar producto por nombre exacto
     */
    Optional<Producto> findByNombreIgnoreCase(String nombre);
    
    /**
     * Contar productos por categoría
     */
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.categoria = :categoria AND p.disponible = true")
    Long countByCategoria(@Param("categoria") String categoria);
    
    /**
     * Obtener productos más caros por categoría
     */
    @Query("SELECT p FROM Producto p WHERE p.categoria = :categoria AND p.disponible = true ORDER BY p.precio DESC")
    List<Producto> findByCategoriaOrderByPrecioDesc(@Param("categoria") String categoria);
    
    /**
     * Obtener productos más baratos por categoría
     */
    @Query("SELECT p FROM Producto p WHERE p.categoria = :categoria AND p.disponible = true ORDER BY p.precio ASC")
    List<Producto> findByCategoriaOrderByPrecioAsc(@Param("categoria") String categoria);
    
    /**
     * Buscar productos creados recientemente (últimos 30 días)
     */
    @Query("SELECT p FROM Producto p WHERE p.fechaCreacion >= CURRENT_DATE - 30 AND p.disponible = true ORDER BY p.fechaCreacion DESC")
    List<Producto> findProductosRecientes();
}