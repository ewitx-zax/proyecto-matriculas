package com.example.login.controller;

import com.example.login.model.User;
import com.example.login.repository.UserRepository;
import com.example.login.security.PasswordHasher;
import jakarta.servlet.http.HttpSession;
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

        // ✅ CORREGIDO: "admin" (coincide con LoginWebController y el HTML)
        if (!"admin".equalsIgnoreCase(user.getRol()))
            return ResponseEntity.status(403).build();

        return ResponseEntity.ok().build();
    }

    /**
     * Marca en sesión que el admin fue verificado.
     * POST /api/guardar-verificacion-admin
     */
    @PostMapping("/guardar-verificacion-admin")
    public ResponseEntity<?> guardarVerificacionAdmin(HttpSession session) {
        session.setAttribute("adminVerificado", true);
        return ResponseEntity.ok().build();
    }
}
