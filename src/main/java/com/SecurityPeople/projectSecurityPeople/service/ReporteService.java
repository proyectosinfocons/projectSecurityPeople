    package com.SecurityPeople.projectSecurityPeople.service;
    
    import com.SecurityPeople.projectSecurityPeople.dto.ReporteDTO;
    import com.SecurityPeople.projectSecurityPeople.model.Usuario;
    import com.SecurityPeople.projectSecurityPeople.repository.UsuarioRepository;
    import jakarta.transaction.Transactional;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;
    import com.SecurityPeople.projectSecurityPeople.model.Reporte;
    import com.SecurityPeople.projectSecurityPeople.repository.ReporteRepository;
    
    import java.io.IOException;
    import java.time.LocalDateTime;
    import java.time.ZoneId;
    import java.util.ArrayList;
    import java.util.Base64;
    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;

    @Service
    public class ReporteService {
    
        private final ReporteRepository reporteRepository;

        private final UsuarioRepository usuarioRepository;
    
        public ReporteService(ReporteRepository reporteRepository, UsuarioRepository usuarioRepository) {
            this.reporteRepository = reporteRepository;
            this.usuarioRepository = usuarioRepository;
        }


        public Reporte guardarReporte(String descripcion, Double latitud, Double longitud, MultipartFile archivo) throws IOException {

            // 🔹 Buscar el usuario en base de datos (por ID = 28, como ejemplo)
            Long usuarioId = 1L;
            Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);

            if (optionalUsuario.isEmpty()) {
                throw new IllegalArgumentException("No se encontró el usuario con ID " + usuarioId);
            }

            Usuario usuario = optionalUsuario.get(); // Usuario real desde la BD

            String tipoArchivo = determinarTipoArchivo(
                    archivo != null ? archivo.getContentType() : null
            );

            Reporte reporte = new Reporte();
            reporte.setDescripcion(descripcion);
            reporte.setLatitud(latitud);
            reporte.setLongitud(longitud);
            reporte.setTipo(tipoArchivo);
            reporte.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Bogota")));
            reporte.setUsuario(usuario);
            reporte.setArchivo(archivo.getBytes());


            return reporteRepository.save(reporte);
        }
    
        public Reporte obtenerReportePorId(Long id) {
            Optional<Reporte> opt = reporteRepository.findById(id);
            return opt.orElse(null);

        }



        private String determinarTipoArchivo(String contentType) {
            if (contentType == null) return "desconocido";

            if (contentType.startsWith("image/")) {
                return "imagen";
            } else if (contentType.startsWith("video/")) {
                return "video";
            } else {
                return "otro";
            }
        }

        @Transactional
        public List<ReporteDTO> obtenerReportesPorUsuario(Long usuarioId) {
            List<Reporte> reportes = reporteRepository.findAllBasicByUsuarioId(usuarioId);

            return reportes.stream()
                    .map(r -> new ReporteDTO(
                            r.getId(),
                            r.getDescripcion(),
                            r.getLatitud(),
                            r.getLongitud(),
                            r.getFechaRegistro(),
                            r.getUsuario() != null ? r.getUsuario().getId() : null,
                            r.getTipo(),
                            generarUrlDeArchivo(r)
                    )).collect(Collectors.toList());
        }

        private String generarUrlDeArchivo(Reporte r) {
            if ("video".equals(r.getTipo())) {
                return "/api/reportes/archivo/video/" + r.getId();
            }
            return "/api/reportes/archivo/" + r.getId();
        }





    }
