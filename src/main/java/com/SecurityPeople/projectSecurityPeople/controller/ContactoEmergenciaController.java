package com.SecurityPeople.projectSecurityPeople.controller;

import com.SecurityPeople.projectSecurityPeople.model.ContactoEmergencia;
import com.SecurityPeople.projectSecurityPeople.service.ContactoEmergenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contactos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ContactoEmergenciaController {

    private final ContactoEmergenciaService contactoEmergenciaService;

    // 🔥 LISTAR CONTACTOS
    @GetMapping
    public List<ContactoEmergencia> listar(@RequestHeader("Authorization") String token) {
        return contactoEmergenciaService.listar(token);
    }

    // 🔥 AGREGAR CONTACTO
    @PostMapping
    public ContactoEmergencia guardar(
            @RequestHeader("Authorization") String token,
            @RequestBody ContactoEmergencia contacto) {

        return contactoEmergenciaService.guardar(token, contacto);
    }

    // =========================================================
    // 🔥 NUEVO ENDPOINT (NO INTERFIERE)
    // 👉 Ruta diferente: /actualizar/{id}
    // 👉 NO usa el mismo mapping que DELETE
    // =========================================================
    @PutMapping("/actualizar/{id}")
    public ContactoEmergencia actualizarContactoNuevo(
            @PathVariable Long id,
            @RequestBody ContactoEmergencia contacto) {

        return contactoEmergenciaService.actualizarContactoNuevo(id, contacto);
    }


    // 🔥 ELIMINAR CONTACTO
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        contactoEmergenciaService.eliminar(id);
    }
}