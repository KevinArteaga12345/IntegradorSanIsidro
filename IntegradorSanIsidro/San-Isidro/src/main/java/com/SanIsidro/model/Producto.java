package com.SanIsidro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad Producto - Representa los productos del menú del restaurante
 * Implementa principios SOLID: Single Responsibility (solo maneja datos del producto)
 */
@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "Formato de precio inválido")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
    
    @Size(max = 255, message = "La URL de imagen no puede exceder 255 caracteres")
    @Column(name = "imagen_url")
    private String imagenUrl;
    
    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
    @Column(nullable = false, length = 50)
    private String categoria;
    
    @Builder.Default
    @Column(nullable = false)
    private Boolean disponible = true;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        
        // Validación adicional usando Apache Commons
        if (StringUtils.isBlank(nombre)) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    /**
     * Método de utilidad para verificar disponibilidad
     * Implementa principio de encapsulación
     */
    public boolean estaDisponible() {
        return disponible != null && disponible;
    }
    
    /**
     * Método para formatear precio con moneda peruana
     */
    public String getPrecioFormateado() {
        return "S/ " + precio.toString();
    }
}