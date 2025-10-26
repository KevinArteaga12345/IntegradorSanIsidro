import React from 'react';
import './ExtrasModal.css';

const ExtrasModal = ({ isOpen, onClose, onAddExtra }) => {
  if (!isOpen) return null;

  const extras = {
    bebidas: [
      { id: 'extra-1', nombre: 'Coca Cola personal', precio: 4.00, imagen: '/images/menu/coca-cola.jpg' },
      { id: 'extra-2', nombre: 'Inka Kola personal', precio: 4.00, imagen: '/images/menu/inka-kola.jpg' }
    ],
    adicionales: [
      { id: 'extra-3', nombre: 'Papas adicionales', precio: 3.00, imagen: '/images/menu/papas.jpg' },
      { id: 'extra-4', nombre: 'Ensalada adicional', precio: 3.00, imagen: '/images/menu/ensalada.jpg' }
    ],
    cremas: [
      { id: 'extra-5', nombre: 'Mayonesa', precio: 1.00, imagen: '/images/menu/mayonesa.jpg' },
      { id: 'extra-6', nombre: 'Ají', precio: 1.00, imagen: '/images/menu/aji.jpg' },
      { id: 'extra-7', nombre: 'Ketchup', precio: 1.00, imagen: '/images/menu/ketchup.jpg' },
      { id: 'extra-8', nombre: 'Mostaza', precio: 1.00, imagen: '/images/menu/mostaza.jpg' },
      { id: 'extra-9', nombre: 'Tártara', precio: 1.00, imagen: '/images/menu/tartara.jpg' }
    ]
  };

  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  return (
    <div className="modal-overlay" onClick={handleOverlayClick}>
      <div className="modal-content">
        <div className="modal-header">
          <h2>Extras y Adicionales</h2>
          <button className="modal-close" onClick={onClose}>
            <i className="fas fa-times"></i>
          </button>
        </div>

        <div className="modal-body">
          {/* Bebidas */}
          <div className="extras-category">
            <h3 className="category-title">
              <i className="fas fa-glass-whiskey"></i> Bebidas
            </h3>
            <div className="extras-list">
              {extras.bebidas.map((extra) => (
                <div key={extra.id} className="extra-item">
                  <div className="extra-item-image">
                    <img src={extra.imagen} alt={extra.nombre} />
                  </div>
                  <div className="extra-item-info">
                    <div className="extra-item-name">{extra.nombre}</div>
                    <div className="extra-item-footer">
                      <span className="extra-item-price">S/ {extra.precio.toFixed(2)}</span>
                      <button 
                        className="extra-add-button"
                        onClick={() => onAddExtra(extra)}
                      >
                        <i className="fas fa-plus"></i>
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Adicionales */}
          <div className="extras-category">
            <h3 className="category-title">
              <i className="fas fa-utensils"></i> Adicionales
            </h3>
            <div className="extras-list">
              {extras.adicionales.map((extra) => (
                <div key={extra.id} className="extra-item">
                  <div className="extra-item-image">
                    <img src={extra.imagen} alt={extra.nombre} />
                  </div>
                  <div className="extra-item-info">
                    <div className="extra-item-name">{extra.nombre}</div>
                    <div className="extra-item-footer">
                      <span className="extra-item-price">S/ {extra.precio.toFixed(2)}</span>
                      <button 
                        className="extra-add-button"
                        onClick={() => onAddExtra(extra)}
                      >
                        <i className="fas fa-plus"></i>
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Cremas */}
          <div className="extras-category">
            <h3 className="category-title">
              <i className="fas fa-pepper-hot"></i> Cremas
            </h3>
            <div className="extras-list">
              {extras.cremas.map((extra) => (
                <div key={extra.id} className="extra-item">
                  <div className="extra-item-image">
                    <img src={extra.imagen} alt={extra.nombre} />
                  </div>
                  <div className="extra-item-info">
                    <div className="extra-item-name">{extra.nombre}</div>
                    <div className="extra-item-footer">
                      <span className="extra-item-price">S/ {extra.precio.toFixed(2)}</span>
                      <button 
                        className="extra-add-button"
                        onClick={() => onAddExtra(extra)}
                      >
                        <i className="fas fa-plus"></i>
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="modal-footer">
          <button className="modal-done-btn" onClick={onClose}>
            <i className="fas fa-check"></i> Listo
          </button>
        </div>
      </div>
    </div>
  );
};

export default ExtrasModal;
