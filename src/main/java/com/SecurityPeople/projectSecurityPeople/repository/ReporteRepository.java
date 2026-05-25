package com.SecurityPeople.projectSecurityPeople.repository;

import com.SecurityPeople.projectSecurityPeople.dto.ReporteDTO;
import com.SecurityPeople.projectSecurityPeople.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    // ✅ Solo se cargan los campos necesarios, no el BLOB
    @Query("SELECT r FROM Reporte r JOIN FETCH r.usuario WHERE r.usuario.id = :usuarioId")
    List<Reporte> findAllBasicByUsuarioId(@Param("usuarioId") Long usuarioId);


    // =========================================================
    // 🔥 INICIO CAMBIO: TRAER SOLO LO NECESARIO (SIN ARCHIVO)
    // =========================================================
    @Query("SELECT new com.SecurityPeople.projectSecurityPeople.dto.ReporteDTO(" +
            "r.id, r.descripcion, r.latitud, r.longitud, r.fechaRegistro, " +
            "r.usuario.id, r.tipo, null, r.tiporeporte) " +
            "FROM Reporte r WHERE r.usuario.id = :id")
    List<ReporteDTO> findReportesSinArchivo(@Param("id") Long id);
    // =========================================================
    // 🔥 FIN CAMBIO
    // =========================================================
}
