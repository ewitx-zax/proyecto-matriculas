package com.example.login.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora")
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Integer idBitacora;

    // USUARIO QUE HIZO LA ACCIÓN
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User usuario;

    // USUARIO AFECTADO
    @Column(name = "id_usuarioafectado")
    private Integer idUsuarioAfectado;

    // ACCIÓN
    private String accion;

    // DESCRIPCIÓN
    private String descripcion;

    // FECHA
    private LocalDateTime fecha;

    public Bitacora() {
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public Integer getIdBitacora() {
        return idBitacora;
    }

    public void setIdBitacora(Integer idBitacora) {
        this.idBitacora = idBitacora;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Integer getIdUsuarioAfectado() {
        return idUsuarioAfectado;
    }

    public void setIdUsuarioAfectado(Integer idUsuarioAfectado) {
        this.idUsuarioAfectado = idUsuarioAfectado;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}