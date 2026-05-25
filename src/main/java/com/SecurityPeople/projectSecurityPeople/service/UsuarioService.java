package com.SecurityPeople.projectSecurityPeople.service;

import com.SecurityPeople.projectSecurityPeople.config.JwtTokenUtil;
import com.SecurityPeople.projectSecurityPeople.dto.CambiarPasswordRequest;
import com.SecurityPeople.projectSecurityPeople.dto.RegistroCodigoRequest;
import com.SecurityPeople.projectSecurityPeople.dto.VerificarCodigoRequest;
import com.SecurityPeople.projectSecurityPeople.model.Usuario;
import com.SecurityPeople.projectSecurityPeople.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

        // =================================================
        // 🔥 ENVIAR EMAIL
        // =================================================
        SimpleMailMessage mensaje =
                new SimpleMailMessage();

        mensaje.setTo(correo);

        mensaje.setSubject(
                "Código de verificación"
        );

        mensaje.setText(
                "Tu código de verificación es: "
                        + codigo
        );

        mailSender.send(mensaje);
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
        String mensaje =
                "✅ Correo creado exitosamente\n\n"
                        + "Detalles del mensaje:\n"
                        + usuario.getCorreo()
                        + "\n\nSaludos,\nProject Informatic";
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
        String mensaje = "Tu nueva contraseña temporal es: " + nuevaClave +
                "\nPor favor cámbiala después de iniciar sesión.";

        boolean enviado = emailService.sendSimpleEmail(correo, mensaje);

        if (!enviado) {
            throw new RuntimeException("Hubo un error al enviar el correo");
        }
    }

}
