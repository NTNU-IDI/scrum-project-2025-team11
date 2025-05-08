package no.ntnu.idatt2106.krisefikser.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import no.ntnu.idatt2106.krisefikser.dto.EmailRequest;
import no.ntnu.idatt2106.krisefikser.model.PasswordResetToken;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.repository.PasswordResetTokenRepository;

/**
 * Service class for handling password reset functionality.
 * This class provides methods for initiating and completing password resets.
 */
@Service 
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
    User user = userService.getUserByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("No user with email: " + email));

    // 1. Remove old tokens
    tokenRepo.deleteByUser(user);

    // 2. Generate a short 6-character alphanumeric token
    String token = generateResetCode(6);

    // 3. Save token with expiration
    PasswordResetToken prt = PasswordResetToken.builder()
        .user(user)
        .token(token)
        .expiryDate(LocalDateTime.now().plus(EXPIRATION))
        .build();
    tokenRepo.save(prt);

    // 4. Send the token via email
    String body = "<p>Hello <strong>" + user.getFirstName() + "</strong>,</p>"
                + "<p>Your password reset code is:</p>"
                + "<h2><strong>" + token + "</strong></h2>"
                + "<p>This code expires in 24 hours.</p>";

    EmailRequest req = new EmailRequest(
        user.getEmail(),
        "Password Reset Code",
        body,
        true
    );

    try {
        emailService.sendEmail(req);
    } catch (Exception ex) {
        throw new RuntimeException("Unable to send password reset code", ex);
    }
}

    /**
     * Generates a random alphanumeric code of the specified length.
     * @param length the length of the code to generate
     * @return a random alphanumeric code
     */
    private String generateResetCode(int length) {
    String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // no O, 0, I, 1 for clarity
    SecureRandom rnd = new SecureRandom();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
        sb.append(chars.charAt(rnd.nextInt(chars.length())));
    }
    return sb.toString();
}

    /**
     * Completes the password reset process by validating the token and updating the user's password.
     * @param token the password reset token
     * @param newPassword the new password to set for the user
     */

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
    }
}


