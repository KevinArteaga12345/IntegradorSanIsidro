package com.SanIsidro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidad Reserva - Representa las reservas de mesa del restaurante
 * Implementa principios SOLID y utiliza Apache Commons para validaciones
 */
@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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
    
    @NotNull(message = "La fecha de reserva es obligatoria")
    @Future(message = "La fecha de reserva debe ser futura")
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;
    
    @NotNull(message = "La hora de reserva es obligatoria")
    @Column(name = "hora_reserva", nullable = false)
    private LocalTime horaReserva;
    
    @NotNull(message = "El número de personas es obligatorio")
    @Min(value = 1, message = "Debe ser al menos 1 persona")
    @Max(value = 20, message = "No se pueden reservar más de 20 personas")
    @Column(name = "numero_personas", nullable = false)
    private Integer numeroPersonas;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoReserva estado = EstadoReserva.PENDIENTE;
    
    @Column(name = "numero_mesa")
    private Integer numeroMesa;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(length = 500)
    private String observaciones;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        
        // Validación usando Apache Commons
        if (emailCliente != null && !EmailValidator.getInstance().isValid(emailCliente)) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        
        // Validar horario de funcionamiento (ejemplo: 11:00 - 23:00)
        if (horaReserva != null) {
            LocalTime apertura = LocalTime.of(11, 0);
            LocalTime cierre = LocalTime.of(23, 0);
            
            if (horaReserva.isBefore(apertura) || horaReserva.isAfter(cierre)) {
                throw new IllegalArgumentException("La hora de reserva debe estar entre 11:00 y 23:00");
            }
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
    
    /**
     * Método para cambiar estado de la reserva
     * Implementa principio de responsabilidad única
     */
    public void cambiarEstado(EstadoReserva nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        
        // Validar transiciones de estado
        if (this.estado == EstadoReserva.COMPLETADA && nuevoEstado != EstadoReserva.COMPLETADA) {
            throw new IllegalStateException("No se puede cambiar el estado de una reserva completada");
        }
        
        if (this.estado == EstadoReserva.CANCELADA && nuevoEstado != EstadoReserva.CANCELADA) {
            throw new IllegalStateException("No se puede cambiar el estado de una reserva cancelada");
        }
        
        this.estado = nuevoEstado;
    }
    
    /**
     * Método para asignar mesa
     */
    public void asignarMesa(Integer numeroMesa) {
        if (numeroMesa == null || numeroMesa < 1) {
            throw new IllegalArgumentException("El número de mesa debe ser válido");
        }
        
        this.numeroMesa = numeroMesa;
        
        if (this.estado == EstadoReserva.PENDIENTE) {
            this.estado = EstadoReserva.CONFIRMADA;
        }
    }
    
    /**
     * Método para verificar si la reserva es para hoy
     */
    public boolean esParaHoy() {
        return fechaReserva != null && fechaReserva.equals(LocalDate.now());
    }
    
    /**
     * Método para obtener fecha y hora completa
     */
    public LocalDateTime getFechaHoraCompleta() {
        if (fechaReserva != null && horaReserva != null) {
            return LocalDateTime.of(fechaReserva, horaReserva);
        }
        return null;
    }
    
    /**
     * Enum para estados de reserva
     */
    public enum EstadoReserva {
        PENDIENTE("Pendiente"),
        CONFIRMADA("Confirmada"),
        OCUPADA("Ocupada"),
        COMPLETADA("Completada"),
        CANCELADA("Cancelada"),
        NO_SHOW("No Show");
        
        private final String descripcion;
        
        EstadoReserva(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
}