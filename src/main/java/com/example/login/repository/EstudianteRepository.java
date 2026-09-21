package com.example.login.repository;

import com.example.login.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    // ===== BÚSQUEDAS BÁSICAS =====
    
    Optional<Estudiante> findByNie(String nie);
    
    List<Estudiante> findByEstadoPersona(String estadoPersona);
    
    List<Estudiante> findByActivoTrue();
    
    // ===== BÚSQUEDAS POR TEXTO =====
    
    List<Estudiante> findByNieContainingOrPrimerNombreContaining(String nie, String nombre);
    
    List<Estudiante> findByNieContainingOrPrimerApellidoContaining(String nie, String apellido);
    
    List<Estudiante> findByPrimerNombreContainingOrPrimerApellidoContaining(String nombre, String apellido);
    
    // ===== BÚSQUEDAS POR ACADÉMICO =====
    
    List<Estudiante> findByBachillerato_IdBachillerato(Integer idBachillerato);
    
    List<Estudiante> findByGrado_IdGrado(Integer idGrado);
    
    List<Estudiante> findBySeccion_IdSeccion(Integer idSeccion);
    
    List<Estudiante> findByBachillerato_IdBachilleratoAndGrado_IdGradoAndSeccion_IdSeccion(
            Integer idBachillerato, Integer idGrado, Integer idSeccion);
    
    // ===== BÚSQUEDAS CON QUERY PERSONALIZADA =====
    
    @Query("SELECT e FROM Estudiante e WHERE e.activo = true AND e.bachillerato IS NOT NULL AND e.grado IS NOT NULL AND e.seccion IS NOT NULL")
    List<Estudiante> findEstudiantesConMatriculaCompleta();
    
    @Query("SELECT e FROM Estudiante e WHERE e.activo = true AND (e.bachillerato IS NULL OR e.grado IS NULL OR e.seccion IS NULL)")
    List<Estudiante> findEstudiantesConMatriculaIncompleta();
    
    @Query("SELECT COUNT(e) FROM Estudiante e WHERE e.bachillerato.idBachillerato = :idBachillerato")
    long countByBachilleratoId(@Param("idBachillerato") Integer idBachillerato);
    
    @Query("SELECT COUNT(e) FROM Estudiante e WHERE e.grado.idGrado = :idGrado")
    long countByGradoId(@Param("idGrado") Integer idGrado);
    
    @Query("SELECT COUNT(e) FROM Estudiante e WHERE e.seccion.idSeccion = :idSeccion")
    long countBySeccionId(@Param("idSeccion") Integer idSeccion);
    
    // ===== BÚSQUEDAS CON JOIN =====
    
    @Query("SELECT e FROM Estudiante e JOIN FETCH e.bachillerato b JOIN FETCH e.grado g JOIN FETCH e.seccion s WHERE e.idEstudiante = :id")
    Optional<Estudiante> findByIdWithAcademicData(@Param("id") Integer id);
    
    @Query("SELECT e FROM Estudiante e LEFT JOIN FETCH e.bachillerato LEFT JOIN FETCH e.grado LEFT JOIN FETCH e.seccion")
    List<Estudiante> findAllWithAcademicData();
    
    // ===== CONTADORES =====
    
    long countByActivoTrue();
    
    long countByEstadoPersona(String estadoPersona);
    
    long countByBachilleratoIsNotNullAndGradoIsNotNullAndSeccionIsNotNull();
    
    // ===== BÚSQUEDA AVANZADA POR NOMBRE COMPLETO (USANDO CONCAT) =====
    
    @Query("SELECT e FROM Estudiante e WHERE " +
           "LOWER(CONCAT(e.primerNombre, ' ', e.primerApellido)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(CONCAT(e.primerNombre, ' ', e.segundoNombre, ' ', e.primerApellido)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.nie) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Estudiante> searchEstudiantes(@Param("search") String search);
}