package com.example.login.repository;

import com.example.login.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Buscar usuario por username (login)
    Optional<User> findByUsername(String username);

    
    // DESHABILITAR USUARIO
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.estado = 'Inactivo' WHERE u.idUsuario = :id")
    void deshabilitarUsuario(Integer id);


    // ACTUALIZAR USUARIO Y CONTRASEÑA
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.username = :username, u.password = :password WHERE u.idUsuario = :id")
    void actualizarUsuario(
            Integer id,
            String username,
            String password
    );
}