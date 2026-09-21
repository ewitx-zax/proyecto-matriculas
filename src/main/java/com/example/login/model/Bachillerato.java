package com.example.login.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "bachillerato")
public class Bachillerato {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bachillerato")
    private Integer idBachillerato;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    private Boolean activo = true;
    
    @OneToMany(mappedBy = "bachillerato", cascade = CascadeType.ALL)
    private List<Grado> grados;
    
    public Bachillerato() {}
    
    public Bachillerato(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = true;
    }
    
    // Getters y Setters
    public Integer getIdBachillerato() { return idBachillerato; }
    public void setIdBachillerato(Integer idBachillerato) { this.idBachillerato = idBachillerato; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public List<Grado> getGrados() { return grados; }
    public void setGrados(List<Grado> grados) { this.grados = grados; }
}