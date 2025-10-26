import React, { useState } from 'react';
import { useCart } from '../context/CartContext';
import Header from '../components/Header';
import Footer from '../components/Footer';
import ExtrasModal from '../components/ExtrasModal';
import Toast from '../components/Toast';
import PaymentModal from '../components/PaymentModal';
import InvoiceModal from '../components/InvoiceModal';
import './CartPage.css';

const CartPage = () => {
  const { cartItems, updateQuantity, removeFromCart, updateComments, getCartTotal, addToCart, clearCart } = useCart();
  const [isExtrasModalOpen, setIsExtrasModalOpen] = useState(false);
  const [isPaymentModalOpen, setIsPaymentModalOpen] = useState(false);
  const [isInvoiceModalOpen, setIsInvoiceModalOpen] = useState(false);
  const [orderData, setOrderData] = useState(null);
  const [toast, setToast] = useState(null);

  const handleAddExtra = (extra) => {
    addToCart(extra);
    setToast({
      message: `✓ ${extra.nombre} agregado al carrito`,
      type: 'success'
    });
  };

  const handleCheckout = () => {
    if (cartItems.length === 0) return;
    setIsPaymentModalOpen(true);
  };

  const handlePaymentConfirm = (paymentMethod) => {
    const now = new Date();
    const invoiceNumber = `${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, '0')}${String(now.getDate()).padStart(2, '0')}-${String(now.getHours()).padStart(2, '0')}${String(now.getMinutes()).padStart(2, '0')}${String(now.getSeconds()).padStart(2, '0')}`;
    
    const order = {
      invoiceNumber,
      date: now.toLocaleDateString('es-PE'),
      time: now.toLocaleTimeString('es-PE', { hour: '2-digit', minute: '2-digit' }),
      paymentMethod,
      items: cartItems,
      total: getCartTotal()
    };

    // Guardar en historial
    const savedOrders = localStorage.getItem('orderHistory');
    const orders = savedOrders ? JSON.parse(savedOrders) : [];
    orders.unshift(order);
    localStorage.setItem('orderHistory', JSON.stringify(orders));

    setOrderData(order);
    setIsPaymentModalOpen(false);
    setIsInvoiceModalOpen(true);
    clearCart();
  };

  return (
    <div className="cart-page">
      <Header />
      
      {toast && (
        <Toast
          message={toast.message}
          type={toast.type}
          onClose={() => setToast(null)}
        />
      )}

      <ExtrasModal
        isOpen={isExtrasModalOpen}
        onClose={() => setIsExtrasModalOpen(false)}
        onAddExtra={handleAddExtra}
      />

      <PaymentModal
        isOpen={isPaymentModalOpen}
        onClose={() => setIsPaymentModalOpen(false)}
        onConfirm={handlePaymentConfirm}
        total={getCartTotal()}
      />

      <InvoiceModal
        isOpen={isInvoiceModalOpen}
        onClose={() => setIsInvoiceModalOpen(false)}
        orderData={orderData}
      />
      
      <div className="cart-container">
        <div className="container">
          <h1 className="cart-title">Mi carrito</h1>

          <div className="cart-content">
            <div className="cart-items-section">
              <div className="cart-table-header">
                <div className="header-product">Producto</div>
                <div className="header-price">Precio</div>
                <div className="header-subtotal">Subtotal</div>
              </div>

              {cartItems.length === 0 ? (
                <div className="empty-cart">
                  <i className="fas fa-shopping-cart"></i>
                  <h3>Tu carrito está vacío</h3>
                  <p>Agrega productos desde el menú</p>
                </div>
              ) : (
                cartItems.map((item) => (
                  <div key={item.id} className="cart-item-wrapper">
                    <div className="cart-item">
                      <div className="item-product">
                        <img src={item.imagen} alt={item.nombre} />
                        <span>{item.nombre}</span>
                      </div>

                      <div className="item-price">
                        <span>S/ {item.precio.toFixed(2)}</span>
                      </div>

                      <div className="item-quantity">
                        <button 
                          className="quantity-btn"
                          onClick={() => updateQuantity(item.id, item.cantidad - 1)}
                        >
                          −
                        </button>
                        <span className="quantity">{item.cantidad}</span>
                        <button 
                          className="quantity-btn"
                          onClick={() => updateQuantity(item.id, item.cantidad + 1)}
                        >
                          +
                        </button>
                      </div>

                      <div className="item-subtotal">
                        <span>S/ {(item.precio * item.cantidad).toFixed(2)}</span>
                      </div>

                      <div className="item-actions">
                        <button 
                          className="action-btn" 
                          onClick={() => removeFromCart(item.id)}
                          title="Eliminar"
                        >
                          <i className="fas fa-trash"></i>
                        </button>
                      </div>
                    </div>
                    
                    {/* Solo mostrar comentarios para platos principales, no para extras */}
                    {!item.id.toString().startsWith('extra-') && (
                      <div className="item-comments">
                        <i className="fas fa-comment-dots"></i>
                        <input
                          type="text"
                          placeholder="Comentarios (ej: sin cebolla, sin ají, etc.)"
                          value={item.comentarios || ''}
                          onChange={(e) => updateComments(item.id, e.target.value)}
                          className="comments-input"
                        />
                      </div>
                    )}
                  </div>
                ))
              )}

              {/* Botón de Agregar Extras */}
              {cartItems.length > 0 && (
                <div className="add-extras-section">
                  <h3 className="extras-title">
                    <i className="fas fa-plus-circle"></i> Extras y Adicionales
                  </h3>
                  <p className="extras-description">
                    ¿Quieres agregar bebidas, cremas o adicionales a tu pedido?
                  </p>
                  <button 
                    className="add-extras-btn"
                    onClick={() => setIsExtrasModalOpen(true)}
                  >
                    <i className="fas fa-plus"></i> Agregar extras
                  </button>
                </div>
              )}
            </div>

            <div className="cart-summary">
              <div className="summary-row">
                <span>Subtotal</span>
                <span>S/ {getCartTotal().toFixed(2)}</span>
              </div>
              
              <div className="summary-row total-row">
                <span>Total Pedido</span>
                <span>S/ {getCartTotal().toFixed(2)}</span>
              </div>

              <button 
                className="checkout-btn" 
                disabled={cartItems.length === 0}
                onClick={handleCheckout}
              >
                FINALIZAR COMPRA
              </button>
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default CartPage;