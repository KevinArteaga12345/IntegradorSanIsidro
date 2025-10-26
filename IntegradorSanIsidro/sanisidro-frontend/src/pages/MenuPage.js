import React, { useState } from 'react';
import { useCart } from '../context/CartContext';
import Header from '../components/Header';
import Footer from '../components/Footer';
import Toast from '../components/Toast';
import './MenuPage.css';

const MenuPage = () => {
  const { addToCart } = useCart();
  const [toast, setToast] = useState(null);

  const menuItems = [
    {
      id: 1,
      nombre: 'Chicharrón de Pollo Clásico',
      descripcion: 'Crocante y jugoso, acompañado de papas fritas y salsa criolla.',
      precio: 25.90,
      imagen: '/images/menu/chicharron-clasico.jpg'
    },
    {
      id: 2,
      nombre: 'Chicharrón de Pollo al Ají',
      descripcion: 'Servido con papas doradas y una deliciosa crema de ají amarillo.',
      precio: 28.90,
      imagen: '/images/menu/chicharron-aji.jpg'
    },
    {
      id: 3,
      nombre: 'Chicharrón Mixto',
      descripcion: 'Pollo, cerdo y mariscos en una explosión de sabores.',
      precio: 32.90,
      imagen: '/images/menu/chicharron-mixto.jpg'
    },
    {
      id: 4,
      nombre: 'Chicharrón Pollo Salvaje',
      descripcion: 'Una mezcla de especias únicas y una salsa secreta.',
      precio: 29.90,
      imagen: '/images/menu/chicharron-salvaje.jpg'
    },
    {
      id: 5,
      nombre: 'Chicharrón Picante',
      descripcion: 'Con un toque picante y delicioso.',
      precio: 27.90,
      imagen: '/images/menu/chicharron-picante.jpg'
    },
    {
      id: 6,
      nombre: 'Lomo Saltado',
      descripcion: 'Clásico peruano con carne, papas fritas y arroz.',
      precio: 30.90,
      imagen: '/images/menu/lomo-saltado.jpg'
    }
  ];

  const handleAddToCart = (item) => {
    addToCart(item);
    setToast({
      message: `✓ ${item.nombre} agregado al carrito`,
      type: 'success'
    });
  };

  return (
    <div className="menu-page">
      <Header />
      
      {toast && (
        <Toast
          message={toast.message}
          type={toast.type}
          onClose={() => setToast(null)}
        />
      )}

      <div className="menu-container">
        <div className="container">
          <h1 className="menu-title">Carta de Comidas</h1>
          
          <div className="menu-grid">
            {menuItems.map((item) => (
              <div key={item.id} className="menu-card">
                <div className="card-left">
                  <h3 className="card-title">{item.nombre}</h3>
                  <p className="card-description">{item.descripcion}</p>
                  <div className="card-footer">
                    <span className="card-price">S/ {item.precio.toFixed(2)}</span>
                    <button 
                      className="add-to-cart-btn"
                      onClick={() => handleAddToCart(item)}
                    >
                      <i className="fas fa-shopping-cart"></i>
                      Añadir al carrito
                    </button>
                  </div>
                </div>
                <div className="card-right">
                  <img src={item.imagen} alt={item.nombre} />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default MenuPage;