package org.ftm.api.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.mail.from-name}")
    private String fromName;

    @Value("${app.mail.frontend-url}")
    private String frontendUrl;

    @Override
    public void sendWelcomeEmail(
            String email,
            String firstname,
            String lastname,
            String temporaryPassword
    ) {

        try {
            Context context = new Context();
            context.setVariable("firstname", firstname);
            context.setVariable("lastname", lastname);
            context.setVariable("email", email);
            context.setVariable("temporaryPassword", temporaryPassword);
            context.setVariable("frontendUrl", frontendUrl);

            String htmlContent = templateEngine.process("email/welcome-account", context);

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(from, fromName);
            helper.setTo(email);
            helper.setSubject("Bienvenue sur la plateforme FTM");
            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException exception) {
            throw new IllegalStateException( "Impossible de créer l'email de bienvenue " + "pour : " + email, exception);
        } catch (Exception exception) {
            throw new IllegalStateException( "Impossible d'envoyer l'email de bienvenue " + "pour : " + email, exception);
        }

    }
}
