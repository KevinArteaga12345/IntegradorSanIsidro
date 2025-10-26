package com.SanIsidro.repository;

import com.SanIsidro.model.Reserva;
import com.SanIsidro.model.Reserva.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Repositorio para Reserva - Implementa patrón DAO
 * Maneja consultas específicas para el sistema de reservas
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    /**
     * Buscar reservas por fecha
     */
    List<Reserva> findByFechaReservaOrderByHoraReserva(LocalDate fechaReserva);
    
    /**
     * Buscar reservas por estado
     */
    List<Reserva> findByEstadoOrderByFechaReservaDescHoraReservaAsc(EstadoReserva estado);
    
    /**
     * Buscar reservas por cliente (email)
     */
    List<Reserva> findByEmailClienteOrderByFechaReservaDesc(String emailCliente);
    
    /**
     * Verificar disponibilidad de mesa en fecha y hora específica
     */
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.fechaReserva = :fecha " +
           "AND r.horaReserva = :hora AND r.estado IN ('CONFIRMADA', 'OCUPADA') " +
           "AND (:numeroMesa IS NULL OR r.numeroMesa = :numeroMesa)")
    Long countReservasEnHorario(@Param("fecha") LocalDate fecha, 
                               @Param("hora") LocalTime hora,
                               @Param("numeroMesa") Integer numeroMesa);
    
    /**
     * Buscar reservas del día actual
     */
    @Query("SELECT r FROM Reserva r WHERE r.fechaReserva = CURRENT_DATE ORDER BY r.horaReserva ASC")
    List<Reserva> findReservasDelDia();
    
    /**
     * Buscar reservas activas (confirmadas y ocupadas)
     */
    @Query("SELECT r FROM Reserva r WHERE r.estado IN ('CONFIRMADA', 'OCUPADA') " +
           "AND r.fechaReserva >= CURRENT_DATE ORDER BY r.fechaReserva ASC, r.horaReserva ASC")
    List<Reserva> findReservasActivas();
    
    /**
     * Buscar reservas por mesa
     */
    List<Reserva> findByNumeroMesaAndFechaReservaOrderByHoraReserva(Integer numeroMesa, LocalDate fechaReserva);
    
    /**
     * Buscar reservas por rango de fechas
     */
    @Query("SELECT r FROM Reserva r WHERE r.fechaReserva BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY r.fechaReserva ASC, r.horaReserva ASC")
    List<Reserva> findByFechaRange(@Param("fechaInicio") LocalDate fechaInicio, 
                                  @Param("fechaFin") LocalDate fechaFin);
    
    /**
     * Buscar reservas por cliente (nombre o teléfono)
     */
    @Query("SELECT r FROM Reserva r WHERE LOWER(r.nombreCliente) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR r.telefonoCliente LIKE CONCAT('%', :busqueda, '%') ORDER BY r.fechaReserva DESC")
    List<Reserva> findByClienteInfo(@Param("busqueda") String busqueda);
    
    /**
     * Contar reservas por estado
     */
    Long countByEstado(EstadoReserva estado);
    
    /**
     * Obtener mesas ocupadas en fecha y hora específica
     */
    @Query("SELECT DISTINCT r.numeroMesa FROM Reserva r WHERE r.fechaReserva = :fecha " +
           "AND r.horaReserva = :hora AND r.estado IN ('CONFIRMADA', 'OCUPADA') " +
           "AND r.numeroMesa IS NOT NULL")
    List<Integer> findMesasOcupadas(@Param("fecha") LocalDate fecha, @Param("hora") LocalTime hora);
    
    /**
     * Verificar conflictos de horario para una mesa específica
     */
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.numeroMesa = :numeroMesa " +
           "AND r.fechaReserva = :fecha AND r.estado IN ('CONFIRMADA', 'OCUPADA') " +
           "AND ((r.horaReserva <= :horaInicio AND FUNCTION('ADDTIME', r.horaReserva, '02:00:00') > :horaInicio) " +
           "OR (r.horaReserva < :horaFin AND FUNCTION('ADDTIME', r.horaReserva, '02:00:00') >= :horaFin) " +
           "OR (r.horaReserva >= :horaInicio AND r.horaReserva < :horaFin))")
    Long countConflictosHorario(@Param("numeroMesa") Integer numeroMesa,
                               @Param("fecha") LocalDate fecha,
                               @Param("horaInicio") LocalTime horaInicio,
                               @Param("horaFin") LocalTime horaFin);
    
    /**
     * Obtener estadísticas de reservas por día
     */
    @Query("SELECT r.fechaReserva as fecha, COUNT(r) as cantidad, r.estado as estado " +
           "FROM Reserva r WHERE r.fechaReserva >= :fechaInicio " +
           "GROUP BY r.fechaReserva, r.estado ORDER BY fecha DESC")
    List<Object[]> getEstadisticasPorDia(@Param("fechaInicio") LocalDate fechaInicio);
}