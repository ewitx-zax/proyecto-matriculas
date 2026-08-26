package com.example.login.controller;

import com.example.login.model.Estudiante;
import com.example.login.model.LoginModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

public class LoginWebControllerTest {

    public LoginWebControllerTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testMostrarLogin() {
        System.out.println("mostrarLogin");
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.mostrarLogin(model);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testLogin() {
        System.out.println("login");
        LoginModel loginModel = null;
        Model model = null;
        MockHttpSession session = new MockHttpSession();
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.login(loginModel, model, session);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testMostrarDashboard() {
        System.out.println("mostrarDashboard");
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.mostrarDashboard(model);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testMostrarConfiguracion() {
        System.out.println("mostrarConfiguracion");
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.mostrarConfiguracion(model);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGuardarUsuario() {
        System.out.println("guardarUsuario");
        String username = "";
        String password = "";
        String rol = "docente";           // ← parámetro nuevo
        MockHttpSession session = new MockHttpSession();
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.guardarUsuario(username, password, rol, session); // ← corregido
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testDeshabilitarUsuario() {
        System.out.println("deshabilitarUsuario");
        Integer id = null;
        MockHttpSession session = new MockHttpSession();
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.deshabilitarUsuario(id, session);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testActualizarUsuario() {
        System.out.println("actualizarUsuario");
        Integer id = null;
        String username = "";
        String password = "";
        MockHttpSession session = new MockHttpSession();
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.actualizarUsuario(id, username, password, session);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testMostrarRegistroEstudiante() {
        System.out.println("mostrarRegistroEstudiante");
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.mostrarRegistroEstudiante(model);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGuardarEstudiante() {
        System.out.println("guardarEstudiante");
        Estudiante estudiante = null;
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.guardarEstudiante(estudiante, model);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testMostrarGestionMatricula() {
        System.out.println("mostrarGestionMatricula");
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.mostrarGestionMatricula(model);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}