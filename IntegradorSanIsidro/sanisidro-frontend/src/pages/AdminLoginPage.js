import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import './AdminLoginPage.css';

const AdminLoginPage = () => {
  const [formData, setFormData] = useState({
    usuario: '',
    contraseña: ''
  });
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (formData.usuario === 'admin' && formData.contraseña === 'admin123') {
      localStorage.setItem('adminToken', 'fake-jwt-token');
      navigate('/admin/dashboard');
    } else {
      alert('Credenciales incorrectas');
    }
  };

  return (
    <div className="admin-login-page">
      <Header />
      
      <div className="login-background">
        <div className="login-content">
          <div className="login-card">
            <h1>INICIA SESIÓN COMO ADMINISTRADOR</h1>
            
            <form onSubmit={handleSubmit} className="login-form">
              <div className="form-group">
                <label>USUARIO:</label>
                <input
                  type="text"
                  name="usuario"
                  value={formData.usuario}
                  onChange={handleChange}
                  className="form-input"
                />
              </div>

              <div className="form-group">
                <label>CONTRASEÑA:</label>
                <input
                  type="password"
                  name="contraseña"
                  value={formData.contraseña}
                  onChange={handleChange}
                  className="form-input"
                />
              </div>

              <button type="submit" className="login-btn">
                INICIAR SESIÓN
              </button>
            </form>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default AdminLoginPage;