package no.ntnu.idatt2106.krisefikser.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import no.ntnu.idatt2106.krisefikser.model.User;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;  // e.g. https://yourapp.com

    public void sendPasswordResetEmail(User user, String token) {
        String to = user.getEmail();
        String subject = "Password Reset Request";
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        // For simple text email:
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(
            "Hello " + user.getFirstName() + ",\n\n" +
            "To reset your password, click the link below:\n" +
            resetLink + "\n\n" +
            "This link will expire in 24 hours.\n\n" +
            "If you did not request a password reset, just ignore this email."
        );
        mailSender.send(message);
    }

    /**
     * Helper method to send a test email.
     *
     * @param to the recipient's email address
     */
    public void sendTestEmail(String to) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("🎉 Spring Boot Mail Test");
        msg.setText("Woah, The SMTP setup works!");
        mailSender.send(msg);
    }
}
