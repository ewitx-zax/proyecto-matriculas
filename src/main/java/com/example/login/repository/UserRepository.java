package com.example.login.repository;

import com.example.login.model.User;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author MINEDUCYT
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    // =========================
    // BUSCAR POR USERNAME
    // =========================
    Optional<User> findByUsername(String username);

    // =========================
    // DESHABILITAR USUARIO
    // =========================
    @Transactional
    @Modifying
    @Query("""
        UPDATE User u 
        SET u.estado = 'Inactivo' 
        WHERE u.id = :id
    """)
    void deshabilitarUsuario(Integer id);

}