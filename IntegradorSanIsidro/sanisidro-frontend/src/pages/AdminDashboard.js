import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './AdminDashboard.css';

const AdminDashboard = () => {
  const [adminUser, setAdminUser] = useState(null);
  const navigate = useNavigate();

  // Datos simulados
  const [pedidos, setPedidos] = useState([
    {
      id: 1,
      numeroPedido: 'PED20241022001',
      cliente: 'Juan Pérez',
      productos: 'Chicharrón de Pollo x2',
      mesa: 5,
      hora: '12:30',
      estado: 'pendiente',
      total: 179.80
    },
    {
      id: 2,
      numeroPedido: 'PED20241022002',
      cliente: 'María García',
      productos: 'Lomo Saltado x1, Ají de Gallina x1',
      mesa: 3,
      hora: '13:15',
      estado: 'en_preparacion',
      total: 61.80
    },
    {
      id: 3,
      numeroPedido: 'PED20241022003',
      cliente: 'Carlos López',
      productos: 'Chicharrón Mixto x1',
      mesa: 8,
      hora: '13:45',
      estado: 'listo',
      total: 95.90
    }
  ]);

  const [reservas, setReservas] = useState([
    {
      id: 1,
      nombre: 'Ana Rodríguez',
      personas: 4,
      fecha: '2024-10-22',
      hora: '19:00',
      estado: 'confirmada',
      mesa: 12
    },
    {
      id: 2,
      nombre: 'Pedro Martínez',
      personas: 2,
      fecha: '2024-10-22',
      hora: '20:30',
      estado: 'pendiente',
      mesa: null
    },
    {
      id: 3,
      nombre: 'Lucía Fernández',
      personas: 6,
      fecha: '2024-10-23',
      hora: '18:00',
      estado: 'confirmada',
      mesa: 15
    }
  ]);

  useEffect(() => {
    const token = localStorage.getItem('adminToken');
    const user = localStorage.getItem('adminUser');
    
    if (!token || !user) {
      navigate('/admin/login');
      return;
    }
    
    setAdminUser(JSON.parse(user));
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem('adminToken');
    localStorage.removeItem('adminUser');
    navigate('/');
  };

  const updatePedidoEstado = (id, nuevoEstado) => {
    setPedidos(pedidos.map(pedido =>
      pedido.id === id ? { ...pedido, estado: nuevoEstado } : pedido
    ));
  };

  const updateReservaEstado = (id, nuevoEstado) => {
    setReservas(reservas.map(reserva =>
      reserva.id === id ? { ...reserva, estado: nuevoEstado } : reserva
    ));
  };

  const getEstadoColor = (estado) => {
    const colores = {
      pendiente: '#f39c12',
      en_preparacion: '#3498db',
      listo: '#27ae60',
      entregado: '#95a5a6',
      confirmada: '#27ae60',
      ocupada: '#e74c3c',
      completada: '#95a5a6',
      cancelada: '#e74c3c'
    };
    return colores[estado] || '#6c757d';
  };

  const getEstadoTexto = (estado) => {
    const textos = {
      pendiente: 'Pendiente',
      en_preparacion: 'En Preparación',
      listo: 'Listo',
      entregado: 'Entregado',
      confirmada: 'Confirmada',
      ocupada: 'Ocupada',
      completada: 'Completada',
      cancelada: 'Cancelada'
    };
    return textos[estado] || estado;
  };

  if (!adminUser) {
    return (
      <div className="loading-screen">
        <div className="loading-spinner"></div>
        <p>Cargando dashboard...</p>
      </div>
    );
  }

  return (
    <div className="admin-dashboard">
      <div className="dashboard-header">
        <div className="logo-section">
          <h1>San Isidro</h1>
          <span>Restaurant</span>
        </div>
        <button className="logout-btn" onClick={handleLogout}>
          <i className="fas fa-sign-out-alt"></i>
        </button>
      </div>

      <div className="dashboard-content">
        <div className="section">
          <h2>GESTIONAR PEDIDOS</h2>
          <table className="data-table">
            <thead>
              <tr>
                <th>Número de pedidos</th>
                <th>Pedidos</th>
                <th>Número de mesa</th>
                <th>Hora de pedido</th>
                <th>estado (entregado)</th>
              </tr>
            </thead>
            <tbody>
              {pedidos.map((pedido) => (
                <tr key={pedido.id}>
                  <td>{pedido.numeroPedido}</td>
                  <td>{pedido.productos}</td>
                  <td>{pedido.mesa}</td>
                  <td>{pedido.hora}</td>
                  <td>
                    <select 
                      value={pedido.estado}
                      onChange={(e) => updatePedidoEstado(pedido.id, e.target.value)}
                    >
                      <option value="pendiente">Pendiente</option>
                      <option value="en_preparacion">En Preparación</option>
                      <option value="listo">Listo</option>
                      <option value="entregado">Entregado</option>
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="section">
          <h2>RESERVAS DE MESA</h2>
          <table className="data-table">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Cantidad de personas</th>
                <th>Hora fecha</th>
                <th>estado (ocupado o libre)</th>
              </tr>
            </thead>
            <tbody>
              {reservas.map((reserva) => (
                <tr key={reserva.id}>
                  <td>{reserva.nombre}</td>
                  <td>{reserva.personas}</td>
                  <td>{reserva.fecha} {reserva.hora}</td>
                  <td>
                    <select 
                      value={reserva.estado}
                      onChange={(e) => updateReservaEstado(reserva.id, e.target.value)}
                    >
                      <option value="libre">Libre</option>
                      <option value="ocupada">Ocupada</option>
                      <option value="confirmada">Confirmada</option>
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;