package com.SanIsidro.service;

import com.SanIsidro.model.Pedido;
import com.SanIsidro.model.DetallePedido;
import com.SanIsidro.model.Producto;
import com.SanIsidro.model.Pedido.EstadoPedido;
import com.SanIsidro.repository.PedidoRepository;
import com.SanIsidro.repository.ProductoRepository;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Servicio para gestión de pedidos
 * Implementa principios SOLID y utiliza Google Guava y Apache Commons
 */
@Service
@Transactional
public class PedidoService {
    
    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    
    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = Preconditions.checkNotNull(pedidoRepository, 
            "PedidoRepository no puede ser nulo");
        this.productoRepository = Preconditions.checkNotNull(productoRepository, 
            "ProductoRepository no puede ser nulo");
    }
    
    /**
     * Crear nuevo pedido
     */
    public Pedido crearPedido(Pedido pedido, List<DetallePedido> detalles) {
        Preconditions.checkNotNull(pedido, "El pedido no puede ser nulo");
        Preconditions.checkNotNull(detalles, "Los detalles no pueden ser nulos");
        Preconditions.checkArgument(!detalles.isEmpty(), "El pedido debe tener al menos un producto");
        
        // Validaciones usando Apache Commons
        if (StringUtils.isBlank(pedido.getNombreCliente())) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        
        // Generar número de pedido único
        String numeroPedido = generarNumeroPedido();
        pedido.setNumeroPedido(numeroPedido);
        
        // Validar y procesar detalles
        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido detalle : detalles) {
            validarDetallePedido(detalle);
            detalle.setPedido(pedido);
            total = total.add(detalle.getSubtotal());
        }
        
        pedido.setTotal(total);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.getDetalles().addAll(detalles);
        
        logger.info("Creando nuevo pedido para cliente: {}", pedido.getNombreCliente());
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        logger.info("Pedido creado exitosamente con número: {}", numeroPedido);
        
        return pedidoGuardado;
    }
    
    /**
     * Obtener pedido por ID
     */
    @Transactional(readOnly = true)
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        Preconditions.checkNotNull(id, "El ID no puede ser nulo");
        Preconditions.checkArgument(id > 0, "El ID debe ser mayor a 0");
        
        logger.info("Buscando pedido con ID: {}", id);
        return pedidoRepository.findById(id);
    }
    
    /**
     * Obtener pedido por número
     */
    @Transactional(readOnly = true)
    public Optional<Pedido> obtenerPedidoPorNumero(String numeroPedido) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(numeroPedido), 
            "El número de pedido no puede estar vacío");
        
        logger.info("Buscando pedido con número: {}", numeroPedido);
        return pedidoRepository.findByNumeroPedido(numeroPedido);
    }
    
    /**
     * Obtener todos los pedidos del día
     */
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosDelDia() {
        logger.info("Obteniendo pedidos del día actual");
        return pedidoRepository.findPedidosDelDia();
    }
    
    /**
     * Obtener pedidos por estado
     */
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosPorEstado(EstadoPedido estado) {
        Preconditions.checkNotNull(estado, "El estado no puede ser nulo");
        
        logger.info("Obteniendo pedidos con estado: {}", estado);
        return pedidoRepository.findByEstadoOrderByHoraPedidoDesc(estado);
    }
    
    /**
     * Obtener pedidos activos (pendientes y en preparación)
     */
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosActivos() {
        logger.info("Obteniendo pedidos activos");
        return pedidoRepository.findPedidosActivos();
    }
    
    /**
     * Cambiar estado del pedido
     */
    public Pedido cambiarEstadoPedido(Long id, EstadoPedido nuevoEstado) {
        Preconditions.checkNotNull(id, "El ID no puede ser nulo");
        Preconditions.checkNotNull(nuevoEstado, "El nuevo estado no puede ser nulo");
        
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isEmpty()) {
            throw new IllegalArgumentException("Pedido no encontrado con ID: " + id);
        }
        
        Pedido pedido = pedidoOpt.get();
        EstadoPedido estadoAnterior = pedido.getEstado();
        
        pedido.cambiarEstado(nuevoEstado);
        
        logger.info("Cambiando estado del pedido {} de {} a {}", 
            pedido.getNumeroPedido(), estadoAnterior, nuevoEstado);
        
        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        logger.info("Estado del pedido actualizado exitosamente");
        
        return pedidoActualizado;
    }
    
    /**
     * Buscar pedidos por cliente
     */
    @Transactional(readOnly = true)
    public List<Pedido> buscarPedidosPorCliente(String busqueda) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(busqueda), 
            "El término de búsqueda no puede estar vacío");
        
        logger.info("Buscando pedidos por cliente: {}", busqueda);
        return pedidoRepository.findByClienteInfo(busqueda);
    }
    
    /**
     * Obtener pedidos por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<Pedido> obtenerPedidosPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Preconditions.checkNotNull(fechaInicio, "La fecha de inicio no puede ser nula");
        Preconditions.checkNotNull(fechaFin, "La fecha de fin no puede ser nula");
        Preconditions.checkArgument(fechaFin.isAfter(fechaInicio), 
            "La fecha de fin debe ser posterior a la fecha de inicio");
        
        logger.info("Obteniendo pedidos entre {} y {}", fechaInicio, fechaFin);
        return pedidoRepository.findByFechaRange(fechaInicio, fechaFin);
    }
    
    /**
     * Obtener estadísticas de pedidos
     */
    @Transactional(readOnly = true)
    public EstadisticasPedidos obtenerEstadisticas() {
        logger.info("Calculando estadísticas de pedidos");
        
        Long pendientes = pedidoRepository.countByEstado(EstadoPedido.PENDIENTE);
        Long enPreparacion = pedidoRepository.countByEstado(EstadoPedido.EN_PREPARACION);
        Long listos = pedidoRepository.countByEstado(EstadoPedido.LISTO);
        Long entregados = pedidoRepository.countByEstado(EstadoPedido.ENTREGADO);
        Long cancelados = pedidoRepository.countByEstado(EstadoPedido.CANCELADO);
        
        return new EstadisticasPedidos(pendientes, enPreparacion, listos, entregados, cancelados);
    }
    
    /**
     * Generar número de pedido único
     */
    private String generarNumeroPedido() {
        String fecha = LocalDateTime.now().format(FORMATTER);
        int numeroAleatorio = ThreadLocalRandom.current().nextInt(1000, 9999);
        String numeroPedido = "PED" + fecha + numeroAleatorio;
        
        // Verificar que no exista
        while (pedidoRepository.existsByNumeroPedido(numeroPedido)) {
            numeroAleatorio = ThreadLocalRandom.current().nextInt(1000, 9999);
            numeroPedido = "PED" + fecha + numeroAleatorio;
        }
        
        return numeroPedido;
    }
    
    /**
     * Validar detalle de pedido
     */
    private void validarDetallePedido(DetallePedido detalle) {
        Preconditions.checkNotNull(detalle, "El detalle no puede ser nulo");
        Preconditions.checkNotNull(detalle.getProducto(), "El producto es obligatorio");
        Preconditions.checkNotNull(detalle.getCantidad(), "La cantidad es obligatoria");
        Preconditions.checkArgument(detalle.getCantidad() > 0, "La cantidad debe ser mayor a 0");
        
        // Verificar que el producto existe y está disponible
        Optional<Producto> producto = productoRepository.findById(detalle.getProducto().getId());
        if (producto.isEmpty()) {
            throw new IllegalArgumentException("Producto no encontrado: " + detalle.getProducto().getId());
        }
        
        if (!producto.get().estaDisponible()) {
            throw new IllegalArgumentException("Producto no disponible: " + producto.get().getNombre());
        }
        
        // Establecer precio unitario actual del producto
        detalle.setPrecioUnitario(producto.get().getPrecio());
        detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
    }
    
    /**
     * Clase para estadísticas de pedidos
     */
    public static class EstadisticasPedidos {
        private final Long pendientes;
        private final Long enPreparacion;
        private final Long listos;
        private final Long entregados;
        private final Long cancelados;
        
        public EstadisticasPedidos(Long pendientes, Long enPreparacion, Long listos, 
                                 Long entregados, Long cancelados) {
            this.pendientes = pendientes;
            this.enPreparacion = enPreparacion;
            this.listos = listos;
            this.entregados = entregados;
            this.cancelados = cancelados;
        }
        
        // Getters
        public Long getPendientes() { return pendientes; }
        public Long getEnPreparacion() { return enPreparacion; }
        public Long getListos() { return listos; }
        public Long getEntregados() { return entregados; }
        public Long getCancelados() { return cancelados; }
        public Long getTotal() { return pendientes + enPreparacion + listos + entregados + cancelados; }
    }
}