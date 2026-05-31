package com.SecurityPeople.projectSecurityPeople.service;

import com.SecurityPeople.projectSecurityPeople.config.JwtTokenUtil;
import com.SecurityPeople.projectSecurityPeople.dto.CambiarPasswordRequest;
import com.SecurityPeople.projectSecurityPeople.dto.RegistroCodigoRequest;
import com.SecurityPeople.projectSecurityPeople.dto.VerificarCodigoRequest;
import com.SecurityPeople.projectSecurityPeople.model.Usuario;
import com.SecurityPeople.projectSecurityPeople.repository.UsuarioRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailValidatorService emailValidatorService;

    // =====================================================
    // 🔥 MAIL SENDER
    // =====================================================
    @Autowired
    private  JavaMailSender mailSender;



    private final Map<String, String> codigosOTP =
            new HashMap<>();


    // =====================================================
    // 🔥 NUEVO
    // 👉 memoria temporal códigos OTP
    // =====================================================
    private final Map<String, String> codigosRegistro =
            new HashMap<>();


    private final Map<String, Usuario> usuariosTemporales =
            new HashMap<>();

    // =====================================================
// 🔥 ENVIAR CÓDIGO OTP
// =====================================================
    public void enviarCodigoRegistro(
            Usuario usuario
    ) {

        String correo =
                usuario.getCorreo();

        // =================================================
        // 🔥 VALIDAR CORREO
        // =================================================
        if (
                usuarioRepository
                        .findByCorreo(correo)
                        .isPresent()
        ) {

            throw new RuntimeException(
                    "Este correo ya está registrado"
            );
        }

        // =================================================
        // 🔥 GENERAR OTP
        // =================================================
        String codigo =
                String.valueOf(
                        (int)(
                                Math.random() * 900000
                        ) + 100000
                );

        // =================================================
        // 🔥 GUARDAR OTP
        // =================================================
        codigosRegistro.put(
                correo,
                codigo
        );

        // =================================================
        // 🔥 VER CONSOLA
        // =================================================
        System.out.println("================================");
        System.out.println("OTP GUARDADO");
        System.out.println("Correo: " + correo);
        System.out.println("Código: " + codigo);
        System.out.println("================================");
        String mensajeHtml =

                "<html>" +

                        "<body style='margin:0;padding:0;background-color:#f4f6f9;" +
                        "font-family:Arial,sans-serif;'>" +

                        "<table width='100%' cellpadding='0' cellspacing='0'>" +
                        "<tr>" +
                        "<td align='center'>" +

                        // =================================================
                        // 🔥 CONTENEDOR PRINCIPAL
                        // =================================================

                        "<table width='600' cellpadding='0' cellspacing='0' " +
                        "style='background:#ffffff;margin-top:40px;border-radius:12px;" +
                        "overflow:hidden;box-shadow:0 4px 12px rgba(0,0,0,0.1);'>" +

                        // =================================================
                        // 🔥 HEADER
                        // =================================================

                        "<tr>" +
                        "<td style='background:#D32F2F;padding:30px;" +
                        "text-align:center;color:white;'>" +

                        "<h1 style='margin:0;font-size:28px;'>" +
                        "Alertas Comunitarias" +
                        "</h1>" +

                        "<p style='margin-top:10px;font-size:16px;'>" +
                        "Código de verificación" +
                        "</p>" +

                        "</td>" +
                        "</tr>" +

                        // =================================================
                        // 🔥 CONTENIDO
                        // =================================================

                        "<tr>" +
                        "<td style='padding:40px;color:#333333;'>" +

                        "<h2 style='color:#D32F2F;margin-top:0;'>" +
                        "🔐 Verificación de cuenta" +
                        "</h2>" +

                        "<p style='font-size:16px;line-height:1.7;'>" +
                        "Hemos recibido una solicitud de verificación para su cuenta. " +
                        "Utilice el siguiente código OTP para completar el proceso de registro." +
                        "</p>" +

                        // =================================================
                        // 🔥 CÓDIGO OTP
                        // =================================================

                        "<div style='margin-top:30px;" +
                        "background:#f8f9fa;" +
                        "padding:25px;" +
                        "border-radius:12px;" +
                        "text-align:center;" +
                        "border:2px dashed #D32F2F;'>" +

                        "<p style='margin:0;font-size:15px;color:#666;'>" +
                        "Código de verificación" +
                        "</p>" +

                        "<h1 style='margin:15px 0;color:#D32F2F;" +
                        "font-size:42px;letter-spacing:8px;'>" +
                        codigo +
                        "</h1>" +

                        "</div>" +

                        // =================================================
                        // 🔥 MENSAJE
                        // =================================================

                        "<p style='font-size:15px;line-height:1.7;margin-top:30px;'>" +
                        "Este código es temporal y debe utilizarse únicamente para " +
                        "confirmar el registro de su cuenta." +
                        "</p>" +

                        // =================================================
                        // 🔥 ALERTA
                        // =================================================

                        "<div style='margin-top:20px;padding:15px;" +
                        "background:#fff3cd;border-left:5px solid #ff9800;" +
                        "border-radius:6px;font-size:14px;color:#856404;'>" +

                        "<b>Importante:</b> " +
                        "No comparta este código con terceros para proteger la seguridad de su cuenta." +

                        "</div>" +

                        "</td>" +
                        "</tr>" +

                        // =================================================
                        // 🔥 FOOTER
                        // =================================================

                        "<tr>" +
                        "<td style='background:#eeeeee;padding:20px;" +
                        "text-align:center;font-size:13px;color:#666666;'>" +

                        "Alertas Comunitarias<br>" +
                        "Sistema Inteligente de Seguridad Ciudadana<br><br>" +

                        "© 2026 Alertas Comunitarias" +

                        "</td>" +
                        "</tr>" +

                        "</table>" +

                        "</td>" +
                        "</tr>" +
                        "</table>" +

                        "</body>" +
                        "</html>";
        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(
                    "projectinformatic6666@gmail.com"
            );

            helper.setTo(correo);

            helper.setSubject(
                    "CODIGO DE VERIFICACION"
            );

            helper.setText(
                    mensajeHtml,
                    true
            );

            mailSender.send(message);

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Error al enviar el correo"
            );
        }
    }

    // =====================================================
    // 🔥 VERIFICAR CÓDIGO Y GUARDAR
    // =====================================================
    // ¿Qué hace?
    //
    // 1. verifica código
    // 2. obtiene usuario temporal
    // 3. encripta password
    // 4. guarda en BD
    // 5. limpia memoria temporal
    // =====================================================



    // =====================================================
// 🔥 VERIFICAR OTP Y REGISTRAR
// =====================================================
    public Usuario verificarCodigoYRegistrar(
            RegistroCodigoRequest request
    ) {

        String correo =
                request.getCorreo();

        String codigoIngresado =
                request.getCodigo();

        String codigoGuardado =
                codigosRegistro.get(correo);

        System.out.println("================================");
        System.out.println("VERIFICANDO OTP");
        System.out.println("Correo: " + correo);
        System.out.println("Código guardado: " + codigoGuardado);
        System.out.println("Código ingresado: " + codigoIngresado);
        System.out.println("================================");

        // =================================================
        // 🔥 VALIDAR EXISTE
        // =================================================
        if (codigoGuardado == null) {

            throw new RuntimeException(
                    "No existe código para este correo"
            );
        }

        // =================================================
        // 🔥 VALIDAR OTP
        // =================================================
        if (
                !codigoGuardado.equals(
                        codigoIngresado
                )
        ) {

            throw new RuntimeException(
                    "Código incorrecto"
            );
        }

        // =================================================
        // 🔥 OBTENER USUARIO
        // =================================================
        Usuario usuario =
                request.getUsuario();

        // =================================================
        // 🔥 ENCRIPTAR PASSWORD
        // =================================================
        usuario.setContraseña(
                bcrypt.encode(
                        usuario.getContraseña()
                )
        );
//        String mensaje =
//                "✅ Correo creado exitosamente\n\n"
//                        + "Detalles del mensaje:\n"
//                        + usuario.getCorreo()
//                        + "\n\nSaludos,\nProject Informatic";
        // =====================================================
// 🔥 MENSAJE HTML PROFESIONAL PARA TESIS
// =====================================================

// =====================================================
// 🔥 MENSAJE HTML PROFESIONAL
// =====================================================

        String mensaje =
                "<html>" +
                        "<body style='margin:0;padding:0;background-color:#f4f6f9;font-family:Arial,sans-serif;'>" +

                        "<table width='100%' cellpadding='0' cellspacing='0'>" +
                        "<tr>" +
                        "<td align='center'>" +

                        // =================================================
                        // 🔥 CONTENEDOR PRINCIPAL
                        // =================================================

                        "<table width='600' cellpadding='0' cellspacing='0' " +
                        "style='background:#ffffff;margin-top:40px;border-radius:12px;" +
                        "overflow:hidden;box-shadow:0 4px 12px rgba(0,0,0,0.1);'>" +

                        // =================================================
                        // 🔥 HEADER
                        // =================================================

                        "<tr>" +
                        "<td style='background:#D32F2F;padding:30px;text-align:center;color:white;'>" +

                        "<h1 style='margin:0;font-size:28px;'>Alertas Comunitarias</h1>" +

                        "<p style='margin-top:10px;font-size:16px;'>" +
                        "Sistema Inteligente de Seguridad Ciudadana" +
                        "</p>" +

                        "</td>" +
                        "</tr>" +

                        // =================================================
                        // 🔥 CONTENIDO
                        // =================================================

                        "<tr>" +
                        "<td style='padding:40px;color:#333333;'>" +

                        "<h2 style='color:#D32F2F;margin-top:0;'>" +
                        "✅ Registro Exitoso" +
                        "</h2>" +

                        "<p style='font-size:16px;line-height:1.6;'>" +
                        "Estimado usuario, su cuenta ha sido registrada correctamente " +
                        "en la plataforma <b>Alertas Comunitarias</b>." +
                        "</p>" +

                        "<table width='100%' cellpadding='10' cellspacing='0' " +
                        "style='margin-top:20px;background:#f8f9fa;border-radius:8px;'>" +

                        "<tr>" +
                        "<td style='font-size:15px;'>" +
                        "<b>Correo registrado:</b><br>" +
                        usuario.getCorreo() +
                        "</td>" +
                        "</tr>" +

                        "</table>" +

                        "<p style='font-size:15px;line-height:1.6;margin-top:25px;'>" +
                        "Ahora podrá acceder a las funcionalidades del sistema, " +
                        "incluyendo reportes de incidentes, alertas cercanas y " +
                        "herramientas de seguridad ciudadana." +
                        "</p>" +

                        // =================================================
                        // 🔥 ALERTA SEGURIDAD
                        // =================================================

                        "<div style='margin-top:25px;padding:15px;" +
                        "background:#fff3cd;border-left:5px solid #ff9800;" +
                        "border-radius:6px;font-size:14px;color:#856404;'>" +

                        "<b>Importante:</b> " +
                        "No comparta sus credenciales con terceros para mantener " +
                        "la seguridad de su cuenta." +

                        "</div>" +

                        "</td>" +
                        "</tr>" +

                        // =================================================
                        // 🔥 FOOTER
                        // =================================================

                        "<tr>" +
                        "<td style='background:#eeeeee;padding:20px;text-align:center;" +
                        "font-size:13px;color:#666666;'>" +

                        "Alertas Comunitarias<br>" +
                        "Sistema Inteligente de Seguridad Ciudadana<br><br>" +

                        "© 2026 Alertas Comunitarias" +

                        "</td>" +
                        "</tr>" +

                        "</table>" +

                        "</td>" +
                        "</tr>" +
                        "</table>" +

                        "</body>" +
                        "</html>";






        // =================================================
        // 🔥 GUARDAR USUARIO
        // =================================================

        // =================================================
// 🔥 VINCULAR CONTACTOS CON USUARIO
// =================================================
        if (usuario.getContactosEmergencia() != null) {

            usuario.getContactosEmergencia().forEach(contacto -> {

                contacto.setUsuario(usuario);

            });
        }

        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        emailService.sendSimpleEmail(usuario.getCorreo(),mensaje);
        // =================================================
        // 🔥 ELIMINAR OTP
        // =================================================
        codigosRegistro.remove(correo);

        return usuarioGuardado;
    }













    // 🔥 REGISTRAR USUARIO CON CONTACTOS DE EMERGENCIA
    public Usuario registrarUsuario(Usuario usuario) {

        // Validar si ya existe el correo
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Fecha automática
        usuario.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Bogota")));

        // Encriptar contraseña
        usuario.setContraseña(bcrypt.encode(usuario.getContraseña()));

        // 🔥 CLAVE: vincular cada contacto con el usuario
        if (usuario.getContactosEmergencia() != null) {

            // Validación (según tu tesis: máximo 3 contactos)
            if (usuario.getContactosEmergencia().size() >= 4) {
                throw new RuntimeException("Máximo 4 contactos de emergencia");
            }

            usuario.getContactosEmergencia().forEach(contacto -> {
                contacto.setUsuario(usuario); // 🔥 relación bidireccional
            });
        }

//        String mensaje =
//                "✅ Correo creado exitosamente\n\n"
//                        + "Detalles del mensaje:\n"
//                        + usuario.getCorreo()
//                        + "\n\nSaludos,\nProject Informatic";
//
////        boolean enviado = emailService.sendSimpleEmail(
////                usuario.getCorreo(),
////                mensaje
////        );
//
//
//
//        boolean correoValido =
//                emailValidatorService.esCorreoValido(
//                        usuario.getCorreo()
//                );
//
//        // =====================================================
//        // 🔥 SI EL CORREO FALLA → NO GUARDA
//        // =====================================================
//        if (!correoValido) {
//
//            throw new RuntimeException(
//                    "No se pudo enviar el correo de confirmación. Verifique el email ingresado."
//            );
//        }
//
//        boolean enviado =
//                emailService.sendSimpleEmail(
//                        usuario.getCorreo(),
//                        mensaje
//                );
//
//// =====================================================
//// 🔥 SI FALLA ENVÍO → NO GUARDA
//// =====================================================
//
//        if (!enviado) {
//
//            throw new RuntimeException(
//                    "No se pudo enviar el correo de confirmación"
//            );
//        }

        // Guardar todo (usuario + contactos)
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Enviar correo
        //emailService.sendSimpleEmail(usuario.getCorreo(), usuario.getCorreo());

        return usuarioGuardado;
    }



    // 🔥 OBTENER PERFIL DESDE TOKEN
    public Usuario obtenerPerfil(String token) {

        String correo = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));

        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // 🔥 ACTUALIZAR PERFIL
    public Usuario actualizarPerfil(String token, Usuario datos) {

        String correo = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 🔥 SOLO ACTUALIZA CAMPOS EDITABLES
        usuario.setNombre(datos.getNombre());
        usuario.setApellido(datos.getApellido());
        usuario.setCorreo(datos.getCorreo());
        // 🔐 🔥 SOLUCIÓN FINAL CONTRASEÑA
        if (datos.getContraseña() != null && !datos.getContraseña().trim().isEmpty()) {

            // Solo si viene algo → actualiza
            usuario.setContraseña(bcrypt.encode(datos.getContraseña()));

        }
        return usuarioRepository.save(usuario);
    }



    public void cambiarPassword(String correo, CambiarPasswordRequest request) {

        // 🔴 VALIDACIÓN CRÍTICA (AQUÍ ESTÁ LA CLAVE)
        if (request.getPasswordNueva() == null || request.getPasswordNueva().trim().isEmpty()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }

        if (request.getPasswordNueva().length() < 6) {
            throw new RuntimeException("La contraseña debe tener mínimo 6 caracteres");
        }

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setContraseña(bcrypt.encode(request.getPasswordNueva()));
        usuarioRepository.save(usuario);
    }
    public void recuperarContraseña(String correo) {

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo no registrado"));

        // Generar contraseña temporal
        String nuevaClave = "TEMP" + (int)(Math.random() * 9000 + 1000);

        // Encriptar nueva contraseña
        usuario.setContraseña(bcrypt.encode(nuevaClave));
        usuarioRepository.save(usuario);

        // Enviar correo
//        String mensaje = "Tu nueva contraseña temporal es: " + nuevaClave +
//                "\nPor favor cámbiala después de iniciar sesión.";



        String mensaje =

                "<html>" +

                        "<body style='margin:0;padding:0;background-color:#f4f6f9;" +
                        "font-family:Arial,sans-serif;'>" +

                        "<table width='100%' cellpadding='0' cellspacing='0'>" +
                        "<tr>" +
                        "<td align='center'>" +

                        // =================================================
                        // 🔥 CONTENEDOR
                        // =================================================

                        "<table width='600' cellpadding='0' cellspacing='0' " +
                        "style='background:#ffffff;margin-top:40px;border-radius:12px;" +
                        "overflow:hidden;box-shadow:0 4px 12px rgba(0,0,0,0.1);'>" +

                        // =================================================
                        // 🔥 HEADER
                        // =================================================

                        "<tr>" +
                        "<td style='background:#D32F2F;padding:30px;" +
                        "text-align:center;color:white;'>" +

                        "<h1 style='margin:0;font-size:28px;'>" +
                        "Alertas Comunitarias" +
                        "</h1>" +

                        "<p style='margin-top:10px;font-size:16px;'>" +
                        "Recuperación de contraseña" +
                        "</p>" +

                        "</td>" +
                        "</tr>" +

                        // =================================================
                        // 🔥 CONTENIDO
                        // =================================================

                        "<tr>" +
                        "<td style='padding:40px;color:#333333;'>" +

                        "<h2 style='color:#D32F2F;margin-top:0;'>" +
                        "🔐 Cambio de contraseña" +
                        "</h2>" +

                        "<p style='font-size:16px;line-height:1.6;'>" +
                        "Se ha generado una contraseña temporal para su cuenta." +
                        "</p>" +

                        // =================================================
                        // 🔥 PASSWORD TEMPORAL
                        // =================================================

                        "<div style='margin-top:25px;" +
                        "background:#f8f9fa;" +
                        "padding:20px;" +
                        "border-radius:10px;" +
                        "text-align:center;" +
                        "border:2px dashed #D32F2F;'>" +

                        "<p style='margin:0;font-size:15px;color:#666;'>" +
                        "Contraseña temporal" +
                        "</p>" +

                        "<h1 style='margin:10px 0;color:#D32F2F;" +
                        "letter-spacing:3px;'>" +
                        nuevaClave +
                        "</h1>" +

                        "</div>" +

                        // =================================================
                        // 🔥 MENSAJE
                        // =================================================

                        "<p style='font-size:15px;line-height:1.7;margin-top:25px;'>" +
                        "Por seguridad, le recomendamos cambiar esta contraseña " +
                        "después de iniciar sesión en la aplicación." +
                        "</p>" +

                        // =================================================
                        // 🔥 ALERTA
                        // =================================================

                        "<div style='margin-top:20px;padding:15px;" +
                        "background:#fff3cd;border-left:5px solid #ff9800;" +
                        "border-radius:6px;font-size:14px;color:#856404;'>" +

                        "<b>Importante:</b> " +
                        "No comparta esta contraseña con terceros." +

                        "</div>" +

                        "</td>" +
                        "</tr>" +

                        // =================================================
                        // 🔥 FOOTER
                        // =================================================

                        "<tr>" +
                        "<td style='background:#eeeeee;padding:20px;" +
                        "text-align:center;font-size:13px;color:#666666;'>" +

                        "Alertas Comunitarias<br>" +
                        "Sistema Inteligente de Seguridad Ciudadana<br><br>" +

                        "© 2026 Alertas Comunitarias" +

                        "</td>" +
                        "</tr>" +

                        "</table>" +

                        "</td>" +
                        "</tr>" +
                        "</table>" +

                        "</body>" +
                        "</html>";

        boolean enviado = emailService.sendSimpleEmailUpdatePassword(correo, mensaje);

        if (!enviado) {
            throw new RuntimeException("Hubo un error al enviar el correo");
        }
    }

}
