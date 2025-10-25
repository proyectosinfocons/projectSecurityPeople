package com.SecurityPeople.projectSecurityPeople.repository;

import com.SecurityPeople.projectSecurityPeople.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    // Aquí puedes agregar consultas personalizadas si lo necesitas en el futuro
    // Por ejemplo, buscar reportes por ubicación, fecha, etc.
}