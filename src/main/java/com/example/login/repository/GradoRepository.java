package com.example.login.repository;

import com.example.login.model.Grado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GradoRepository extends JpaRepository<Grado, Integer> {

    // Grados de un bachillerato activos
    List<Grado> findByBachillerato_IdBachilleratoAndActivoTrue(Integer idBachillerato);

    // Todos los grados activos
    List<Grado> findByActivoTrue();

    // Grados de un bachillerato ordenados por nivel
    List<Grado> findByBachillerato_IdBachilleratoOrderByNivel(Integer idBachillerato);

    // Grados de un bachillerato (sin filtro de activo)
    List<Grado> findByBachillerato_IdBachillerato(Integer idBachillerato);
}