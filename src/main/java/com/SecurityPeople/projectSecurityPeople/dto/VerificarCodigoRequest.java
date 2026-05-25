package com.SecurityPeople.projectSecurityPeople.dto;

import com.SecurityPeople.projectSecurityPeople.model.Usuario;

public class VerificarCodigoRequest {

    private String correo;
    private String codigo;
    private Usuario usuario;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}