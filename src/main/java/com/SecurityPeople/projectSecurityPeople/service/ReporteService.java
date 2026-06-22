    package com.SecurityPeople.projectSecurityPeople.service;
    
    import com.SecurityPeople.projectSecurityPeople.config.JwtTokenUtil;
    import com.SecurityPeople.projectSecurityPeople.dto.ReporteDTO;
    import com.SecurityPeople.projectSecurityPeople.model.Usuario;
    import com.SecurityPeople.projectSecurityPeople.repository.UsuarioRepository;
    import jakarta.transaction.Transactional;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;
    import com.SecurityPeople.projectSecurityPeople.model.Reporte;
    import com.SecurityPeople.projectSecurityPeople.repository.ReporteRepository;
    
    import java.io.IOException;
    import java.time.LocalDate;
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

        private final JwtTokenUtil jwtTokenUtil;

        public ReporteService(ReporteRepository reporteRepository, UsuarioRepository usuarioRepository,JwtTokenUtil jwtTokenUtil) {
            this.reporteRepository = reporteRepository;
            this.usuarioRepository = usuarioRepository;
            this.jwtTokenUtil=jwtTokenUtil;
        }






        // =========================================================
        // 🔥 INICIO CAMBIO: MÉTODO CON TOKEN (BOTÓN PÁNICO)
        // =========================================================
        public Reporte guardarReporteConToken(
                String token,
                String descripcion,
                Double latitud,
                Double longitud,
                MultipartFile archivo
        ) throws IOException {

            // 🔴 1. OBTENER CORREO DESDE TOKEN
            String correo = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));

            // 🔴 2. BUSCAR USUARIO REAL
            Usuario usuario = usuarioRepository.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 🔴 3. CREAR REPORTE (SIN IMAGEN)
            Reporte reporte = new Reporte();
            reporte.setDescripcion(descripcion);
            reporte.setLatitud(latitud);
            reporte.setLongitud(longitud);
            reporte.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Bogota")));
            reporte.setUsuario(usuario);
            reporte.setTiporeporte("BOTON DE PANICO");
            // 🔴 NO GUARDAMOS ARCHIVO (botón pánico)
            reporte.setArchivo(null);
            reporte.setTipo("No existe archivo");

            return reporteRepository.save(reporte);
        }
        // =========================================================
        // 🔥 FIN CAMBIO
        // =========================================================







        // =========================================================
        // 🔥 INICIO CAMBIO PRINCIPAL
        // 👉 SOLUCIONA ERROR LOB + OPTIMIZA CONSULTA
        // =========================================================
        @Transactional
        public List<ReporteDTO> obtenerReportesDesdeToken(String token) {

            // 🔴 1. Extraer correo del token
            String correo = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));

            // 🔴 2. Buscar usuario
            Usuario usuario = usuarioRepository.findByCorreo(correo)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // 🔴 3. Traer reportes SIN archivo (🔥 CLAVE)
            return reporteRepository.findReportesSinArchivo(usuario.getId());
        }
        // =========================================================
        // 🔥 FIN CAMBIO
        // =========================================================


        public Reporte guardarReporte(String descripcion, Double latitud, Double longitud, MultipartFile archivo,String token) throws IOException {

            // 🔴 1. OBTENER CORREO DESDE TOKEN
            String correo = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));

            // 🔹 Buscar el usuario en base de datos (por ID = 28, como ejemplo)
            //Long usuarioId = 1L;
            Optional<Usuario> optionalUsuario = usuarioRepository.findByCorreo(correo);

            if (optionalUsuario.isEmpty()) {
                throw new IllegalArgumentException("No se encontró el usuario con ID " + optionalUsuario.get().getId());
            }

            Usuario usuario = optionalUsuario.get(); // Usuario real desde la BD

            String tipoArchivo = determinarTipoArchivo();

            Reporte reporte = new Reporte();
            reporte.setDescripcion(descripcion);
            reporte.setLatitud(latitud);
            reporte.setLongitud(longitud);
            reporte.setTipo(tipoArchivo);
            reporte.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Bogota")));
            reporte.setUsuario(usuario);
            reporte.setArchivo(archivo.getBytes());
            reporte.setTiporeporte("ROBO");

            return reporteRepository.save(reporte);
        }
    
        public Reporte obtenerReportePorId(Long id) {
            Optional<Reporte> opt = reporteRepository.findById(id);
            return opt.orElse(null);

        }


        // =========================================================
// 🔥 NUEVO: LISTAR TODOS LOS REPORTES
// =========================================================
        public List<ReporteDTO> obtenerTodosLosReportes() {

            List<Reporte> reportes = reporteRepository.findAll();

            return reportes.stream()
                    .map(r -> new ReporteDTO(
                            r.getId(),
                            r.getDescripcion(),
                            r.getLatitud(),
                            r.getLongitud(),
                            r.getFechaRegistro(),
                            r.getUsuario() != null ? r.getUsuario().getId() : null,
                            r.getTipo(),
                            generarUrlDeArchivo(r),
                            r.getTiporeporte()
                    )).collect(Collectors.toList());
        }



        private String determinarTipoArchivo() {
//            if (contentType == null) return "desconocido";
//
//            if (contentType.startsWith("image/")) {
                return "imagen";
//            } else if (contentType.startsWith("video/")) {
//                return "video";
//            } else {
//                return "otro";
//            }
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
                            generarUrlDeArchivo(r),
                            r.getTiporeporte()
                    )).collect(Collectors.toList());
        }

        private String generarUrlDeArchivo(Reporte r) {
            if ("video".equals(r.getTipo())) {
                return "/api/reportes/archivo/video/" + r.getId();
            }
            return "/api/reportes/archivo/" + r.getId();
        }








        @Transactional
        public List<ReporteDTO> filtrarPorFecha(
                String fechaInicio,
                String fechaFin
        ) {

            LocalDateTime inicio =
                    LocalDate.parse(fechaInicio)
                            .atStartOfDay();

            LocalDateTime fin =
                    LocalDate.parse(fechaFin)
                            .atTime(23,59,59);

            List<Reporte> reportes =
                    reporteRepository.buscarPorFechas(
                            inicio,
                            fin
                    );

            return reportes.stream()
                    .map(r -> new ReporteDTO(
                            r.getId(),
                            r.getDescripcion(),
                            r.getLatitud(),
                            r.getLongitud(),
                            r.getFechaRegistro(),
                            r.getUsuario() != null
                                    ? r.getUsuario().getId()
                                    : null,
                            r.getTipo(),
                            generarUrlDeArchivo(r),
                            r.getTiporeporte()
                    ))
                    .collect(Collectors.toList());
        }



        @Transactional
        public List<ReporteDTO> filtrarMisReportes(
                String token,
                String fechaInicio,
                String fechaFin
        ) {

            String correo =
                    jwtTokenUtil.getUsernameFromToken(
                            token.replace("Bearer ", "")
                    );

            Usuario usuario =
                    usuarioRepository.findByCorreo(correo)
                            .orElseThrow(() ->
                                    new RuntimeException("Usuario no encontrado"));

            LocalDateTime inicio =
                    LocalDate.parse(fechaInicio)
                            .atStartOfDay();

            LocalDateTime fin =
                    LocalDate.parse(fechaFin)
                            .atTime(23,59,59);

            return reporteRepository.findReportesSinArchivoPorFechas(
                    usuario.getId(),
                    inicio,
                    fin
            );
        }

    }
