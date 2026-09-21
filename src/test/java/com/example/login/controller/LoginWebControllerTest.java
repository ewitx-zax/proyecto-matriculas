package com.example.login.controller;

import com.example.login.model.Estudiante;
import com.example.login.model.LoginModel;
import com.example.login.model.User;
import com.example.login.repository.BitacoraRepository;
import com.example.login.repository.EstudianteRepository;
import com.example.login.repository.UserRepository;
import com.example.login.security.PasswordHasher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginWebControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private BitacoraRepository bitacoraRepository;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private LoginWebController controller;

    private MockHttpSession session;
    private User adminUser;
    private User docenteUser;
    private Estudiante estudiante;

    @BeforeEach
    public void setUp() {
        session = new MockHttpSession();
        
        adminUser = new User();
        adminUser.setIdUsuario(1);
        adminUser.setUsername("admin");
        adminUser.setPassword(PasswordHasher.hashPassword("admin123"));
        adminUser.setRol("admin");
        adminUser.setEstado("activo");
        adminUser.setIngreso(LocalDateTime.now());

        docenteUser = new User();
        docenteUser.setIdUsuario(2);
        docenteUser.setUsername("docente");
        docenteUser.setPassword(PasswordHasher.hashPassword("docente123"));
        docenteUser.setRol("docente");
        docenteUser.setEstado("activo");
        docenteUser.setIngreso(LocalDateTime.now());

        estudiante = new Estudiante();
        estudiante.setIdEstudiante(1);
        estudiante.setPrimerNombre("Juan");
        estudiante.setSegundoNombre("Carlos");
        estudiante.setPrimerApellido("Pérez");
        estudiante.setSegundoApellido("Gómez");
        estudiante.setNie("123456");
        estudiante.setNui("789012");
        estudiante.setDui("34567890-1");
        estudiante.setFechaNacimiento(LocalDate.of(2000, 1, 15));
        estudiante.setNacionalidad("Salvadoreña");
        estudiante.setSexo("Masculino");
        estudiante.setEstadoPersona("Activo");
        estudiante.setCorreo("juan.perez@example.com");
    }

    // =========================
    // TESTS...
    // (Mantén todos los tests que ya tienes aquí, son los correctos)
    // =========================
}