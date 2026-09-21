package com.example.login.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "estudiante")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Integer idEstudiante;

    @Column(unique = true, nullable = false)
    private String nie;

    private String nui;
    private String dui;

    @Column(name = "primer_nombre")
    private String primerNombre;

    @Column(name = "segundo_nombre")
    private String segundoNombre;

    @Column(name = "tercer_nombre")
    private String tercerNombre;

    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    @Column(name = "tercer_apellido")
    private String tercerApellido;

    @Column(name = "nombre_partida")
    private String nombrePartida;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String nacionalidad;

    @Column(name = "departamento_nacimiento")
    private String departamentoNacimiento;

    @Column(name = "municipio_nacimiento")
    private String municipioNacimiento;

    private String sexo;

    @Column(name = "estado_familiar")
    private String estadoFamiliar;

    private String etnia;

    private Boolean discapacidad;

    @Column(name = "trastorno_aprendizaje")
    private Boolean trastornoAprendizaje;

    private Boolean embarazada;

    @Column(name = "fecha_probable_parto")
    private LocalDate fechaProbableParto;

    @Column(name = "estado_persona")
    private String estadoPersona;

    private String correo;

    @Column(name = "id_direccion")
    private Integer idDireccion;

    @ManyToOne
    @JoinColumn(name = "id_bachillerato")
    private Bachillerato bachillerato;

    @ManyToOne
    @JoinColumn(name = "id_grado")
    private Grado grado;

    @ManyToOne
    @JoinColumn(name = "id_seccion")
    private Seccion seccion;

    @Column(name = "anio_lectivo")
    private Integer anioLectivo;

    @Column(name = "fecha_inscripcion")
    private LocalDate fechaInscripcion;

    private Boolean activo = true;

    public Estudiante() {
        this.fechaInscripcion = LocalDate.now();
        this.activo = true;
    }

    // ===== GETTERS Y SETTERS BÁSICOS =====

    public Integer getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Integer idEstudiante) { this.idEstudiante = idEstudiante; }

    public String getNie() { return nie; }
    public void setNie(String nie) { this.nie = nie; }

    public String getNui() { return nui; }
    public void setNui(String nui) { this.nui = nui; }

    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }

    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }

    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }

    public String getTercerNombre() { return tercerNombre; }
    public void setTercerNombre(String tercerNombre) { this.tercerNombre = tercerNombre; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getTercerApellido() { return tercerApellido; }
    public void setTercerApellido(String tercerApellido) { this.tercerApellido = tercerApellido; }

    public String getNombrePartida() { return nombrePartida; }
    public void setNombrePartida(String nombrePartida) { this.nombrePartida = nombrePartida; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public String getDepartamentoNacimiento() { return departamentoNacimiento; }
    public void setDepartamentoNacimiento(String v) { this.departamentoNacimiento = v; }

    public String getMunicipioNacimiento() { return municipioNacimiento; }
    public void setMunicipioNacimiento(String v) { this.municipioNacimiento = v; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getEstadoFamiliar() { return estadoFamiliar; }
    public void setEstadoFamiliar(String estadoFamiliar) { this.estadoFamiliar = estadoFamiliar; }

    public String getEtnia() { return etnia; }
    public void setEtnia(String etnia) { this.etnia = etnia; }

    public Boolean getDiscapacidad() { return discapacidad; }
    public void setDiscapacidad(Boolean discapacidad) { this.discapacidad = discapacidad; }

    public Boolean getTrastornoAprendizaje() { return trastornoAprendizaje; }
    public void setTrastornoAprendizaje(Boolean v) { this.trastornoAprendizaje = v; }

    public Boolean getEmbarazada() { return embarazada; }
    public void setEmbarazada(Boolean embarazada) { this.embarazada = embarazada; }

    public LocalDate getFechaProbableParto() { return fechaProbableParto; }
    public void setFechaProbableParto(LocalDate v) { this.fechaProbableParto = v; }

    public String getEstadoPersona() { return estadoPersona; }
    public void setEstadoPersona(String estadoPersona) { this.estadoPersona = estadoPersona; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Integer getIdDireccion() { return idDireccion; }
    public void setIdDireccion(Integer idDireccion) { this.idDireccion = idDireccion; }

    public Bachillerato getBachillerato() { return bachillerato; }
    public void setBachillerato(Bachillerato bachillerato) { this.bachillerato = bachillerato; }

    public Grado getGrado() { return grado; }
    public void setGrado(Grado grado) { this.grado = grado; }

    public Seccion getSeccion() { return seccion; }
    public void setSeccion(Seccion seccion) { this.seccion = seccion; }

    public Integer getAnioLectivo() { return anioLectivo; }
    public void setAnioLectivo(Integer anioLectivo) { this.anioLectivo = anioLectivo; }

    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    // ===== GETTERS/SETTERS DE CONVENIENCIA PARA THYMELEAF =====
    // Permiten usar th:field="*{idBachillerato}" en el HTML

    public Integer getIdBachillerato() {
        return bachillerato != null ? bachillerato.getIdBachillerato() : null;
    }

    public void setIdBachillerato(Integer idBachillerato) {
        if (idBachillerato != null) {
            Bachillerato b = new Bachillerato();
            b.setIdBachillerato(idBachillerato);
            this.bachillerato = b;
        } else {
            this.bachillerato = null;
        }
    }

    public Integer getIdGrado() {
        return grado != null ? grado.getIdGrado() : null;
    }

    public void setIdGrado(Integer idGrado) {
        if (idGrado != null) {
            Grado g = new Grado();
            g.setIdGrado(idGrado);
            this.grado = g;
        } else {
            this.grado = null;
        }
    }

    public Integer getIdSeccion() {
        return seccion != null ? seccion.getIdSeccion() : null;
    }

    public void setIdSeccion(Integer idSeccion) {
        if (idSeccion != null) {
            Seccion s = new Seccion();
            s.setIdSeccion(idSeccion);
            this.seccion = s;
        } else {
            this.seccion = null;
        }
    }

    // ===== NOMBRE COMPLETO =====
    public String getNombre() {
        return getNombreCompleto();
    }

    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();
        if (primerNombre   != null && !primerNombre.isEmpty())   sb.append(primerNombre).append(" ");
        if (segundoNombre  != null && !segundoNombre.isEmpty())  sb.append(segundoNombre).append(" ");
        if (tercerNombre   != null && !tercerNombre.isEmpty())   sb.append(tercerNombre).append(" ");
        if (primerApellido != null && !primerApellido.isEmpty()) sb.append(primerApellido).append(" ");
        if (segundoApellido!= null && !segundoApellido.isEmpty())sb.append(segundoApellido).append(" ");
        if (tercerApellido != null && !tercerApellido.isEmpty()) sb.append(tercerApellido);
        return sb.toString().trim();
    }
}