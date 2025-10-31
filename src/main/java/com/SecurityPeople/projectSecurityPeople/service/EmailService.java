package com.SecurityPeople.projectSecurityPeople.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendSimpleEmail(String toEmail, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("projectinformatic6666@gmail.com");
            message.setTo(toEmail);
            message.setSubject("CUENTA REGISTRADA EXITOSAMENTE");

            // 💬 Aquí agregamos el mensaje al cuerpo del correo
            String fullBody = "✅ Correo creado exitosamente\n\n"
                    + "Detalles del mensaje:\n"
                    + body
                    + "\n\nSaludos,\nProject Informatic";

            message.setText(fullBody);

            mailSender.send(message);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}