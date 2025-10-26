package com.SanIsidro.repository;

import com.SanIsidro.model.Pedido;
import com.SanIsidro.model.Pedido.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para Pedido - Implementa patrón DAO
 * Proporciona acceso a datos de pedidos con consultas específicas del negocio
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    /**
     * Buscar pedido por número único
     */
    Optional<Pedido> findByNumeroPedido(String numeroPedido);
    
    /**
     * Buscar pedidos por estado
     */
    List<Pedido> findByEstadoOrderByHoraPedidoDesc(EstadoPedido estado);
    
    /**
     * Buscar pedidos por cliente (email)
     */
    List<Pedido> findByEmailClienteOrderByHoraPedidoDesc(String emailCliente);
    
    /**
     * Buscar pedidos por rango de fechas
     */
    @Query("SELECT p FROM Pedido p WHERE p.horaPedido BETWEEN :fechaInicio AND :fechaFin ORDER BY p.horaPedido DESC")
    List<Pedido> findByFechaRange(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                  @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Buscar pedidos del día actual
     */
    @Query("SELECT p FROM Pedido p WHERE DATE(p.horaPedido) = CURRENT_DATE ORDER BY p.horaPedido DESC")
    List<Pedido> findPedidosDelDia();
    
    /**
     * Buscar pedidos pendientes y en preparación
     */
    @Query("SELECT p FROM Pedido p WHERE p.estado IN ('PENDIENTE', 'EN_PREPARACION') ORDER BY p.horaPedido ASC")
    List<Pedido> findPedidosActivos();
    
    /**
     * Contar pedidos por estado
     */
    Long countByEstado(EstadoPedido estado);
    
    /**
     * Obtener pedidos por mesa
     */
    List<Pedido> findByNumeroMesaAndEstadoNotOrderByHoraPedidoDesc(Integer numeroMesa, EstadoPedido estado);
    
    /**
     * Buscar pedidos por cliente (nombre o teléfono)
     */
    @Query("SELECT p FROM Pedido p WHERE LOWER(p.nombreCliente) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR p.telefonoCliente LIKE CONCAT('%', :busqueda, '%') ORDER BY p.horaPedido DESC")
    List<Pedido> findByClienteInfo(@Param("busqueda") String busqueda);
    
    /**
     * Obtener estadísticas de pedidos por día
     */
    @Query("SELECT DATE(p.horaPedido) as fecha, COUNT(p) as cantidad, SUM(p.total) as totalVentas " +
           "FROM Pedido p WHERE p.horaPedido >= :fechaInicio GROUP BY DATE(p.horaPedido) ORDER BY fecha DESC")
    List<Object[]> getEstadisticasPorDia(@Param("fechaInicio") LocalDateTime fechaInicio);
    
    /**
     * Obtener pedidos con mayor valor
     */
    @Query("SELECT p FROM Pedido p WHERE p.total >= :montoMinimo ORDER BY p.total DESC")
    List<Pedido> findPedidosAltos(@Param("montoMinimo") java.math.BigDecimal montoMinimo);
    
    /**
     * Verificar si existe pedido con número específico
     */
    boolean existsByNumeroPedido(String numeroPedido);
    
    /**
     * Obtener último número de pedido generado
     */
    @Query("SELECT p.numeroPedido FROM Pedido p ORDER BY p.id DESC LIMIT 1")
    Optional<String> findLastNumeroPedido();
}