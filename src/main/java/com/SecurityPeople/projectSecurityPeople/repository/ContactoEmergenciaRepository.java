package com.SecurityPeople.projectSecurityPeople.repository;

import com.SecurityPeople.projectSecurityPeople.model.ContactoEmergencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactoEmergenciaRepository extends JpaRepository<ContactoEmergencia, Long> {

    // 🔥 Obtener contactos por usuario
    List<ContactoEmergencia> findByUsuarioId(Long usuarioId);
}