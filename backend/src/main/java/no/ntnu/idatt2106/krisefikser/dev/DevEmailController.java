package no.ntnu.idatt2106.krisefikser.dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import no.ntnu.idatt2106.krisefikser.service.EmailService;

/**
 * Controller for sending test emails.
 * This controller is only active in the "dev" profile.
 * It provides an endpoint to send a test email to a specified recipient.
 */
@Profile("dev")
@RestController
@RequestMapping("/test")
public class DevEmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<String> sendTestEmail(@RequestParam String to) {
        // you can add an overload in EmailService for a generic test message:
        emailService.sendTestEmail(to);
        return ResponseEntity.ok("Test email sent to " + to);
    }
}
