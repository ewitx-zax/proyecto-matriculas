package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.repository.UserRepository;
import com.example.login.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdminVerificacionController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Verifica que el usuario sea administrador.
     * POST /api/verificar-admin
     * Body: { "username": "...", "password": "..." }
     *
     * Requiere: estado = "activo" Y rol = "administrador"
     */
    @PostMapping("/verificar-admin")
    public ResponseEntity<?> verificarAdmin(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null)
            return ResponseEntity.badRequest().build();

        User user = userRepository.findByUsername(username).orElse(null);

        // Usuario no existe
        if (user == null)
            return ResponseEntity.status(401).build();

        // Usuario deshabilitado
        if (!"activo".equalsIgnoreCase(user.getEstado()))
            return ResponseEntity.status(403).build();

        // Contraseña incorrecta
        if (!PasswordHasher.verifyPassword(password, user.getPassword()))
            return ResponseEntity.status(401).build();

        // No es administrador
        if (!"administrador".equalsIgnoreCase(user.getRol()))
            return ResponseEntity.status(403).build();

        return ResponseEntity.ok().build();
    }
}