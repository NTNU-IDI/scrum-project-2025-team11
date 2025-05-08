package no.ntnu.idatt2106.krisefikser.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import no.ntnu.idatt2106.krisefikser.dto.EmailRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdResponseDTO;
import no.ntnu.idatt2106.krisefikser.mapper.HouseholdMapper;
import no.ntnu.idatt2106.krisefikser.model.Household;
import no.ntnu.idatt2106.krisefikser.model.HouseholdInviteCode;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.repository.HouseholdInviteCodeRepository;
import no.ntnu.idatt2106.krisefikser.repository.UserRepository;

@Service
@Transactional
public class HouseholdInviteCodeService {
    private static final Duration EXPIRATION = Duration.ofHours(1);

    private final HouseholdInviteCodeRepository houseCodeRepo;
    private final HouseholdService householdService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Autowired
    public HouseholdInviteCodeService(HouseholdInviteCodeRepository houseCodeRepo,
                                     HouseholdService householdService,
                                     EmailService emailService, UserRepository userRepository) {
        this.houseCodeRepo = houseCodeRepo;
        this.householdService = householdService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    /**
     * Initiates the household invite code by generating a token and sending an email to a user.
     * @param email the email address of the user you want to send the code to 
     */
    public void initiateCode(String email, int householdId) {

        Household household = householdService.findById(householdId)
            .orElseThrow(() -> new IllegalArgumentException("No household with ID: " + householdId));

        // 1. Remove old code
        houseCodeRepo.deleteByHousehold(household);

        // 2. Generate a short 6-character numeric code 
        String token = generateHouseholdInviteCode(6);

        // 3. Save code with expiration
        HouseholdInviteCode prt = HouseholdInviteCode.builder()
            .household(household)
            .code(token)
            .expiryDate(LocalDateTime.now().plus(EXPIRATION))
            .build();
        houseCodeRepo.save(prt);

        // 4. Send the code via email
        String body = "<p>Hello</p>"
                    + "<p>You have been invited to </p> <strong>" + household.getName() + "</strong>"
                    + "<p>Use the code below to accept the invitation:</p>"
                    + "<h2><strong>" + token + "</strong></h2>"
                    + "<p>This code expires in 1 hour.</p>";

        EmailRequest req = new EmailRequest(
            email,
            "Household Invite Code",
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
    private String generateHouseholdInviteCode(int length) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // no O, 0, I, 1 for clarity
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length); for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }


    public HouseholdResponseDTO consumeInviteCode(String code) {
        HouseholdInviteCode row = houseCodeRepo.findByCode(code)
            .orElseThrow(() -> new IllegalArgumentException("Invalid code"));
    
        if (row.isExpired()) {
            houseCodeRepo.delete(row);
            throw new IllegalStateException("Code expired");
        }
    
        Household household = row.getHousehold();
        houseCodeRepo.delete(row);          // one-time use
        List<User> members = userRepository.findByHousehold_Id(household.getId());
        return HouseholdMapper.toResponseDTO(household, members);
    }
}
