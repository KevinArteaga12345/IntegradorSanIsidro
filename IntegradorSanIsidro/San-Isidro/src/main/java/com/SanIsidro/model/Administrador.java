package com.sanisidro.model;
package com.SanIsidro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad que representa a los administradores del sistema.
 * Esta clase se vincula con la tabla "administradores" de MySQL.
 */
@Entity
@Table(name = "administradores")
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único

    private String username;
    private String password;

    // 🔹 (Opcional) Puedes agregar más campos si decides crear una tabla extendida (AdminInfo)
    // private String nombre;
    // private String dni;
    // private String correo;

    // ----- Constructores -----
    public Administrador() {
    }

    public Administrador(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ----- Getters y Setters -----
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ----- toString (para depuración) -----
    @Override
    public String toString() {
        return "Administrador{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
