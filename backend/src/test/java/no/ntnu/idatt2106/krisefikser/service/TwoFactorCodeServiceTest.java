package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.dto.EmailRequest;
import no.ntnu.idatt2106.krisefikser.dto.LoginRequest;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.TwoFactorCode;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.repository.TwoFactorCodeRepository;
import no.ntnu.idatt2106.krisefikser.repository.UserRepository;

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

    @Mock 
    private TwoFactorCodeRepository codeRepo;
    @Mock 
    private UserService userService;
    @Mock 
    private EmailService emailService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TwoFactorCodeService service;

    @Test
    void initiateCode_generatesCodeAndSendsEmail() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setUsername("HansiBoy__oo");
        user.setFirstName("Hans");
        user.setLastName("Meling");
        user.setPassword("password#123");

        Household household = new Household();
        household.setId(1);
        household.setName("Household 1");
        household.setMemberCount(1);
        user.setHousehold(household);

        when(userService.getUserByUsername("HansiBoy__oo"))
                .thenReturn(Optional.of(user));

        service.initiateCode("HansiBoy__oo");

        verify(codeRepo).deleteByUser(user);
        verify(codeRepo).save(any(TwoFactorCode.class));
        verify(emailService).sendEmail(any(EmailRequest.class));
    }


    @Test
    void completeAuthentication_validCode_validatesUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setUsername("HansiBoy__oo");
        user.setFirstName("Hans");
        user.setLastName("Meling");
        user.setPassword("password#123");

        Household household = new Household();
        household.setId(1);
        household.setName("Household 1");
        household.setMemberCount(1);
        user.setHousehold(household);
        
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(user.getPassword());
        TwoFactorCode code = new TwoFactorCode();
        code.setUser(user);
        code.setExpiryDate(LocalDateTime.now().plusHours(1));

        when(codeRepo.findByCode("abc123")).thenReturn(Optional.of(code));

        service.completeAuthentication("abc123", loginRequest);

        verify(codeRepo).delete(code);
    }
}