package com.example.login.repository;

import com.example.login.model.Bachillerato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BachilleratoRepository extends JpaRepository<Bachillerato, Integer> {
    
    List<Bachillerato> findByActivoTrue();
    
    List<Bachillerato> findByActivoTrueOrderByNombre();
}