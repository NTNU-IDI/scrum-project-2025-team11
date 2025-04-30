package no.ntnu.idatt2106.krisefikser.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
