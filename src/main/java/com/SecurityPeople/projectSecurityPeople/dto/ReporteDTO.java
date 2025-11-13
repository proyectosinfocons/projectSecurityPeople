package com.SecurityPeople.projectSecurityPeople.dto;

import java.time.LocalDateTime;

public class ReporteDTO {

    private Long id;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fechaRegistro;
    private Long usuarioId;
    private String tipo;
    private String urlArchivo; // URL para obtener imagen o video aparte ✅

    public ReporteDTO() {
    }

    public ReporteDTO(Long id, String descripcion, Double latitud,
                      Double longitud, LocalDateTime fechaRegistro,
                      Long usuarioId, String tipo, String urlArchivo) {
        this.id = id;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaRegistro = fechaRegistro;
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.urlArchivo = urlArchivo;
    }

    // ✅ Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }
}
