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

    @Autowired
    private BitacoraRepository bitacoraRepository;

    // =========================
    // MÉTODO BITÁCORA
    // =========================
    private void registrarBitacora(
            User usuario,
            Integer usuarioAfectado,
            String accion,
            String descripcion) {

        Bitacora bitacora = new Bitacora();

        bitacora.setUsuario(usuario);

        bitacora.setIdUsuarioAfectado(usuarioAfectado);

        bitacora.setAccion(accion);

        bitacora.setDescripcion(descripcion);

        bitacora.setFecha(LocalDateTime.now());

        bitacoraRepository.save(bitacora);
    }

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

            if (!"Activo".equalsIgnoreCase(user.getEstado())) {

                model.addAttribute(
                        "error",
                        "Usuario deshabilitado"
                );

                return "login";
            }

            if (PasswordHasher.verifyPassword(
                    loginModel.getPassword(),
                    user.getPassword())) {

                user.setIngreso(LocalDateTime.now());

                userRepository.save(user);

                // BITÁCORA LOGIN
                registrarBitacora(
                        user,
                        user.getIdUsuario(),
                        "LOGIN",
                        "El usuario inició sesión"
                );

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

        nuevoUsuario.setPassword(
                PasswordHasher.hashPassword(password)
        );

        nuevoUsuario.setIngreso(LocalDateTime.now());

        nuevoUsuario.setEstado("Activo");

        userRepository.save(nuevoUsuario);

        // BITÁCORA REGISTRO
        registrarBitacora(
                nuevoUsuario,
                nuevoUsuario.getIdUsuario(),
                "REGISTRO",
                "Se registró un nuevo usuario"
        );

        return "redirect:/configuracion";
    }

    // =========================
    // DESHABILITAR USUARIO
    // =========================
    @GetMapping("/deshabilitar/{id}")
    public String deshabilitarUsuario(
            @PathVariable Integer id) {

        User usuario = userRepository
                .findById(id)
                .orElse(null);

        if (usuario != null) {

            usuario.setEstado("Deshabilitado");

            userRepository.save(usuario);

            // BITÁCORA DESHABILITAR
            registrarBitacora(
                    usuario,
                    usuario.getIdUsuario(),
                    "DESHABILITAR",
                    "Se deshabilitó un usuario"
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

        User usuario = userRepository
                .findById(id)
                .orElse(null);

        if (usuario != null) {

            usuario.setUsername(username);

            usuario.setPassword(
                    PasswordHasher.hashPassword(password)
            );

            userRepository.save(usuario);

            // BITÁCORA EDITAR
            registrarBitacora(
                    usuario,
                    usuario.getIdUsuario(),
                    "EDITAR",
                    "Se editaron los datos del usuario"
            );
        }

        return "redirect:/configuracion";
    }

    // =========================
    // REGISTRAR ESTUDIANTE
    // =========================
    @GetMapping("/registrar-estudiante")
    public String mostrarRegistroEstudiante(Model model) {

        model.addAttribute(
                "estudiante",
                new Estudiante()
        );

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

            model.addAttribute(
                    "error",
                    "El NIE ya existe o hay datos incorrectos"
            );

            model.addAttribute(
                    "estudiante",
                    estudiante
            );

            return "registrar-estudiante";
        }
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