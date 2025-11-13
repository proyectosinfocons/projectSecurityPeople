package com.SecurityPeople.projectSecurityPeople.controller;

import com.SecurityPeople.projectSecurityPeople.dto.ReporteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.SecurityPeople.projectSecurityPeople.model.Reporte;
import com.SecurityPeople.projectSecurityPeople.service.ReporteService;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*") // Permite llamadas desde el frontend
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @PostMapping("/guardar")
    public Reporte guardarReporte(
            @RequestParam("descripcion") String descripcion,
            @RequestParam("latitud") Double latitud,
            @RequestParam("longitud") Double longitud,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo
    ) throws Exception {
        return reporteService.guardarReporte(descripcion, latitud, longitud, archivo);
    }



    // ✅ Nuevo método: obtener imagen por ID
    @GetMapping("/archivo/{id}")
    public ResponseEntity<byte[]> obtenerArchivoPorId(@PathVariable Long id) {
        Reporte reporte = reporteService.obtenerReportePorId(id);

        if (reporte == null || reporte.getArchivo() == null) {
            return ResponseEntity.notFound().build();
        }

        // 🔍 Determinar tipo MIME según el contenido
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // por defecto

        byte[] archivo = reporte.getArchivo();
        // (Opcional) Si deseas, podrías detectar MIME dinámicamente

        return new ResponseEntity<>(archivo, headers, HttpStatus.OK);
    }


    @GetMapping("/archivo/video/{id}")
    public ResponseEntity<byte[]> obtenerVideoPorId(@PathVariable Long id) {
        Reporte reporte = reporteService.obtenerReportePorId(id);
        if (reporte == null || reporte.getArchivo() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("video/mp4"));
        headers.set("Content-Disposition", "inline; filename=\"video.mp4\"");

        return new ResponseEntity<>(reporte.getArchivo(), headers, HttpStatus.OK);
    }


    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReporteDTO>> obtenerReportesPorUsuario(@PathVariable Long usuarioId) {
        List<ReporteDTO> reportes = reporteService.obtenerReportesPorUsuario(usuarioId);

        // Limpiar el campo archivo para no saturar la respuesta JSON

        return ResponseEntity.ok(reportes);
    }





}
