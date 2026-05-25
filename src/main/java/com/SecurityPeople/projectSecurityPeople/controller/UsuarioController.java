package com.SecurityPeople.projectSecurityPeople.controller;

import com.SecurityPeople.projectSecurityPeople.dto.CambiarPasswordRequest;
import com.SecurityPeople.projectSecurityPeople.dto.RegistroCodigoRequest;
import com.SecurityPeople.projectSecurityPeople.dto.VerificarCodigoRequest;
import com.SecurityPeople.projectSecurityPeople.model.Usuario;
import com.SecurityPeople.projectSecurityPeople.repository.UsuarioRepository;
import com.SecurityPeople.projectSecurityPeople.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // Permite llamadas desde el frontend
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<Usuario> obtenerPerfil(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(token));
    }

    // 🔥 NUEVO: ACTUALIZAR PERFIL
    @PutMapping("/perfil")
    public ResponseEntity<Usuario> actualizarPerfil(
            @RequestHeader("Authorization") String token,
            @RequestBody Usuario usuario) {

        return ResponseEntity.ok(usuarioService.actualizarPerfil(token, usuario));
    }


    @PostMapping("/enviar-codigo")
    public ResponseEntity<?> enviarCodigo(
            @RequestBody Usuario usuario
    ) {

        try {

            usuarioService.enviarCodigoRegistro(usuario);

            return ResponseEntity.ok(
                    "Código enviado al correo"
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =====================================================
    // 🔥 VERIFICAR OTP Y REGISTRAR
    // =====================================================
    @PostMapping("/verificar-codigo")
    public ResponseEntity<?> verificarCodigo(
            @RequestBody RegistroCodigoRequest request
    ) {

        try {

            Usuario usuario =
                    usuarioService
                            .verificarCodigoYRegistrar(
                                    request
                            );

            return ResponseEntity.ok(
                    usuario
            );

        } catch (RuntimeException e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // 🔥 CAMBIAR CONTRASEÑA
/*
    @PutMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(
            @RequestBody CambiarPasswordDTO dto,
            Authentication auth
    ) {

        try {

            String correo = auth.getName(); // viene del JWT

            usuarioService.cambiarPassword(correo, dto);

            return ResponseEntity.ok("Contraseña actualizada correctamente");

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
*/

    @PutMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(
            @RequestBody CambiarPasswordRequest request,
            Authentication auth
    ) {
        String correo = auth.getName();

        usuarioService.cambiarPassword(correo, request);

        return ResponseEntity.ok("Contraseña actualizada");
    }

    @GetMapping("/recuperar")
    public ResponseEntity<?> recuperarContraseña(@RequestParam String correo) {
        try {
            usuarioService.recuperarContraseña(correo);
            return ResponseEntity.ok("Se ha enviado una nueva contraseña al correo ingresado");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
