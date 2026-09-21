package com.example.login.controller;

import com.example.login.model.Bachillerato;
import com.example.login.model.Estudiante;
import com.example.login.model.Grado;
import com.example.login.model.Seccion;
import com.example.login.repository.BachilleratoRepository;
import com.example.login.repository.EstudianteRepository;
import com.example.login.repository.GradoRepository;
import com.example.login.repository.SeccionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteRestController {

    @Autowired private EstudianteRepository  estudianteRepository;
    @Autowired private BachilleratoRepository bachilleratoRepository;
    @Autowired private GradoRepository        gradoRepository;
    @Autowired private SeccionRepository      seccionRepository;

    // =========================
    // GET TODOS
    // =========================
    @GetMapping
    public List<Estudiante> getAllEstudiantes() {
        return estudianteRepository.findAll();
    }

    // =========================
    // GET POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<?> getEstudianteById(@PathVariable Integer id) {
        Estudiante e = estudianteRepository.findById(id).orElse(null);
        if (e == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(buildMap(e));
    }

    // =========================
    // GET POR NIE
    // =========================
    @GetMapping("/nie/{nie}")
    public ResponseEntity<?> getEstudianteByNie(@PathVariable String nie) {
        Optional<Estudiante> opt = estudianteRepository.findByNie(nie);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(buildMap(opt.get()));
    }

    // =========================
    // BUSCAR POR TEXTO
    // =========================
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarEstudiantes(@RequestParam String q) {
        List<Estudiante> estudiantes = estudianteRepository.searchEstudiantes(q);

        List<Map<String, Object>> resultados = estudiantes.stream().map(e -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idEstudiante",  e.getIdEstudiante());
            map.put("nie",           e.getNie());
            map.put("nombreCompleto",e.getNombreCompleto());
            map.put("grado",   e.getGrado()   != null ? e.getGrado().getNombre()   : "Sin asignar");
            map.put("seccion", e.getSeccion() != null ? e.getSeccion().getNombre() : "Sin asignar");
            return map;
        }).toList();

        return ResponseEntity.ok(resultados);
    }

    // =========================
    // ACTUALIZAR MATRÍCULA
    // =========================
    @PutMapping("/{id}/matricula")
    public ResponseEntity<?> actualizarMatricula(@PathVariable Integer id,
                                                  @RequestBody Map<String, Object> body) {
        Estudiante e = estudianteRepository.findById(id).orElse(null);
        if (e == null) return ResponseEntity.notFound().build();

        if (body.containsKey("idBachillerato")) {
            Integer idB = (Integer) body.get("idBachillerato");
            e.setBachillerato(idB != null ? bachilleratoRepository.findById(idB).orElse(null) : null);
        }
        if (body.containsKey("idGrado")) {
            Integer idG = (Integer) body.get("idGrado");
            e.setGrado(idG != null ? gradoRepository.findById(idG).orElse(null) : null);
        }
        if (body.containsKey("idSeccion")) {
            Integer idS = (Integer) body.get("idSeccion");
            e.setSeccion(idS != null ? seccionRepository.findById(idS).orElse(null) : null);
        }
        if (body.containsKey("anioLectivo")) {
            e.setAnioLectivo((Integer) body.get("anioLectivo"));
        }

        e.setActivo(true);
        estudianteRepository.save(e);

        Map<String, Object> resp = new HashMap<>();
        resp.put("success",      true);
        resp.put("message",      "Matrícula actualizada correctamente");
        resp.put("idEstudiante", e.getIdEstudiante());
        resp.put("nombre",       e.getNombreCompleto());
        resp.put("bachillerato", e.getBachillerato() != null ? e.getBachillerato().getNombre() : null);
        resp.put("grado",        e.getGrado()        != null ? e.getGrado().getNombre()        : null);
        resp.put("seccion",      e.getSeccion()      != null ? e.getSeccion().getNombre()      : null);

        return ResponseEntity.ok(resp);
    }

    // =========================
    // ACTUALIZAR DATOS GENERALES
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstudiante(@PathVariable Integer id,
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

    // =========================
    // ELIMINAR
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstudiante(@PathVariable Integer id) {
        if (!estudianteRepository.existsById(id)) return ResponseEntity.notFound().build();
        estudianteRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // =========================
    // HELPER: construir mapa de respuesta
    // =========================
    private Map<String, Object> buildMap(Estudiante e) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("idEstudiante",   e.getIdEstudiante());
        resp.put("nie",            e.getNie());
        resp.put("nui",            e.getNui());
        resp.put("dui",            e.getDui());
        resp.put("primerNombre",   e.getPrimerNombre());
        resp.put("segundoNombre",  e.getSegundoNombre());
        resp.put("tercerNombre",   e.getTercerNombre());
        resp.put("primerApellido", e.getPrimerApellido());
        resp.put("segundoApellido",e.getSegundoApellido());
        resp.put("tercerApellido", e.getTercerApellido());
        resp.put("nombreCompleto", e.getNombreCompleto());
        resp.put("fechaNacimiento",e.getFechaNacimiento());
        resp.put("nacionalidad",   e.getNacionalidad());
        resp.put("sexo",           e.getSexo());
        resp.put("correo",         e.getCorreo());
        resp.put("estadoPersona",  e.getEstadoPersona());
        resp.put("anioLectivo",    e.getAnioLectivo());
        resp.put("fechaInscripcion",e.getFechaInscripcion());
        resp.put("activo",         e.getActivo());

        if (e.getBachillerato() != null) {
            resp.put("idBachillerato",    e.getBachillerato().getIdBachillerato());
            resp.put("bachilleratoNombre",e.getBachillerato().getNombre());
        } else {
            resp.put("idBachillerato",    null);
            resp.put("bachilleratoNombre",null);
        }

        if (e.getGrado() != null) {
            resp.put("idGrado",    e.getGrado().getIdGrado());
            resp.put("gradoNombre",e.getGrado().getNombre());
        } else {
            resp.put("idGrado",    null);
            resp.put("gradoNombre",null);
        }

        if (e.getSeccion() != null) {
            resp.put("idSeccion",    e.getSeccion().getIdSeccion());
            resp.put("seccionNombre",e.getSeccion().getNombre());
        } else {
            resp.put("idSeccion",    null);
            resp.put("seccionNombre",null);
        }

        return resp;
    }
}