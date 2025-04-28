package no.ntnu.idatt2106.krisefikser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PasswordChangeDTO {

    @Schema(example = "OldP@ss!")
    private String currentPassword;

    @Schema(example = "NewP@ss2025")
    private String newPassword;
}

