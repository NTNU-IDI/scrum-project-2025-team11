package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.dto.EmailRequest;
import no.ntnu.idatt2106.krisefikser.model.PasswordResetToken;
import no.ntnu.idatt2106.krisefikser.model.TwoFactorCode;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.repository.PasswordResetTokenRepository;
import no.ntnu.idatt2106.krisefikser.repository.TwoFactorCodeRepository;

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
class TwoFactorCodeServiceTest {

    @Mock private TwoFactorCodeRepository codeRepo;
    @Mock private UserService userService;
    @Mock private EmailService emailService;

    @InjectMocks
    private TwoFactorCodeService service;

    @Test
    void initiateCode_generatesCodeAndSendsEmail() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("Alice");

        when(userService.getUserByEmail("user@example.com")).thenReturn(Optional.of(user));

        service.initiateCode("user@example.com");

        verify(codeRepo).deleteByUser(user);
        verify(codeRepo).save(any(TwoFactorCode.class));
        verify(emailService).sendEmail(any(EmailRequest.class));
    }

    @Test
    void completeAuthentication_validCode_validatesUser() {
        User user = new User();
        TwoFactorCode code = new TwoFactorCode();
        code.setUser(user);
        code.setExpiryDate(LocalDateTime.now().plusHours(1));

        when(codeRepo.findByCode("abc123")).thenReturn(Optional.of(code));

        service.completeAuthentication("abc123");

        verify(codeRepo).delete(code);
    }
}