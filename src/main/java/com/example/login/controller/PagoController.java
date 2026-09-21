package com.example.login.controller;

import com.example.login.model.Estudiante;
import com.example.login.model.Pago;
import com.example.login.repository.EstudianteRepository;
import com.example.login.repository.PagoRepository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;

@Controller
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    private static final String[] MESES = {
        "", "Enero","Febrero","Marzo","Abril","Mayo","Junio",
        "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
    };

    // =============================================
    // PANTALLA DE PAGOS
    // =============================================
    @GetMapping("/pagos")
    public String mostrarPagos(
            @RequestParam(defaultValue = "0") int mes,
            @RequestParam(defaultValue = "0") int anio,
            Model model) {

        int mesActual  = mes  > 0 ? mes  : LocalDate.now().getMonthValue();
        int anioActual = anio > 0 ? anio : LocalDate.now().getYear();

        List<Estudiante> estudiantes = estudianteRepository.findAll();
        List<Map<String, Object>> filas = new ArrayList<>();

        for (Estudiante e : estudiantes) {
            Map<String, Object> fila = new LinkedHashMap<>();
            fila.put("idEstudiante", e.getIdEstudiante());
            fila.put("nombre", nombre(e));
            fila.put("nie", e.getNie() != null ? e.getNie() : "—");

            Optional<Pago> pago = pagoRepository
                .findByEstudiante_IdEstudianteAndMesAndAnio(
                    e.getIdEstudiante(), mesActual, anioActual);

            if (pago.isPresent()) {
                fila.put("idPago",    pago.get().getIdPago());
                fila.put("estado",    pago.get().getEstado());
                fila.put("fechaPago", pago.get().getFechaPago());
                fila.put("monto",     pago.get().getMonto());
            } else {
                fila.put("idPago",    null);
                fila.put("estado",    "sin registro");
                fila.put("fechaPago", null);
                fila.put("monto",     null);
            }
            filas.add(fila);
        }

        long pagados    = filas.stream().filter(f -> "pagado".equals(f.get("estado"))).count();
        long pendientes = filas.stream().filter(f -> "pendiente".equals(f.get("estado"))).count();
        Double total    = pagoRepository.totalRecaudado(mesActual, anioActual);

        model.addAttribute("filas",      filas);
        model.addAttribute("mes",        mesActual);
        model.addAttribute("anio",       anioActual);
        model.addAttribute("nombreMes",  MESES[mesActual]);
        model.addAttribute("pagados",    pagados);
        model.addAttribute("pendientes", pendientes);
        model.addAttribute("total",      total != null ? total : 0.0);
        model.addAttribute("meses",      MESES);

        return "pagos";
    }

    // =============================================
    // REGISTRAR PAGO
    // =============================================
    @PostMapping("/api/pagos/registrar")
    @ResponseBody
    public ResponseEntity<?> registrarPago(@RequestBody Map<String, Object> body) {
        try {
            Integer idEstudiante = (Integer) body.get("idEstudiante");
            Integer mes  = (Integer) body.get("mes");
            Integer anio = (Integer) body.get("anio");

            Estudiante estudiante = estudianteRepository.findById(idEstudiante).orElse(null);
            if (estudiante == null) return ResponseEntity.notFound().build();

            Optional<Pago> existente = pagoRepository
                .findByEstudiante_IdEstudianteAndMesAndAnio(idEstudiante, mes, anio);

            Pago pago = existente.orElse(new Pago());
            pago.setEstudiante(estudiante);
            pago.setMes(mes);
            pago.setAnio(anio);
            pago.setEstado("pagado");
            pago.setFechaPago(LocalDate.now());
            pagoRepository.save(pago);

            Map<String, Object> resp = new HashMap<>();
            resp.put("idPago",    pago.getIdPago());
            resp.put("fechaPago", pago.getFechaPago().toString());
            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // =============================================
    // ANULAR PAGO
    // =============================================
    @DeleteMapping("/api/pagos/{id}")
    @ResponseBody
    public ResponseEntity<?> anularPago(@PathVariable Integer id) {
        if (!pagoRepository.existsById(id)) return ResponseEntity.notFound().build();
        pagoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // =============================================
    // ⚠️ ELIMINADO: mostrarReportes() - Ahora está en LoginWebController
    // =============================================

    // =============================================
    // EXPORTAR EXCEL — PAGOS
    // =============================================
    @GetMapping("/reportes/excel/pagos")
    public ResponseEntity<byte[]> exportarPagosExcel(
            @RequestParam int mes,
            @RequestParam int anio) throws Exception {

        List<Estudiante> estudiantes = estudianteRepository.findAll();

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Pagos " + MESES[mes] + " " + anio);

            // Estilos
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle pagadoStyle = wb.createCellStyle();
            pagadoStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            pagadoStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle pendienteStyle = wb.createCellStyle();
            pendienteStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            pendienteStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Encabezado
            String[] cols = {"ID", "NIE", "Nombre", "Estado", "Monto", "Fecha Pago"};
            Row header = sheet.createRow(0);
            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowNum = 1;
            double totalRecaudado = 0;
            for (Estudiante e : estudiantes) {
                Optional<Pago> pago = pagoRepository
                    .findByEstudiante_IdEstudianteAndMesAndAnio(e.getIdEstudiante(), mes, anio);

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(e.getIdEstudiante());
                row.createCell(1).setCellValue(e.getNie() != null ? e.getNie() : "—");
                row.createCell(2).setCellValue(nombre(e));

                if (pago.isPresent()) {
                    Cell estadoCell = row.createCell(3);
                    estadoCell.setCellValue(pago.get().getEstado());
                    estadoCell.setCellStyle(
                        "pagado".equals(pago.get().getEstado()) ? pagadoStyle : pendienteStyle);
                    row.createCell(4).setCellValue(pago.get().getMonto().doubleValue());
                    row.createCell(5).setCellValue(
                        pago.get().getFechaPago() != null
                            ? pago.get().getFechaPago().toString() : "—");
                    if ("pagado".equals(pago.get().getEstado()))
                        totalRecaudado += pago.get().getMonto().doubleValue();
                } else {
                    row.createCell(3).setCellValue("Sin registro");
                    row.createCell(4).setCellValue("—");
                    row.createCell(5).setCellValue("—");
                }
            }

            // Fila total
            Row totalRow = sheet.createRow(rowNum + 1);
            Cell labelCell = totalRow.createCell(3);
            labelCell.setCellValue("TOTAL RECAUDADO:");
            labelCell.setCellStyle(headerStyle);
            Cell totalCell = totalRow.createCell(4);
            totalCell.setCellValue("$" + String.format("%.2f", totalRecaudado));
            totalCell.setCellStyle(headerStyle);

            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment()
                .filename("pagos_" + MESES[mes] + "_" + anio + ".xlsx")
                .build());

            return ResponseEntity.ok().headers(headers).body(out.toByteArray());
        }
    }

    // =============================================
    // EXPORTAR EXCEL — MATRÍCULAS
    // =============================================
    @GetMapping("/reportes/excel/matriculas")
    public ResponseEntity<byte[]> exportarMatriculasExcel() throws Exception {
        List<Estudiante> estudiantes = estudianteRepository.findAll();

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Matrículas");

            CellStyle headerStyle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] cols = {"ID","NIE","NUI","Nombre Completo","Sexo","Estado","Correo","Nacionalidad"};
            Row header = sheet.createRow(0);
            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Estudiante e : estudiantes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(e.getIdEstudiante());
                row.createCell(1).setCellValue(e.getNie()          != null ? e.getNie()          : "—");
                row.createCell(2).setCellValue(e.getNui()          != null ? e.getNui()          : "—");
                row.createCell(3).setCellValue(nombre(e));
                row.createCell(4).setCellValue(e.getSexo()         != null ? e.getSexo()         : "—");
                row.createCell(5).setCellValue(e.getEstadoPersona()!= null ? e.getEstadoPersona(): "—");
                row.createCell(6).setCellValue(e.getCorreo()       != null ? e.getCorreo()       : "—");
                row.createCell(7).setCellValue(e.getNacionalidad() != null ? e.getNacionalidad() : "—");
            }

            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.attachment()
                .filename("matriculas.xlsx")
                .build());

            return ResponseEntity.ok().headers(headers).body(out.toByteArray());
        }
    }

    // ---- helper nombre completo ----
    private String nombre(Estudiante e) {
        return ((e.getPrimerNombre()    != null ? e.getPrimerNombre()    + " " : "")
              + (e.getSegundoNombre()   != null ? e.getSegundoNombre()   + " " : "")
              + (e.getPrimerApellido()  != null ? e.getPrimerApellido()  + " " : "")
              + (e.getSegundoApellido() != null ? e.getSegundoApellido() : "")).trim();
    }
}