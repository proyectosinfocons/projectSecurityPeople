package com.SecurityPeople.projectSecurityPeople.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.SecurityPeople.projectSecurityPeople.model.Reporte;
import com.SecurityPeople.projectSecurityPeople.repository.ReporteRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public Reporte guardarReporte(String descripcion, Double latitud, Double longitud, MultipartFile archivo) throws IOException {
        Reporte reporte = new Reporte();
        reporte.setDescripcion(descripcion);
        reporte.setLatitud(latitud);
        reporte.setLongitud(longitud);
        reporte.setFechaRegistro(LocalDateTime.now());
        reporte.setArchivo(archivo.getBytes());
        return reporteRepository.save(reporte);
    }

    public Reporte obtenerReportePorId(Long id) {
        Optional<Reporte> opt = reporteRepository.findById(id);
        return opt.orElse(null);
    }


}
