package no.ntnu.idatt2106.krisefikser.service;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import no.ntnu.idatt2106.krisefikser.dto.EmailRequest;

@Service
@Profile("dev") // Only active in the "dev" profile
/**
 * Service class for sending emails.
 * This class uses JavaMailSender to send emails.
 * It is currently only active in the "dev" profile.
 */
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends an email based on the provided EmailRequest.
     * Supports both text and HTML emails.
     *
     * @param req the email request details
     * @throws MessagingException if sending an HTML email fails
     */
    public void sendEmail(EmailRequest req) throws MessagingException {
        if (req.isHtml()) {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            // false = not multipart, UTF-8 encoding
            MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                false,
                java.nio.charset.StandardCharsets.UTF_8.name()
            );
            helper.setTo(req.getTo());
            helper.setSubject(req.getSubject());
            helper.setText(req.getBody(), true);
            mailSender.send(mimeMessage);
        } else {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(req.getTo());
            msg.setSubject(req.getSubject());
            msg.setText(req.getBody());
            mailSender.send(msg);
        }
    }
}
