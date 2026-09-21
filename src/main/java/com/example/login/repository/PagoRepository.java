package com.example.login.repository;

import com.example.login.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    // Todos los pagos de un mes/año
    List<Pago> findByMesAndAnio(Integer mes, Integer anio);

    // Pago específico de un estudiante en un mes/año
    Optional<Pago> findByEstudiante_IdEstudianteAndMesAndAnio(
            Integer idEstudiante, Integer mes, Integer anio);

    // Pagos de un estudiante
    List<Pago> findByEstudiante_IdEstudiante(Integer idEstudiante);

    // Pendientes de un mes/año
    List<Pago> findByMesAndAnioAndEstado(Integer mes, Integer anio, String estado);

    // Total recaudado en un mes/año
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p " +
           "WHERE p.mes = :mes AND p.anio = :anio AND p.estado = 'pagado'")
    Double totalRecaudado(@Param("mes") Integer mes, @Param("anio") Integer anio);
}