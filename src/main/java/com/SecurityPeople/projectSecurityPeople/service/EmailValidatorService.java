package com.SecurityPeople.projectSecurityPeople.service;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;

@Service
public class EmailValidatorService {

    // =====================================================
    // 🔥 VALIDAR CORREO
    // =====================================================
    // ¿Qué hace?
    //
    // 1. Valida formato:
    //      usuario@gmail.com
    //
    // 2. Valida dominio:
    //      gmail.com existe
    //
    // Si falla:
    //      retorna false
    // =====================================================

    public boolean esCorreoValido(String correo) {

        // =================================================
        // 🔥 VALIDAR FORMATO
        // =================================================
        boolean formatoValido =
                EmailValidator.getInstance()
                        .isValid(correo);

        if (!formatoValido) {

            return false;
        }

        // =================================================
        // 🔥 OBTENER DOMINIO
        // =================================================
        String dominio =
                correo.substring(
                        correo.indexOf("@") + 1
                );

        // =================================================
        // 🔥 VALIDAR DNS DEL DOMINIO
        // =================================================
        try {

            Hashtable<String, String> env =
                    new Hashtable<>();

            env.put(
                    "java.naming.factory.initial",
                    "com.sun.jndi.dns.DnsContextFactory"
            );

            DirContext dirContext =
                    new InitialDirContext(env);

            Attributes attrs =
                    dirContext.getAttributes(
                            dominio,
                            new String[]{"MX"}
                    );

            Attribute attr = attrs.get("MX");

            return attr != null;

        } catch (NamingException e) {

            return false;
        }
    }
}