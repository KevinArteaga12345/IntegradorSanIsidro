import React, { useState } from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import './ReservaPage.css';

const ReservaPage = () => {
  const [formData, setFormData] = useState({
    email: '',
    fecha: '',
    hora: '',
    nombre: '',
    personas: '',
    telefono: ''
  });
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    
    // Validación especial para teléfono: solo números y máximo 9 dígitos
    if (name === 'telefono') {
      const numericValue = value.replace(/\D/g, '');
      if (numericValue.length <= 9) {
        setFormData(prev => ({
          ...prev,
          [name]: numericValue
        }));
      }
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Validar que el teléfono tenga exactamente 9 dígitos
    if (formData.telefono.length !== 9) {
      alert('El teléfono debe tener exactamente 9 dígitos');
      return;
    }
    
    console.log('Reserva enviada:', formData);
    setShowSuccessModal(true);
    
    // Limpiar formulario después de 3 segundos
    setTimeout(() => {
      setFormData({
        email: '',
        fecha: '',
        hora: '',
        nombre: '',
        personas: '',
        telefono: ''
      });
    }, 3000);
  };

  const closeModal = () => {
    setShowSuccessModal(false);
  };

  return (
    <div className="reserva-page">
      <Header />
      
      <div className="reserva-container">
        <div className="container">
          <div className="reserva-content">
            <h1 className="reserva-title fade-in">Formulario de Reserva</h1>
            
            <form onSubmit={handleSubmit} className="reserva-form slide-in-left">
              <div className="form-group">
                <label htmlFor="email">Correo electronico</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  className="form-input"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="fecha">Fecha</label>
                <input
                  type="date"
                  id="fecha"
                  name="fecha"
                  value={formData.fecha}
                  onChange={handleChange}
                  className="form-input"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="hora">Hora</label>
                <input
                  type="time"
                  id="hora"
                  name="hora"
                  value={formData.hora}
                  onChange={handleChange}
                  className="form-input"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="nombre">Nombre</label>
                <input
                  type="text"
                  id="nombre"
                  name="nombre"
                  value={formData.nombre}
                  onChange={handleChange}
                  className="form-input"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="personas">Numero de personas</label>
                <input
                  type="number"
                  id="personas"
                  name="personas"
                  value={formData.personas}
                  onChange={handleChange}
                  className="form-input"
                  min="1"
                  max="20"
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="telefono">Telefono (9 dígitos)</label>
                <input
                  type="tel"
                  id="telefono"
                  name="telefono"
                  value={formData.telefono}
                  onChange={handleChange}
                  className="form-input"
                  placeholder="987654321"
                  maxLength="9"
                  required
                />
              </div>

              <button type="submit" className="reservar-btn pulse">
                Reservar
              </button>
            </form>
          </div>
        </div>
      </div>

      {/* Modal de éxito */}
      {showSuccessModal && (
        <div className="success-modal-overlay" onClick={closeModal}>
          <div className="success-modal" onClick={(e) => e.stopPropagation()}>
            <div className="success-icon">
              <svg viewBox="0 0 52 52" className="checkmark">
                <circle className="checkmark-circle" cx="26" cy="26" r="25" fill="none"/>
                <path className="checkmark-check" fill="none" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
              </svg>
            </div>
            <h2 className="success-title">¡Reserva Exitosa!</h2>
            <p className="success-message">
              Tu reserva ha sido confirmada exitosamente.<br/>
              <strong>{formData.nombre}</strong>, te esperamos el <strong>{new Date(formData.fecha).toLocaleDateString('es-ES', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}</strong> a las <strong>{formData.hora}</strong>.
            </p>
            <p className="success-submessage">
              Hemos enviado un correo de confirmación a <strong>{formData.email}</strong>
            </p>
            <button className="success-btn" onClick={closeModal}>
              Entendido
            </button>
          </div>
        </div>
      )}

      <Footer />
    </div>
  );
};

export default ReservaPage;