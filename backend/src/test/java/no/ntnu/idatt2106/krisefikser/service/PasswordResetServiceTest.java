package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.dto.EmailRequest;
import no.ntnu.idatt2106.krisefikser.model.PasswordResetToken;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.repository.PasswordResetTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.time.LocalDateTime;



@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock private PasswordResetTokenRepository tokenRepo;
    @Mock private UserService userService;
    @Mock private EmailService emailService;

    @InjectMocks
    private PasswordResetServiceImpl service;

    @Test
    void initiateReset_generatesTokenAndSendsEmail() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("Alice");

        when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));

        service.initiateReset("user@example.com");

        verify(tokenRepo).deleteByUser(user);
        verify(tokenRepo).save(any(PasswordResetToken.class));
        verify(emailService).sendEmail(any(EmailRequest.class));
    }

    @Test
    void completeReset_validToken_updatesPassword() {
        User user = new User();
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(1));

        when(tokenRepo.findByToken("abc123")).thenReturn(Optional.of(token));

        service.completeReset("abc123", "newPassword");

        verify(userService).updatePassword(user, "newPassword");
        verify(tokenRepo).delete(token);
    }
}
