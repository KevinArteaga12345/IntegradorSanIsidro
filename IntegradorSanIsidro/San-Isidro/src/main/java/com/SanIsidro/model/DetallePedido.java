package com.SanIsidro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad DetallePedido - Representa los items individuales de un pedido
 * Implementa principios SOLID: Single Responsibility
 */
@Entity
@Table(name = "detalle_pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Max(value = 99, message = "La cantidad no puede exceder 99")
    @Column(nullable = false)
    private Integer cantidad;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El subtotal debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Size(max = 200, message = "Las observaciones no pueden exceder 200 caracteres")
    @Column(name = "observaciones_item", length = 200)
    private String observacionesItem;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        
        // Calcular subtotal automáticamente
        if (cantidad != null && precioUnitario != null) {
            subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
        
        // Validaciones de negocio
        if (producto == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        
        if (!producto.estaDisponible()) {
            throw new IllegalStateException("No se puede agregar un producto no disponible");
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        // Recalcular subtotal si cambia cantidad o precio
        if (cantidad != null && precioUnitario != null) {
            subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }
    
    /**
     * Método para actualizar cantidad y recalcular subtotal
     * Implementa principio de encapsulación
     */
    public void actualizarCantidad(Integer nuevaCantidad) {
        if (nuevaCantidad == null || nuevaCantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        
        if (nuevaCantidad > 99) {
            throw new IllegalArgumentException("La cantidad no puede exceder 99");
        }
        
        this.cantidad = nuevaCantidad;
        this.subtotal = this.precioUnitario.multiply(BigDecimal.valueOf(nuevaCantidad));
    }
    
    /**
     * Método para obtener el nombre del producto
     */
    public String getNombreProducto() {
        return producto != null ? producto.getNombre() : "";
    }
    
    /**
     * Método para obtener subtotal formateado
     */
    public String getSubtotalFormateado() {
        return "S/ " + subtotal.toString();
    }
}