package com.SecurityPeople.projectSecurityPeople.repository;

import com.SecurityPeople.projectSecurityPeople.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByCorreo(String correo);
}
