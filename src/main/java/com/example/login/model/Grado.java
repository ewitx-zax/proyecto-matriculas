package com.example.login.model;

import jakarta.persistence.*;

@Entity
@Table(name = "grado")
public class Grado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grado")
    private Integer idGrado;
    
    @ManyToOne
    @JoinColumn(name = "id_bachillerato", nullable = false)
    private Bachillerato bachillerato;
    
    @Column(nullable = false, length = 20)
    private String nombre;
    
    @Column(nullable = false)
    private Integer nivel;
    
    private Boolean activo = true;
    
    public Grado() {}
    
    public Grado(Bachillerato bachillerato, String nombre, Integer nivel) {
        this.bachillerato = bachillerato;
        this.nombre = nombre;
        this.nivel = nivel;
        this.activo = true;
    }
    
    // Getters y Setters
    public Integer getIdGrado() { return idGrado; }
    public void setIdGrado(Integer idGrado) { this.idGrado = idGrado; }
    
    public Bachillerato getBachillerato() { return bachillerato; }
    public void setBachillerato(Bachillerato bachillerato) { this.bachillerato = bachillerato; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}