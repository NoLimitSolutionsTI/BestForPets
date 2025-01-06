package com.bestforpets.Service;

import com.bestforpets.Model.Contacto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class MailService {
    private static final Logger LOGGER = Logger.getLogger(MailService.class.getName());

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String verificationCode, String lastname) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Bienvenido a Best For Pets");

            String htmlContent = "<html>"
                    + "<body style='background-color: #FF7B00; color: #FF7B00; font-family: Arial, sans-serif;'>"
                    + "<div style='padding: 20px; text-align: center;'>"
                    + "<h1 style='color: #000000; font-weight: 800;'>¡Bienvenido/a, " + lastname + "!</h1>"
                    + "<p style='color: #000000;'>Nos complace darte la bienvenida a la familia de <strong style='color: #000000;'>'BestForPets'</strong>.</p>"
                    + "<p style='color: #000000;'>Como parte de nuestra comunidad, estamos encantados de contar contigo para brindar el mejor cuidado a las mascotas.</p>"
                    + "<p style='color: #000000;'>Tu código de verificación es: <strong style='color: #000000;'>" + verificationCode + "</strong></p>"
                    + "<p style='color: #000000;'>Por favor, verifica tu dirección de correo electrónico para completar tu registro y empezar a disfrutar de nuestros servicios.</p>"
                    + "<p style='color: #000000;'>Si tienes alguna pregunta o necesitas asistencia, no dudes en contactarnos.</p>"
                    + "<p style='color: #000000;'>Saludos cordiales,</p>"
                    + "<p style='color: #000000; font-weight: 800;'>El equipo de BestForPets</p>"
                    + "<img src='cid:logoImage' style='width: 500px; height: auto; margin-top: 20px;' />"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);

            ClassPathResource resource = new ClassPathResource("static/Images/BestForPetsLogo.jpeg");
            helper.addInline("logoImage", resource);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendWelcomeMessage(String to, String fullName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Bienvenido a BestForPets");
            String htmlContent = "<html>"
                    + "<body style='background-color: #FF7B00; color: #FF7B00; font-family: Arial, sans-serif;'>"
                    + "<div style='padding: 20px; text-align: center;'>"
                    + "<h1 style='color: #000000; font-weight: 800;'>¡Bienvenido/a, " + fullName + "!</h1>"
                    + "<p style='color: #000000;'>Nos complace darte la bienvenida a <strong>BestForPets</strong>.</p>"
                    + "<p style='color: #000000;'>Gracias por unirte a nuestra comunidad de amantes de mascotas. Estamos seguros de que encontrarás productos y servicios de gran calidad para el cuidado de tus mascotas.</p>"
                    + "<p style='color: #000000;'>Si tienes alguna pregunta o necesitas asistencia, no dudes en contactarnos.</p>"
                    + "<p style='color: #000000;'>Saludos cordiales,</p>"
                    + "<p style='color: #000000; font-weight: 800;'>El equipo de BestForPets</p>"
                    + "<img src='cid:logoImage' style='width: 500px; height: auto; margin-top: 20px;' />"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);

            ClassPathResource resource = new ClassPathResource("static/images/BestForPetsLogo.jpeg");
            helper.addInline("logoImage", resource);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Solicitud de Restablecimiento de Contraseña");

            String resetLink = "http://localhost:8080/reset-password?token=" + token;

            String htmlContent = "<html>"
                    + "<body style='background-color: #FF7B00; color: #000000; font-family: Arial, sans-serif;'>"
                    + "<div style='padding: 20px; text-align: center;'>"
                    + "<h1 style='color: #000000; font-weight: 800;'>Restablece tu Contraseña</h1>"
                    + "<p>Has solicitado restablecer tu contraseña. Por favor, haz clic en el siguiente enlace para crear una nueva contraseña:</p>"
                    + "<p><a href='" + resetLink + "' style='color: #FFFFFF; font-weight: 800;'>Restablecer Contraseña</a></p>"
                    + "<p>Este enlace expirará en 1 hora.</p>"
                    + "<p>Si no has solicitado este cambio, ignora este correo.</p>"
                    + "<p>Saludos cordiales,</p>"
                    + "<p style='color: #000000; font-weight: 800;'>El equipo de BestForPets</p>"
                    + "<img src='cid:logoImage' style='width: 500px; height: auto; margin-top: 20px;' />"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);

            ClassPathResource resource = new ClassPathResource("static/images/BestForPetsLogo.jpeg");
            helper.addInline("logoImage", resource);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendContactEmail(String[] to, Contacto contacto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to); // Envía el correo a múltiples destinatarios
            helper.setSubject("Detalles de Contacto");

            // Crea el contenido del correo con los datos del contacto
            String htmlContent = "<html>"
                    + "<body style='background-color: #FF7B00; color: #000000; font-family: Arial, sans-serif;'>"
                    + "<div style='padding: 20px; text-align: center;'>"
                    + "<h1 style='color: #000000; font-weight: 800;'>Detalles de Contacto</h1>"
                    + "<p>Email: " + contacto.getContactoEmail() + "</p>"
                    + "<p>Teléfono: " + contacto.getContactoTelephone() + "</p>"
                    + "<p>Descripción: " + contacto.getContactoDescription() + "</p>"
                    + "<p>Saludos cordiales,</p>"
                    + "<p style='color: #000000; font-weight: 800;'>El equipo de BestForPets</p>"
                    + "<img src='cid:logoImage' style='width: 500px; height: auto; margin-top: 20px;' />"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);

            // Agrega una imagen inline si es necesario
            ClassPathResource resource = new ClassPathResource("static/images/BestForPetsLogo.jpeg");
            helper.addInline("logoImage", resource);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
