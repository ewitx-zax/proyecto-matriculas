package com.example.login.controller;

import com.example.login.model.Bitacora;
import com.example.login.model.Estudiante;
import com.example.login.model.LoginModel;
import com.example.login.model.User;

import com.example.login.repository.BitacoraRepository;
import com.example.login.repository.EstudianteRepository;
import com.example.login.repository.UserRepository;

import com.example.login.security.PasswordHasher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginWebController {

    // =========================
    // REPOSITORIES
    // =========================
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public EstudianteRepository estudianteRepository;

    @Autowired
    public BitacoraRepository bitacoraRepository;

    // =========================
    // MÉTODO BITÁCORA
    // =========================
    private void registrarBitacora(
            User usuario,
            String accion) {

        if (usuario == null) {
            return;
        }

        Bitacora bitacora = new Bitacora();

        bitacora.setUsuario(usuario);

        bitacora.setAccion(accion);

        bitacora.setFecha(LocalDateTime.now());

        bitacoraRepository.save(bitacora);
    }

    // =========================
    // LOGIN
    // =========================
    @GetMapping("/")
    public String mostrarLogin(Model model) {

        if (model != null) {
            model.addAttribute(
                    "loginModel",
                    new LoginModel()
            );
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute LoginModel loginModel,
            Model model) {

        if (loginModel == null) {

            if (model != null) {
                model.addAttribute(
                        "error",
                        "Datos inválidos"
                );
            }

            return "login";
        }

        Optional<User> optionalUser =
                userRepository.findByUsername(
                        loginModel.getUsername()
                );

        User user = optionalUser.orElse(null);

        if (user != null) {

            // VALIDAR ESTADO
            if (!"Activo".equalsIgnoreCase(
                    user.getEstado())) {

                if (model != null) {
                    model.addAttribute(
                            "error",
                            "Usuario deshabilitado"
                    );
                }

                return "login";
            }

            // VALIDAR CONTRASEÑA
            if (PasswordHasher.verifyPassword(
                    loginModel.getPassword(),
                    user.getPassword())) {

                user.setIngreso(LocalDateTime.now());

                userRepository.save(user);

                // BITÁCORA LOGIN
                registrarBitacora(
                        user,
                        "Inicio de sesión"
                );

                if (model != null) {
                    model.addAttribute(
                            "mensaje",
                            "Bienvenido "
                            + user.getUsername()
                    );
                }

                return "dashboard";
            }
        }

        if (model != null) {
            model.addAttribute(
                    "error",
                    "Usuario o contraseña incorrectos"
            );
        }

        return "login";
    }

    // =========================
    // DASHBOARD
    // =========================
    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {

        if (model != null) {
            model.addAttribute(
                    "mensaje",
                    "Panel principal del sistema de matrículas"
            );
        }

        return "dashboard";
    }

    // =========================
    // CONFIGURACIÓN
    // =========================
    @GetMapping("/configuracion")
    public String mostrarConfiguracion(Model model) {

        List<User> usuarios =
                userRepository.findAll();

        if (model != null) {
            model.addAttribute(
                    "usuarios",
                    usuarios
            );
        }

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

        nuevoUsuario.setPassword(
                PasswordHasher.hashPassword(password)
        );

        nuevoUsuario.setIngreso(
                LocalDateTime.now()
        );

        nuevoUsuario.setEstado("Activo");

        userRepository.save(nuevoUsuario);

        // BITÁCORA
        registrarBitacora(
                nuevoUsuario,
                "Nuevo usuario registrado"
        );

        return "redirect:/configuracion";
    }

    // =========================
    // DESHABILITAR USUARIO
    // =========================
    @GetMapping("/deshabilitar/{id}")
    public String deshabilitarUsuario(
            @PathVariable Integer id) {

        User usuario =
                userRepository.findById(id)
                        .orElse(null);

        if (usuario != null) {

            usuario.setEstado("Deshabilitado");

            userRepository.save(usuario);

            // BITÁCORA
            registrarBitacora(
                    usuario,
                    "Usuario deshabilitado"
            );
        }

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

        User usuario =
                userRepository.findById(id)
                        .orElse(null);

        if (usuario != null) {

            usuario.setUsername(username);

            usuario.setPassword(
                    PasswordHasher.hashPassword(password)
            );

            userRepository.save(usuario);

            // BITÁCORA
            registrarBitacora(
                    usuario,
                    "Usuario editado"
            );
        }

        return "redirect:/configuracion";
    }

    // =========================
    // REGISTRAR ESTUDIANTE
    // =========================
    @GetMapping("/registrar-estudiante")
    public String mostrarRegistroEstudiante(
            Model model) {

        if (model != null) {
            model.addAttribute(
                    "estudiante",
                    new Estudiante()
            );
        }

        return "registrar-estudiante";
    }

    @PostMapping("/guardar-estudiante")
    public String guardarEstudiante(
            @ModelAttribute Estudiante estudiante,
            Model model) {

        try {

            estudianteRepository.save(estudiante);

            return "redirect:/gestion-matricula";

        } catch (Exception e) {

            if (model != null) {

                model.addAttribute(
                        "error",
                        "El NIE ya existe o hay datos incorrectos"
                );

                model.addAttribute(
                        "estudiante",
                        estudiante
                );
            }

            return "registrar-estudiante";
        }
    }

    // =========================
    // GESTIÓN MATRÍCULA
    // =========================
    @GetMapping("/gestion-matricula")
    public String mostrarGestionMatricula(
            Model model) {

        List<Estudiante> estudiantes =
                estudianteRepository.findAll();

        if (model != null) {

            model.addAttribute(
                    "estudiantes",
                    estudiantes
            );
        }

        return "gestion-matricula";
    }
}