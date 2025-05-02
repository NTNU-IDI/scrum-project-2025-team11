package no.ntnu.idatt2106.krisefikser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.idatt2106.krisefikser.dto.PasswordResetConfirm;
import no.ntnu.idatt2106.krisefikser.dto.PasswordResetRequest;
import no.ntnu.idatt2106.krisefikser.service.PasswordResetService;
import no.ntnu.idatt2106.krisefikser.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(PasswordResetController.class)
class PasswordResetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PasswordResetService passwordResetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void requestPasswordReset_ShouldReturnOk() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest("test@example.com");

        doNothing().when(passwordResetService).initiateReset(request.getEmail());

        mockMvc.perform(post("/api/auth/request-password-reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword_ShouldReturnOk() throws Exception {
        PasswordResetConfirm confirm = new PasswordResetConfirm("ABC123", "newPassword!");

        doNothing().when(passwordResetService).completeReset(confirm.getToken(), confirm.getNewPassword());

        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(confirm)))
                .andExpect(status().isOk());
    }

    @Test
    void requestPasswordReset_InvalidEmail_ShouldReturnBadRequest() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest(""); // invalid

        mockMvc.perform(post("/api/auth/request-password-reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void resetPassword_MissingFields_ShouldReturnBadRequest() throws Exception {
        PasswordResetConfirm confirm = new PasswordResetConfirm("", ""); // invalid

        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(confirm)))
                .andExpect(status().isBadRequest());
    }
}
