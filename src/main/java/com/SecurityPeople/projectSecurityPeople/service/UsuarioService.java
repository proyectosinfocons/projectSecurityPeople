package com.SecurityPeople.projectSecurityPeople.service;

import com.SecurityPeople.projectSecurityPeople.model.Usuario;
import com.SecurityPeople.projectSecurityPeople.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bcrypt;

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // Encriptar la contraseña antes de guardar
        usuario.setContraseña(bcrypt.encode(usuario.getContraseña()));

        return usuarioRepository.save(usuario);
    }
}
