/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.example.login.controller;

import com.example.login.model.Estudiante;
import com.example.login.model.LoginModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.ui.Model;

/**
 *
 * @author MINEDUCYT
 */
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

    /**
     * Test of mostrarLogin method, of class LoginWebController.
     */
    @Test
    public void testMostrarLogin() {
        System.out.println("mostrarLogin");
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.mostrarLogin(model);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of login method, of class LoginWebController.
     */
    @Test
    public void testLogin() {
        System.out.println("login");
        LoginModel loginModel = null;
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.login(loginModel, model);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mostrarDashboard method, of class LoginWebController.
     */
    @Test
    public void testMostrarDashboard() {
        System.out.println("mostrarDashboard");
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.mostrarDashboard(model);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mostrarConfiguracion method, of class LoginWebController.
     */
    @Test
    public void testMostrarConfiguracion() {
        System.out.println("mostrarConfiguracion");
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.mostrarConfiguracion(model);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of guardarUsuario method, of class LoginWebController.
     */
    @Test
    public void testGuardarUsuario() {
        System.out.println("guardarUsuario");
        String username = "";
        String password = "";
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.guardarUsuario(username, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deshabilitarUsuario method, of class LoginWebController.
     */
    @Test
    public void testDeshabilitarUsuario() {
        System.out.println("deshabilitarUsuario");
        Integer id = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.deshabilitarUsuario(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of actualizarUsuario method, of class LoginWebController.
     */
    @Test
    public void testActualizarUsuario() {
        System.out.println("actualizarUsuario");
        Integer id = null;
        String username = "";
        String password = "";
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.actualizarUsuario(id, username, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mostrarRegistroEstudiante method, of class LoginWebController.
     */
    @Test
    public void testMostrarRegistroEstudiante() {
        System.out.println("mostrarRegistroEstudiante");
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.mostrarRegistroEstudiante(model);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of guardarEstudiante method, of class LoginWebController.
     */
    @Test
    public void testGuardarEstudiante() {
        System.out.println("guardarEstudiante");
        Estudiante estudiante = null;
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.guardarEstudiante(estudiante, model);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mostrarGestionMatricula method, of class LoginWebController.
     */
    @Test
    public void testMostrarGestionMatricula() {
        System.out.println("mostrarGestionMatricula");
        Model model = null;
        LoginWebController instance = new LoginWebController();
        String expResult = "";
        String result = instance.mostrarGestionMatricula(model);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
