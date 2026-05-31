package com.SecurityPeople.projectSecurityPeople.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

//    public boolean sendSimpleEmail(String toEmail, String body) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom("projectinformatic6666@gmail.com");
//            message.setTo(toEmail);
//            message.setSubject("CUENTA REGISTRADA EXITOSAMENTE");
//
//
//
//            message.setText(body);
//
//            mailSender.send(message);
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }







    // =====================================================
    // 🔥 ENVIAR CORREO HTML
    // =====================================================

    public boolean sendSimpleEmail(String toEmail, String body) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(
                    "projectinformatic6666@gmail.com"
            );

            helper.setTo(toEmail);

            helper.setSubject(
                    "CUENTA REGISTRADA EXITOSAMENTE"
            );

            // =================================================
            // 🔥 TRUE = HTML
            // =================================================

            helper.setText(body, true);

            mailSender.send(message);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }











    public boolean sendSimpleEmailUpdatePassword(String toEmail, String body) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(
                    "projectinformatic6666@gmail.com"
            );

            helper.setTo(toEmail);

            helper.setSubject(
                    "RECUPERACIÓN DE CONTRASEÑA"
            );

            helper.setText(body, true);

            mailSender.send(message);

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}