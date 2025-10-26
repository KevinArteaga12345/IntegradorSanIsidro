package com.SanIsidro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Pedido - Representa los pedidos realizados por los clientes
 * Implementa principios SOLID y utiliza Google Guava para validaciones
 */
@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El número de pedido es obligatorio")
    @Column(name = "numero_pedido", nullable = false, unique = true, length = 20)
    private String numeroPedido;
    
    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre_cliente", nullable = false, length = 100)
    private String nombreCliente;
    
    @Email(message = "El formato del email es inválido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Column(name = "email_cliente", length = 100)
    private String emailCliente;
    
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Formato de teléfono inválido")
    @Column(name = "telefono_cliente", length = 15)
    private String telefonoCliente;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    
    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(name = "numero_mesa")
    private Integer numeroMesa;
    
    @Column(name = "hora_pedido", nullable = false)
    private LocalDateTime horaPedido;
    
    @Column(name = "hora_entrega")
    private LocalDateTime horaEntrega;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(length = 500)
    private String observaciones;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DetallePedido> detalles = new ArrayList<>();
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        horaPedido = LocalDateTime.now();
        
        // Validación usando Google Guava
        if (Strings.isNullOrEmpty(numeroPedido)) {
            throw new IllegalArgumentException("El número de pedido no puede estar vacío");
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    /**
     * Método para cambiar estado del pedido con validaciones
     * Implementa principio de responsabilidad única
     */
    public void cambiarEstado(EstadoPedido nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        
        // Validar transiciones de estado válidas
        if (this.estado == EstadoPedido.ENTREGADO && nuevoEstado != EstadoPedido.ENTREGADO) {
            throw new IllegalStateException("No se puede cambiar el estado de un pedido ya entregado");
        }
        
        this.estado = nuevoEstado;
        
        if (nuevoEstado == EstadoPedido.ENTREGADO) {
            this.horaEntrega = LocalDateTime.now();
        }
    }
    
    /**
     * Método para agregar detalle al pedido
     */
    public void agregarDetalle(DetallePedido detalle) {
        if (detalle == null) {
            throw new IllegalArgumentException("El detalle no puede ser nulo");
        }
        
        detalle.setPedido(this);
        this.detalles.add(detalle);
    }
    
    /**
     * Método para calcular el total del pedido
     */
    public BigDecimal calcularTotal() {
        return detalles.stream()
                .map(DetallePedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Enum para estados del pedido
     */
    public enum EstadoPedido {
        PENDIENTE("Pendiente"),
        EN_PREPARACION("En Preparación"),
        LISTO("Listo"),
        ENTREGADO("Entregado"),
        CANCELADO("Cancelado");
        
        private final String descripcion;
        
        EstadoPedido(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
}