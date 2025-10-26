import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/LoginAdmin.css';

// URL del endpoint de Login en tu Spring Boot
const LOGIN_URL = 'http://localhost:8080/api/admin/login'; 

const LoginAdmin = () => {
  const navigate = useNavigate();
  
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false); // Estado para manejar la carga

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      // ----------------------------------------------------
      // PASO CRÍTICO: LLAMADA REAL A LA API DE SPRING BOOT
      // ----------------------------------------------------
      const response = await fetch(LOGIN_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          // NO es necesario incluir 'Access-Control-Allow-Origin' aquí,
          // ya que el lado del servidor (Spring Boot) ya lo maneja con @CrossOrigin.
        },
        body: JSON.stringify({ // Enviamos los datos como JSON
          username: username,
          password: password,
        }),
      });

      // Verificamos si la respuesta está en el rango 200-299 (éxito)
      if (response.ok) {
        // La autenticación fue exitosa (código 200 OK de Spring Boot)
        
        // 1. Opcional: Extraer el token de la respuesta si lo tuvieras
        // const data = await response.json(); 
        // localStorage.setItem('adminToken', data.token);
        
        console.log('Login exitoso. Redirigiendo a Panel Admin...');

        // 2. Redirige al panel de administración
        navigate('/admin/panel');
      } else {
        // La autenticación falló (código 401 Unauthorized de Spring Boot)
        const errorData = await response.json();
        setError(errorData.message || 'Credenciales inválidas. Intente de nuevo.');
      }
    } catch (err) {
      // Este error ocurre si la red falla (e.g., Spring Boot no está corriendo)
      console.error('Error al conectar con el servidor:', err);
      setError('No se pudo conectar con el servidor de administración (¿Spring Boot está iniciado?).');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <h1 className="login-title">Acceso de Administrador</h1>
        
        {error && <p className="login-error">{error}</p>}

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="username">Usuario</label>
            <input
              type="text"
              id="username"
              className="login-input"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              disabled={loading}
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="password">Contraseña</label>
            <input
              type="password"
              id="password"
              className="login-input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              disabled={loading}
            />
          </div>
          
          <button type="submit" className="login-button" disabled={loading}>
            {loading ? 'Verificando...' : 'Iniciar Sesión'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default LoginAdmin;