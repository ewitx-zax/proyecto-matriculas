package com.example.login.controller;

import com.example.login.model.Estudiante;
import com.example.login.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteRestController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    // PUT /api/estudiantes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstudiante(
            @PathVariable Integer id,
            @RequestBody Map<String, String> datos) {

        Estudiante e = estudianteRepository.findById(id).orElse(null);
        if (e == null) return ResponseEntity.notFound().build();

        if (datos.containsKey("nie"))           e.setNie(datos.get("nie"));
        if (datos.containsKey("primerNombre"))  e.setPrimerNombre(datos.get("primerNombre"));
        if (datos.containsKey("primerApellido"))e.setPrimerApellido(datos.get("primerApellido"));
        if (datos.containsKey("estadoPersona")) e.setEstadoPersona(datos.get("estadoPersona"));
        if (datos.containsKey("correo"))        e.setCorreo(datos.get("correo"));

        estudianteRepository.save(e);
        return ResponseEntity.ok().build();
    }

    // DELETE /api/estudiantes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstudiante(@PathVariable Integer id) {
        if (!estudianteRepository.existsById(id))
            return ResponseEntity.notFound().build();

        estudianteRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}