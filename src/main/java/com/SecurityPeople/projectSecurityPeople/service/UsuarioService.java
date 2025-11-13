package com.SecurityPeople.projectSecurityPeople.service;

import com.SecurityPeople.projectSecurityPeople.model.Usuario;
import com.SecurityPeople.projectSecurityPeople.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    @Autowired
    private EmailService emailService;

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }


        usuario.setFechaRegistro(LocalDateTime.now(ZoneId.of("America/Bogota")));
        // Encriptar la contraseña antes de guardar
        usuario.setContraseña(bcrypt.encode(usuario.getContraseña()));
        emailService.sendSimpleEmail(usuario.getCorreo(),usuario.getCorreo());
        return usuarioRepository.save(usuario);
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
