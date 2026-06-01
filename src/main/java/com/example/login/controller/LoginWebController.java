package com.example.login.controller;

import com.example.login.model.Bitacora;
import com.example.login.model.Estudiante;
import com.example.login.model.LoginModel;
import com.example.login.model.User;
import com.example.login.repository.BitacoraRepository;
import com.example.login.repository.EstudianteRepository;
import com.example.login.repository.UserRepository;
import com.example.login.security.PasswordHasher;

import jakarta.servlet.http.HttpSession;

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
            User usuarioAccion,
            Integer usuarioAfectado,
            String accion,
            String descripcion) {

        Bitacora bitacora = new Bitacora();
        bitacora.setUsuario(usuarioAccion);
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
            Model model,
            HttpSession session) {

        User user = userRepository
                .findByUsername(loginModel.getUsername())
                .orElse(null);

        if (user != null) {
            if (!"activo".equalsIgnoreCase(user.getEstado())) { // ← solo permite "activo"
                model.addAttribute("error", "Usuario deshabilitado");
                return "login";
            }

            if (PasswordHasher.verifyPassword(loginModel.getPassword(), user.getPassword())) {
                user.setIngreso(LocalDateTime.now());
                userRepository.save(user);

                session.setAttribute("usuarioLogueado", user);

                registrarBitacora(user, user.getIdUsuario(), "LOGIN", "El usuario inició sesión");
                model.addAttribute("mensaje", "Bienvenido " + user.getUsername());
                return "dashboard";
            }
        }

        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login";
    }

    // =========================
    // DASHBOARD
    // =========================
    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        model.addAttribute("mensaje", "Panel principal del sistema de matrículas");
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
            @RequestParam String password,
            HttpSession session) {

        User usuarioAccion = (User) session.getAttribute("usuarioLogueado");
        if (usuarioAccion == null) return "redirect:/";

        User nuevoUsuario = new User();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setPassword(PasswordHasher.hashPassword(password));
        nuevoUsuario.setIngreso(LocalDateTime.now());
        nuevoUsuario.setEstado("activo"); // ← en minúscula
        userRepository.save(nuevoUsuario);

        registrarBitacora(usuarioAccion, nuevoUsuario.getIdUsuario(), "REGISTRO", "Se registró un nuevo usuario");
        return "redirect:/configuracion";
    }

    // =========================
    // DESHABILITAR USUARIO
    // =========================
    @GetMapping("/deshabilitar/{id}")
    public String deshabilitarUsuario(
            @PathVariable Integer id,
            HttpSession session) {

        User usuarioAccion = (User) session.getAttribute("usuarioLogueado");
        if (usuarioAccion == null) return "redirect:/";

        User usuario = userRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setEstado("Deshabilitado"); // ← Deshabilitado
            userRepository.save(usuario);
            registrarBitacora(usuarioAccion, usuario.getIdUsuario(), "DESHABILITAR", "El usuario deshabilitó otro usuario");
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
            @RequestParam String password,
            HttpSession session) {

        User usuarioAccion = (User) session.getAttribute("usuarioLogueado");
        if (usuarioAccion == null) return "redirect:/";

        User usuario = userRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setUsername(username);
            usuario.setPassword(PasswordHasher.hashPassword(password));
            userRepository.save(usuario);
            registrarBitacora(usuarioAccion, usuario.getIdUsuario(), "EDITAR", "El usuario editó otro usuario");
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
            @ModelAttribute Estudiante estudiante,
            Model model) {

        try {
            estudianteRepository.save(estudiante);
            return "redirect:/gestion-matricula";
        } catch (Exception e) {
            model.addAttribute("error", "El NIE ya existe o hay datos incorrectos");
            model.addAttribute("estudiante", estudiante);
            return "registrar-estudiante";
        }
    }

    // =========================
    // GESTIÓN MATRÍCULA
    // =========================
    @GetMapping("/gestion-matricula")
    public String mostrarGestionMatricula(Model model) {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        model.addAttribute("estudiantes", estudiantes);
        return "gestion-matricula";
    }
}