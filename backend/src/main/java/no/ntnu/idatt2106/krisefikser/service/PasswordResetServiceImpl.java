package no.ntnu.idatt2106.krisefikser.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

import no.ntnu.idatt2106.krisefikser.dto.EmailRequest;
import no.ntnu.idatt2106.krisefikser.model.PasswordResetToken;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.repository.PasswordResetTokenRepository;
import no.ntnu.idatt2106.krisefikser.service.PasswordResetService;
import no.ntnu.idatt2106.krisefikser.service.UserService;

/**
 * Service class for handling password reset functionality.
 * This class provides methods for initiating and completing password resets.
 * It is only active in the "dev" profile.
 */
@Service
@Profile("dev") 
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final Duration EXPIRATION = Duration.ofHours(24);

    private final PasswordResetTokenRepository tokenRepo;
    private final UserService userService;
    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Autowired
    public PasswordResetServiceImpl(PasswordResetTokenRepository tokenRepo,
                                     UserService userService,
                                     EmailService emailService) {
        this.tokenRepo    = tokenRepo;
        this.userService  = userService;
        this.emailService = emailService;
    }

    /**
     * Initiates the password reset process by generating a token and sending an email to the user.
     * @param email the email address of the user requesting the password reset
     */
    @Override
    public void initiateReset(String email) {
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("No user with email: " + email));

        // 1. clean up old tokens
        tokenRepo.deleteByUser(user);

        // 2. generate new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken prt = PasswordResetToken.builder()
            .user(user)
            .token(token)
            .expiryDate(LocalDateTime.now().plus(EXPIRATION))
            .build();
        tokenRepo.save(prt);

        // 3. build reset link
        String link = frontendUrl + "/reset-password?token=" + token;
        String body = "<p>Hello " + user.getFirstName() + ",</p>"
                    + "<p>Click <a href=\"" + link + "\">here</a> to reset your password.</p>"
                    + "<p>This link expires in 24 hours.</p>";

        // 4. send the email
        EmailRequest req = new EmailRequest(
            user.getEmail(),
            "Reset your password",
            body,
            true
        );
        try {
            emailService.sendEmail(req);
        } catch (MessagingException ex) {
            throw new RuntimeException("Unable to send password reset email", ex);
        }
    }

    /**
     * Completes the password reset process by validating the token and updating the user's password.
     * @param token the password reset token
     * @param newPassword the new password to set for the user
     */

    /**  
    @Override
    public void completeReset(String token, String newPassword) {
        PasswordResetToken prt = tokenRepo.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (prt.isExpired()) {
            tokenRepo.delete(prt);
            throw new IllegalStateException("Token expired");
        }

        User user = prt.getUser();
        userService.updatePassword(user, newPassword);
        tokenRepo.delete(prt);
    }*/
}


