package com.example.login.controller;

import com.example.login.model.Estudiante;
import com.example.login.model.LoginModel;
import com.example.login.model.User;
import com.example.login.repository.EstudianteRepository;
import com.example.login.repository.UserRepository;
import com.example.login.security.PasswordHasher;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginWebController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    // =========================
    // LOGIN
    // =========================
    @GetMapping("/")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginModel", new LoginModel());
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute LoginModel loginModel,
            Model model) {

        User user = userRepository
                .findByUsername(loginModel.getUsername())
                .orElse(null);

        if (user != null) {

            // validar estado activo
            if (!"Activo".equalsIgnoreCase(user.getEstado())) {
                model.addAttribute(
                        "error",
                        "Usuario deshabilitado"
                );
                return "login";
            }

            // validar contraseña
            if (PasswordHasher.verifyPassword(
                    loginModel.getPassword(),
                    user.getPassword())) {

                model.addAttribute(
                        "mensaje",
                        "Bienvenido " + user.getUsername()
                );

                return "dashboard";
            }
        }

        model.addAttribute(
                "error",
                "Usuario o contraseña incorrectos"
        );

        return "login";
    }

    // =========================
    // DASHBOARD
    // =========================
    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        model.addAttribute(
                "mensaje",
                "Panel principal del sistema de matrículas"
        );
        return "dashboard";
    }

    // =========================
    // CONFIGURACIÓN
    // =========================
    @GetMapping("/configuracion")
    public String mostrarConfiguracion(Model model) {

        List<User> usuarios = userRepository.findAll();

        model.addAttribute("usuarios", usuarios);

        return "configuracion";
    }

    // =========================
    // AGREGAR NUEVO USUARIO
    // =========================
    @PostMapping("/guardar-usuario")
    public String guardarUsuario(
            @RequestParam String username,
            @RequestParam String password) {

        User nuevoUsuario = new User();

        nuevoUsuario.setUsername(username);

        // encriptar contraseña
        nuevoUsuario.setPassword(
                PasswordHasher.hashPassword(password)
        );

        // fecha actual
        nuevoUsuario.setIngreso(LocalDateTime.now());

        // estado activo
        nuevoUsuario.setEstado("Activo");

        userRepository.save(nuevoUsuario);

        return "redirect:/configuracion";
    }

    // =========================
    // DESHABILITAR USUARIO
    // =========================
    @GetMapping("/deshabilitar/{id}")
    public String deshabilitarUsuario(
            @PathVariable Integer id) {

        userRepository.deshabilitarUsuario(id);

        return "redirect:/configuracion";
    }

    // =========================
    // ACTUALIZAR USUARIO
    // =========================
    @PostMapping("/actualizar-usuario")
    public String actualizarUsuario(
            @RequestParam Integer id,
            @RequestParam String username,
            @RequestParam String password) {

        User usuario = userRepository.findById(id).orElse(null);

        if (usuario != null) {
            usuario.setUsername(username);

            usuario.setPassword(
                    PasswordHasher.hashPassword(password)
            );

            userRepository.save(usuario);
        }

        return "redirect:/configuracion";
    }

    // =========================
    // REGISTRAR ESTUDIANTE
    // =========================
    @GetMapping("/registrar-estudiante")
    public String mostrarRegistroEstudiante(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "registrar-estudiante";
    }

    @PostMapping("/guardar-estudiante")
    public String guardarEstudiante(
            @ModelAttribute Estudiante estudiante) {

        estudianteRepository.save(estudiante);

        return "redirect:/gestion-matricula";
    }

    // =========================
    // GESTIÓN MATRÍCULA
    // =========================
    @GetMapping("/gestion-matricula")
    public String mostrarGestionMatricula(Model model) {

        List<Estudiante> estudiantes =
                estudianteRepository.findAll();

        model.addAttribute(
                "estudiantes",
                estudiantes
        );

        return "gestion-matricula";
    }
}