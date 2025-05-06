/* package no.ntnu.idatt2106.krisefikser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.ntnu.idatt2106.krisefikser.dto.TwoFactorConfirmDTO;
import no.ntnu.idatt2106.krisefikser.dto.TwoFactorRequestDTO;
import no.ntnu.idatt2106.krisefikser.service.TwoFactorCodeService;
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
@WebMvcTest(TwoFactorController.class)
class TwoFactorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TwoFactorCodeService twoFactorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void twoFactorRequestDTO_ShouldReturnOk() throws Exception {
        TwoFactorRequestDTO request = new TwoFactorRequestDTO("testbruker");

        doNothing().when(twoFactorService).initiateCode(request.getUsername());

        mockMvc.perform(post("/api/auth/request-two-factor-code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void TwoFactorConfirmDTO_ShouldReturnOk() throws Exception {
        TwoFactorConfirmDTO confirm = new TwoFactorConfirmDTO("ABC123");

        doNothing().when(twoFactorService).completeAuthentication(confirm.getCode());

        mockMvc.perform(post("/api/auth/confirm-authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(confirm)))
                .andExpect(status().isOk());
    }

    @Test
    void requestTwoFactor_InvalidUsername_ShouldReturnBadRequest() throws Exception {
        TwoFactorRequestDTO request = new TwoFactorRequestDTO(""); // invalid

        mockMvc.perform(post("/api/auth/request-two-factor-code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void twoFactor_MissingFields_ShouldReturnBadRequest() throws Exception {
        TwoFactorConfirmDTO confirm = new TwoFactorConfirmDTO(""); // invalid

        mockMvc.perform(post("/api/auth/confirm-authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(confirm)))
                .andExpect(status().isBadRequest());
    }
}
 */