package no.ntnu.idatt2106.krisefikser.service;

import java.time.Duration;
import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import no.ntnu.idatt2106.krisefikser.dto.EmailRequest;
import no.ntnu.idatt2106.krisefikser.model.TwoFactorCode;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.repository.TwoFactorCodeRepository;

@Service
public class TwoFactorCodeService {
    private static final Duration EXPIRATION = Duration.ofHours(1);

    private final TwoFactorCodeRepository twoFactorRepo;
    private final UserService userService;
    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Autowired
    public TwoFactorCodeService(TwoFactorCodeRepository twoFactorRepo,
                                     UserService userService,
                                     EmailService emailService) {
        this.twoFactorRepo = twoFactorRepo;
        this.userService = userService;
        this.emailService = emailService;
    }

    /**
     * Initiates the two factor authentication by generating a token and sending an email to the user.
     * @param email the email address of the user requesting the password reset
     */
    public void initiateCode(String email) {
        User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("No user with email: " + email));

        // 1. Remove old code
        twoFactorRepo.deleteByUser(user);

        // 2. Generate a short 6-character numeric code 
        String token = generateTwoFactorCode(6);

        // 3. Save code with expiration
        TwoFactorCode prt = TwoFactorCode.builder()
            .user(user)
            .code(token)
            .expiryDate(LocalDateTime.now().plus(EXPIRATION))
            .build();
        twoFactorRepo.save(prt);

        // 4. Send the code via email
        String body = "<p>Hello <strong>" + user.getFirstName() + "</strong>,</p>"
                    + "<p>Your two factor code is:</p>"
                    + "<h2><strong>" + token + "</strong></h2>"
                    + "<p>This code expires in 1 hour.</p>";

        EmailRequest req = new EmailRequest(
            user.getEmail(),
            "Two Factor Code",
            body,
            true
        );

        try {
            emailService.sendEmail(req);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to send two factor code", ex);
        }
}

    /**
     * Generates a random numeric code of the specified length.
     * @param length the length of the code to generate
     * @return a random alphanumeric code
     */
    private String generateTwoFactorCode(int length) {
    String chars = "123456789"; 
    SecureRandom rnd = new SecureRandom();
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
        sb.append(chars.charAt(rnd.nextInt(chars.length())));
    }
    return sb.toString();
}

    /**
     * Completes the two factor process by validating the token.
     * @param token the two factor code to validate
     */
    public void completeAuthentication(String token) {
        TwoFactorCode prt = twoFactorRepo.findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (prt.isExpired()) {
            twoFactorRepo.delete(prt);
            throw new IllegalStateException("Token expired");
        }

        twoFactorRepo.delete(prt);
    }
}
