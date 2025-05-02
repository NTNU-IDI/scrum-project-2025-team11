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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;



@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock private PasswordResetTokenRepository tokenRepo;
    @Mock private UserService userService;
    @Mock private EmailService emailService;

    @InjectMocks
    private PasswordResetServiceImpl resetService;

    @Test
    void initiateReset_shouldGenerateAndSendToken() {
        String email = "user@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setFirstName("Jon");

        when(userService.getUserByEmail(email)).thenReturn(Optional.of(mockUser));

        assertDoesNotThrow(() -> resetService.initiateReset(email));

        verify(tokenRepo).deleteByUser(mockUser);
        verify(tokenRepo).save(any(PasswordResetToken.class));
        verify(emailService).sendEmail(any(EmailRequest.class));
    }
}
