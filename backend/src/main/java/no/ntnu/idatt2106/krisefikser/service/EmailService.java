package no.ntnu.idatt2106.krisefikser.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;

import no.ntnu.idatt2106.krisefikser.dto.EmailRequest;


@Service
/**
 * Service class for sending emails.
 * This class uses JavaMailSender to send emails.
 * It is currently only active in the "dev" profile.
 */
public class EmailService {

    private final JavaMailSender mailSender;

    private final String from;

    @Autowired
    public EmailService(JavaMailSender mailSender,
                        @Value("${spring.mail.from}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    /**
     * Sends an email based on the provided EmailRequest.
     * Supports both text and HTML emails.
     *
     * @param req the email request details
     * @throws MessagingException if sending an HTML email fails
     */
    public void sendEmail(EmailRequest req) {
        try {
            if (req.isHtml()) {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    false,
                    java.nio.charset.StandardCharsets.UTF_8.name()
                );
                helper.setFrom(from);
                helper.setTo(req.getTo());
                helper.setSubject(req.getSubject());
                helper.setText(req.getBody(), true);
                mailSender.send(mimeMessage);
            } else {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom(from);      
                msg.setTo(req.getTo());
                msg.setSubject(req.getSubject());
                msg.setText(req.getBody());
                mailSender.send(msg);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
    
}
