package com.SecurityPeople.projectSecurityPeople.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "contactos_emergencia")
public class ContactoEmergencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido;

    private String relacion;

    private String telefono;
    private String correo;

    // 🔥 Relación MANY TO ONE (muchos contactos pertenecen a un usuario)
    @ManyToOne
    @JoinColumn(name = "usuario_id") // FK en la BD
    @JsonIgnore // evita bucle infinito en JSON
    private Usuario usuario;

    public ContactoEmergencia() {}

    public ContactoEmergencia(Long id, String nombre, String apellido, String relacion, String telefono, String correo, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.relacion = relacion;
        this.telefono = telefono;
        this.correo = correo;
        this.usuario = usuario;
    }

    public String getRelacion() {
        return relacion;
    }

    public void setRelacion(String relacion) {
        this.relacion = relacion;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    // ================= GETTERS Y SETTERS =================

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }

    public void setCorreo(String correo) { this.correo = correo; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}