package no.ntnu.idatt2106.krisefikser.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.ntnu.idatt2106.krisefikser.dto.TwoFactorConfirmDTO;
import no.ntnu.idatt2106.krisefikser.dto.TwoFactorRequestDTO;
import no.ntnu.idatt2106.krisefikser.service.TwoFactorCodeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TwoFactorController {

    private final TwoFactorCodeService twoFactorCodeService;

    @PostMapping("/request-two-factor-code")
    public ResponseEntity<Void> requestTwoFactorCode(@RequestBody @Valid TwoFactorRequestDTO request) {
        twoFactorCodeService.initiateCode(request.getEmail());
        // Always return 200 to avoid user enumeration
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-authentication")
    public ResponseEntity<Void> confirmAuthentication(@RequestBody @Valid TwoFactorConfirmDTO request) {
        twoFactorCodeService.completeAuthentication(request.getCode());
        return ResponseEntity.ok().build();
    }
}
