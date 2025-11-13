    package com.SecurityPeople.projectSecurityPeople.model;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import jakarta.persistence.*;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "reporte", schema = "public")
    public class Reporte {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(length = 255)
        private String descripcion;

        private Double latitud;
        private Double longitud;

        @Lob
        @Basic(fetch = FetchType.LAZY)
        @Column(name = "archivo")
        private byte[] archivo;

        private String tipo; // Nuevo campo (imagen o video)

        @Column(name = "fecha_registro")
        private LocalDateTime fechaRegistro = LocalDateTime.now();

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "usuario_id")
        @JsonIgnoreProperties({"reportes"}) // 👈 evita recursión infinita
        private Usuario usuario;

        public Reporte() {
        }

        public Reporte(Long id, String descripcion, Double latitud, Double longitud, byte[] archivo, LocalDateTime fechaRegistro, String tipo, Usuario usuario) {
            this.id = id;
            this.descripcion = descripcion;
            this.latitud = latitud;
            this.longitud = longitud;
            this.archivo = archivo;
            this.fechaRegistro = fechaRegistro;
            this.tipo=tipo;
            this.usuario = usuario;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        // Getters y setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public Double getLatitud() {
            return latitud;
        }

        public void setLatitud(Double latitud) {
            this.latitud = latitud;
        }

        public Double getLongitud() {
            return longitud;
        }

        public void setLongitud(Double longitud) {
            this.longitud = longitud;
        }

        public byte[] getArchivo() {
            return archivo;
        }

        public void setArchivo(byte[] archivo) {
            this.archivo = archivo;
        }

        public LocalDateTime getFechaRegistro() {
            return fechaRegistro;
        }

        public void setFechaRegistro(LocalDateTime fechaRegistro) {
            this.fechaRegistro = fechaRegistro;
        }
    }
