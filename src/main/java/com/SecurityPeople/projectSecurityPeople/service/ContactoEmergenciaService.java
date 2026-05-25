package com.SecurityPeople.projectSecurityPeople.service;

import com.SecurityPeople.projectSecurityPeople.config.JwtTokenUtil;
import com.SecurityPeople.projectSecurityPeople.model.ContactoEmergencia;
import com.SecurityPeople.projectSecurityPeople.model.Usuario;
import com.SecurityPeople.projectSecurityPeople.repository.ContactoEmergenciaRepository;
import com.SecurityPeople.projectSecurityPeople.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactoEmergenciaService {

    private final ContactoEmergenciaRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final JwtTokenUtil jwtTokenUtil;

    public List<ContactoEmergencia> listar(String token) {
        String correo = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        Usuario usuario = usuarioRepo.findByCorreo(correo).get();
        return usuario.getContactosEmergencia();
    }

    public ContactoEmergencia guardar(String token, ContactoEmergencia contacto) {
        String correo = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        Usuario usuario = usuarioRepo.findByCorreo(correo).get();

        contacto.setUsuario(usuario);

        return repo.save(contacto);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }


    // =========================================================
    // 🔥 NUEVO MÉTODO (NO AFECTA AL ORIGINAL)
    // 👉 Se usa SOLO para editar contactos
    // =========================================================
    public ContactoEmergencia actualizarContactoNuevo(Long id, ContactoEmergencia datos) {

        ContactoEmergencia contacto = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado"));

        // 🔥 ACTUALIZA CAMPOS
        contacto.setNombre(datos.getNombre());
        contacto.setTelefono(datos.getTelefono());
        contacto.setCorreo(datos.getCorreo());
        contacto.setRelacion(datos.getRelacion());
        contacto.setApellido(datos.getApellido());
        return repo.save(contacto);
    }

}