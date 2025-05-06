package no.ntnu.idatt2106.krisefikser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import no.ntnu.idatt2106.krisefikser.dto.ConfirmAuthenticationRequest;
import no.ntnu.idatt2106.krisefikser.dto.LoginRequest;
import no.ntnu.idatt2106.krisefikser.dto.TwoFactorConfirmDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.Enums;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.security.JwtAuthFilter;
import no.ntnu.idatt2106.krisefikser.security.JwtUtil;
import no.ntnu.idatt2106.krisefikser.service.RefreshTokenService;
import no.ntnu.idatt2106.krisefikser.service.TwoFactorCodeService;
import no.ntnu.idatt2106.krisefikser.service.UserService;
import no.ntnu.idatt2106.krisefikser.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TwoFactorCodeService twoFactorCodeService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private ConfirmAuthenticationRequest confirmAuthenticationRequest;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void confirmAuthentication_WithValidCredentials_ShouldReturnOk() throws Exception {
        // Arrange
        String code = "123456";
        String username = "thelegend27";
        String password = "passordhash1";
        
        // Create nested request structure
        TwoFactorConfirmDTO twoFactorDTO = new TwoFactorConfirmDTO(code);
        LoginRequest loginDTO = new LoginRequest();
        loginDTO.setPassword(password);
        loginDTO.setUsername(username);
        ConfirmAuthenticationRequest request = new ConfirmAuthenticationRequest();
        request.setTwoFactorCode(twoFactorDTO);
        request.setLogin(loginDTO);

        // Mock service layer
        doNothing().when(twoFactorCodeService)
            .completeAuthentication(eq(code), any(LoginRequest.class));
        
        // Mock user retrieval
        User mockUser = new User();
        mockUser.setUsername(username);
        when(userService.getUserByUsername(username))
            .thenReturn(Optional.of(mockUser));

        // Mock JWT generation
        when(jwtUtil.generateToken(anyString(), anyString()))
            .thenReturn("mock.jwt.token");
        when(jwtUtil.generateRefreshToken(anyString()))
            .thenReturn("mock.refresh.token");

        // Act & Assert
        mockMvc.perform(post("/auth/confirm-authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("normal"));
        
        // Verify service interactions
        verify(twoFactorCodeService, times(1))
            .completeAuthentication(code, loginDTO);
        verify(userService, times(1))
            .getUserByUsername(username);
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
        String username = "user";
        String password = "wrong";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        when(userService.getUserByUsername(username)).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void register_WithDuplicateEmail_ShouldReturnConflict() throws Exception {
        UserRequestDTO userRequest = new UserRequestDTO();
        userRequest.setUsername("user");
        userRequest.setEmail("taken@example.com");

        when(userService.emailExists(userRequest.getEmail())).thenReturn(true);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void refreshJwtToken_WithValidToken_ShouldReturnOk() throws Exception {
        String validRefreshToken = "valid.token";
        String username = "user";
        User user = new User();
        user.setUsername(username);

        Cookie refreshCookie = new Cookie("refreshToken", validRefreshToken);

        when(jwtUtil.validateToken(validRefreshToken)).thenReturn(true);
        when(jwtUtil.extractUsername(validRefreshToken)).thenReturn(username);
        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));
        doNothing().when(refreshTokenService).revokeToken(validRefreshToken);

        mockMvc.perform(post("/auth/refresh").cookie(refreshCookie))
                .andExpect(status().isOk());
    }

    @Test
    void refreshJwtToken_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }


}
