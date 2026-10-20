package com.example.login.controller;

import com.example.login.model.Bachillerato;
import com.example.login.model.Bitacora;
import com.example.login.model.Estudiante;
import com.example.login.model.Grado;
import com.example.login.model.LoginModel;
import com.example.login.model.Seccion;
import com.example.login.model.User;
import com.example.login.repository.BachilleratoRepository;
import com.example.login.repository.BitacoraRepository;
import com.example.login.repository.EstudianteRepository;
import com.example.login.repository.GradoRepository;
import com.example.login.repository.PagoRepository;
import com.example.login.repository.SeccionRepository;
import com.example.login.repository.UserRepository;
import com.example.login.security.PasswordHasher;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginWebController {

    @Autowired private UserRepository         userRepository;
    @Autowired private EstudianteRepository   estudianteRepository;
    @Autowired private BitacoraRepository     bitacoraRepository;
    @Autowired private PagoRepository         pagoRepository;
    @Autowired private BachilleratoRepository bachilleratoRepository;
    @Autowired private GradoRepository        gradoRepository;
    @Autowired private SeccionRepository      seccionRepository;

    private static final String[] MESES = {
        "", "Enero","Febrero","Marzo","Abril","Mayo","Junio",
        "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
    };

    // ---- helpers ----
    private User usuarioDeSesion(HttpSession session) {
        return (User) session.getAttribute("usuarioLogueado");
    }

    private boolean haySesion(HttpSession session) {
        return usuarioDeSesion(session) != null;
    }

    private boolean esAdmin(HttpSession session) {
        User u = usuarioDeSesion(session);
        return u != null && "admin".equalsIgnoreCase(u.getRol());
    }

    // =========================
    // BITÁCORA
    // =========================
    private void registrarBitacora(User usuarioAccion, Integer usuarioAfectado,
                                    String accion, String descripcion) {
        Bitacora b = new Bitacora();
        b.setUsuario(usuarioAccion);
        b.setIdUsuarioAfectado(usuarioAfectado);
        b.setAccion(accion);
        b.setDescripcion(descripcion);
        b.setFecha(LocalDateTime.now());
        bitacoraRepository.save(b);
    }

    // =========================
    // LOGIN
    // =========================
    @GetMapping("/")
    public String mostrarLogin(Model model, HttpSession session) {
        // ✅ Si ya hay sesión, no mostrar login otra vez
        if (haySesion(session)) return "redirect:/dashboard";

        model.addAttribute("loginModel", new LoginModel());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginModel loginModel,
                        Model model, HttpSession session) {

        User user = userRepository.findByUsername(loginModel.getUsername()).orElse(null);

        if (user != null) {
            if (!"activo".equalsIgnoreCase(user.getEstado())) {
                model.addAttribute("error", "Usuario deshabilitado");
                return "login";
            }
            if (PasswordHasher.verifyPassword(loginModel.getPassword(), user.getPassword())) {
                user.setIngreso(LocalDateTime.now());
                userRepository.save(user);
                session.setAttribute("usuarioLogueado", user);
                session.setAttribute("rolUsuario", user.getRol());
                registrarBitacora(user, user.getIdUsuario(), "LOGIN", "El usuario inició sesión");
                return "redirect:/dashboard";
            }
        }

        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login";
    }

    // =========================
    // DASHBOARD — protegido
    // =========================
    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, HttpSession session) {
        // ✅ BLOQUEO: sin sesión → login
        if (!haySesion(session)) return "redirect:/";

        int mesActual  = LocalDate.now().getMonthValue();
        int anioActual = LocalDate.now().getYear();

        long totalEstudiantes  = estudianteRepository.count();
        long matriculasActivas = estudianteRepository.findAll().stream()
            .filter(e -> "Activo".equalsIgnoreCase(e.getEstadoPersona())).count();
        long pagosPendientes   = pagoRepository
            .findByMesAndAnioAndEstado(mesActual, anioActual, "pendiente").size();
        Double totalMes = pagoRepository.totalRecaudado(mesActual, anioActual);

        model.addAttribute("mensaje",           "Panel principal del sistema de matrículas");
        model.addAttribute("totalEstudiantes",  totalEstudiantes);
        model.addAttribute("matriculasActivas", matriculasActivas);
        model.addAttribute("pagosPendientes",   pagosPendientes);
        model.addAttribute("totalMes",          totalMes != null ? totalMes : 0.0);

        return "dashboard";
    }

    // =========================
    // REPORTES — solo admin
    // =========================
    @GetMapping("/reportes")
    public String mostrarReportes(Model model, HttpSession session, RedirectAttributes ra) {
        if (!esAdmin(session)) {
            ra.addFlashAttribute("error", "No tienes permisos para acceder a Reportes");
            return "redirect:/";
        }

        int mesActual  = LocalDate.now().getMonthValue();
        int anioActual = LocalDate.now().getYear();

        long totalEstudiantes = estudianteRepository.count();
        Double totalMes = pagoRepository.totalRecaudado(mesActual, anioActual);
        long pagadosMes = pagoRepository
            .findByMesAndAnioAndEstado(mesActual, anioActual, "pagado").size();

        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("totalMes",   totalMes != null ? totalMes : 0.0);
        model.addAttribute("pagadosMes", pagadosMes);
        model.addAttribute("mesActual",  MESES[mesActual]);
        model.addAttribute("anioActual", anioActual);
        model.addAttribute("meses",      MESES);

        return "reportes";
    }

    // =========================
    // CONFIGURACIÓN — solo admin
    // =========================
    @GetMapping("/configuracion")
    public String mostrarConfiguracion(Model model, HttpSession session, RedirectAttributes ra) {
        if (!esAdmin(session)) {
            ra.addFlashAttribute("error", "No tienes permisos para acceder a Configuración");
            return "redirect:/";
        }
        model.addAttribute("usuarios", userRepository.findAll());
        return "configuracion";
    }

    // =========================
    // GUARDAR USUARIO — solo admin
    // =========================
    @PostMapping("/guardar-usuario")
    public String guardarUsuario(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam(defaultValue = "docente") String rol,
                                  HttpSession session, RedirectAttributes ra) {
        if (!esAdmin(session)) return "redirect:/";

        if (userRepository.findByUsername(username).isPresent()) {
            ra.addFlashAttribute("error", "El usuario ya existe");
            return "redirect:/configuracion";
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(PasswordHasher.hashPassword(password));
        u.setIngreso(LocalDateTime.now());
        u.setEstado("activo");
        u.setRol(rol);
        userRepository.save(u);

        registrarBitacora(usuarioDeSesion(session),
            u.getIdUsuario(), "REGISTRO", "Se registró usuario con rol: " + rol);
        ra.addFlashAttribute("success", "Usuario creado exitosamente");
        return "redirect:/configuracion";
    }

    // =========================
    // DESHABILITAR USUARIO — solo admin
    // =========================
    @GetMapping("/deshabilitar/{id}")
    public String deshabilitarUsuario(@PathVariable Integer id,
                                       HttpSession session, RedirectAttributes ra) {
        if (!esAdmin(session)) return "redirect:/";

        User usuario = userRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setEstado("Deshabilitado");
            userRepository.save(usuario);
            registrarBitacora(usuarioDeSesion(session),
                usuario.getIdUsuario(), "DESHABILITAR", "Usuario deshabilitado");
            ra.addFlashAttribute("success", "Usuario deshabilitado");
        }
        return "redirect:/configuracion";
    }

    // =========================
    // ACTUALIZAR USUARIO — solo admin
    // =========================
    @PostMapping("/actualizar-usuario")
    public String actualizarUsuario(@RequestParam Integer id,
                                     @RequestParam String username,
                                     @RequestParam String password,
                                     HttpSession session, RedirectAttributes ra) {
        if (!esAdmin(session)) return "redirect:/";

        User usuario = userRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setUsername(username);
            usuario.setPassword(PasswordHasher.hashPassword(password));
            userRepository.save(usuario);
            registrarBitacora(usuarioDeSesion(session),
                usuario.getIdUsuario(), "EDITAR", "Usuario editado");
            ra.addFlashAttribute("success", "Usuario actualizado");
        }
        return "redirect:/configuracion";
    }

    // =========================
    // REGISTRAR ESTUDIANTE — protegido
    // =========================
    @GetMapping("/registrar-estudiante")
    public String mostrarRegistroEstudiante(Model model, HttpSession session,
                                             RedirectAttributes ra) {
        if (!haySesion(session)) return "redirect:/";

        model.addAttribute("estudiante", new Estudiante());

        List<Bachillerato> bachilleratos = bachilleratoRepository.findAll();
        model.addAttribute("bachilleratos", bachilleratos);

        Map<Integer, List<Map<String, Object>>> gradosPorBachillerato = new LinkedHashMap<>();
        for (Bachillerato b : bachilleratos) {
            List<Grado> grados = gradoRepository.findByBachillerato_IdBachillerato(b.getIdBachillerato());
            List<Map<String, Object>> lista = new ArrayList<>();
            for (Grado g : grados) {
                Map<String, Object> m = new HashMap<>();
                m.put("id",     g.getIdGrado());
                m.put("nombre", g.getNombre());
                lista.add(m);
            }
            gradosPorBachillerato.put(b.getIdBachillerato(), lista);
        }
        model.addAttribute("gradosPorBachillerato", gradosPorBachillerato);

        Map<Integer, List<Map<String, Object>>> seccionesPorGrado = new LinkedHashMap<>();
        List<Seccion> todasSecciones = seccionRepository.findAll();
        for (Bachillerato b : bachilleratos) {
            List<Grado> grados = gradoRepository.findByBachillerato_IdBachillerato(b.getIdBachillerato());
            for (Grado g : grados) {
                List<Map<String, Object>> lista = new ArrayList<>();
                for (Seccion s : todasSecciones) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id",     s.getIdSeccion());
                    m.put("nombre", s.getNombre());
                    lista.add(m);
                }
                seccionesPorGrado.put(g.getIdGrado(), lista);
            }
        }
        model.addAttribute("seccionesPorGrado", seccionesPorGrado);
        model.addAttribute("anioActual", LocalDate.now().getYear());

        return "registrar-estudiante";
    }

    @PostMapping("/guardar-estudiante")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante,
                                     Model model, HttpSession session,
                                     RedirectAttributes ra) {
        if (!haySesion(session)) return "redirect:/";
        User usuario = usuarioDeSesion(session);

        try {
            estudianteRepository.save(estudiante);
            registrarBitacora(usuario, null, "REGISTRO_ESTUDIANTE",
                "Registró estudiante: " + estudiante.getPrimerNombre()
                + " " + estudiante.getPrimerApellido());
            ra.addFlashAttribute("success", "Estudiante registrado exitosamente");
            return "redirect:/gestion-matricula";
        } catch (Exception e) {
            model.addAttribute("error", "El NIE ya existe o hay datos incorrectos");
            model.addAttribute("estudiante", estudiante);
            return "registrar-estudiante";
        }
    }

    // =========================
    // GESTIÓN MATRÍCULA — protegido
    // =========================
    @GetMapping("/gestion-matricula")
    public String mostrarGestionMatricula(Model model, HttpSession session) {
        if (!haySesion(session)) return "redirect:/";
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        return "gestion-matricula";
    }

    // =========================
    // LOGOUT
    // =========================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        User usuario = usuarioDeSesion(session);
        if (usuario != null)
            registrarBitacora(usuario, usuario.getIdUsuario(), "LOGOUT", "Cerró sesión");
        session.invalidate();
        return "redirect:/";
    }

    // =========================
    // CREAR USUARIO DESDE LOGIN (verificado por admin)
    // =========================
    @PostMapping("/api/crear-usuario")
    @ResponseBody
    public ResponseEntity<?> crearUsuarioDesdeLogin(@RequestBody Map<String, String> body,
                                                     HttpSession session) {

        Boolean adminVerificado = (Boolean) session.getAttribute("adminVerificado");
        if (adminVerificado == null || !adminVerificado) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado");
        }

        String username = body.get("username");
        String password = body.get("password");
        String rol      = body.getOrDefault("rol", "docente");

        if (username == null || password == null)
            return ResponseEntity.badRequest().body("Faltan datos");

        if (userRepository.findByUsername(username).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe");

        User u = new User();
        u.setUsername(username);
        u.setPassword(PasswordHasher.hashPassword(password));
        u.setRol(rol);
        u.setEstado("activo");
        u.setIngreso(LocalDateTime.now());
        userRepository.save(u);

        session.removeAttribute("adminVerificado");

        return ResponseEntity.ok().build();
    }
}