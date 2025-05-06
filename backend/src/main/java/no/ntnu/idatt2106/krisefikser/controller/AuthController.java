  package no.ntnu.idatt2106.krisefikser.controller;

  import java.util.Arrays;
  import java.util.Map;

  import lombok.RequiredArgsConstructor;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
  import org.springframework.security.crypto.password.PasswordEncoder;
  import org.springframework.web.bind.annotation.PostMapping;
  import org.springframework.web.bind.annotation.RequestBody;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;

  import io.swagger.v3.oas.annotations.Operation;
  import io.swagger.v3.oas.annotations.Parameter;
  import io.swagger.v3.oas.annotations.media.Content;
  import io.swagger.v3.oas.annotations.media.Schema;
  import io.swagger.v3.oas.annotations.responses.ApiResponse;
  import io.swagger.v3.oas.annotations.responses.ApiResponses;
  import jakarta.servlet.http.Cookie;
  import jakarta.servlet.http.HttpServletRequest;
  import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import no.ntnu.idatt2106.krisefikser.dto.ConfirmAuthenticationRequest;
import no.ntnu.idatt2106.krisefikser.dto.LoginRequest;
import no.ntnu.idatt2106.krisefikser.dto.TwoFactorConfirmDTO;
import no.ntnu.idatt2106.krisefikser.dto.TwoFactorRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserRequestDTO;
  import no.ntnu.idatt2106.krisefikser.dto.UserResponseDTO;
  import no.ntnu.idatt2106.krisefikser.model.User;
  import no.ntnu.idatt2106.krisefikser.security.JwtUtil;
  import no.ntnu.idatt2106.krisefikser.service.RefreshTokenService;
import no.ntnu.idatt2106.krisefikser.service.TwoFactorCodeService;
import no.ntnu.idatt2106.krisefikser.service.UserService;

  @RestController
  @RequiredArgsConstructor
  @RequestMapping("/auth")
  public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final TwoFactorCodeService twoFactorCodeService;

    @Operation(
      summary = "Refresh JWT token",
      description = "Refreshes the JWT token by generating a new one if old was a valid token")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Token successfully refreshed"),
      @ApiResponse(responseCode = "403", description = "Could not refresh the token, check credentials.")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwtToken(HttpServletRequest request, HttpServletResponse response) {
      Cookie[] cookies = request.getCookies();
      if (cookies == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing refresh token");
      }

      String refreshToken = Arrays.stream(cookies)
          .filter(cookie -> "refreshToken".equals(cookie.getName()))
          .map(Cookie::getValue)
          .findFirst()
          .orElse(null);

      if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
      }
      String username = jwtUtil.extractUsername(refreshToken);
      User user = userService.getUserByUsername(username).orElse(null);
      
      if (user == null) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
      }

      Cookie jwtCookie = generateJwtToken(username, user);

      response.addCookie(jwtCookie);

      refreshTokenService.revokeToken(refreshToken);

      Cookie refreshCookie = generateRefreshToken(username, user);
      response.addCookie(refreshCookie);

      return ResponseEntity.ok().body(Map.of("role ", user.getRole()));
    }

    /**
     * Checks the information provided by the client wether the information is 
     * valid for log in. If it is valid then backend will send a mail to the users
     * mail address containing the authentication code
     * 
     * @param loginRequest a DTO of the information that is sent such as username and password 
     */
    @Operation(
      summary = "Checks login credentials",
      description = "Checks login credentials and sends a mail to the registered mail address")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Login successful"),
      @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
      String username = loginRequest.getUsername();
      String password = loginRequest.getPassword();

      User user = userService.getUserByUsername(username).orElse(null);
      if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
        return ResponseEntity.status(401).body("Invalid credentials");
      }

      String email = user.getEmail();
      twoFactorCodeService.initiateCode(email);

      return ResponseEntity.ok().build();
    }

    private Cookie generateRefreshToken(String username, User user) {
      String refreshToken = jwtUtil.generateRefreshToken(username);

      refreshTokenService.createRefreshToken(user, refreshToken, jwtUtil.getRefreshExpiration());

      Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
      refreshCookie.setHttpOnly(true);
      refreshCookie.setSecure(true); // important in production
      refreshCookie.setPath("/auth/refresh");
      refreshCookie.setMaxAge((int) jwtUtil.getRefreshExpiration() / 1000);
      return refreshCookie;
    }

    private Cookie generateJwtToken(String username, User user) {
      String token = jwtUtil.generateToken(username, user.getRole().toString());
      Cookie jwtCookie = new Cookie("jwtToken", token);
      jwtCookie.setHttpOnly(true);
      jwtCookie.setSecure(true);
      jwtCookie.setPath("/");
      jwtCookie.setMaxAge((int) jwtUtil.getExpiration() / 1000);
      return jwtCookie;
    }
    /**
     * Checks wether the information given by the client is valid. If it is then it will send
     * a two factor mail to the email provided by the user and register that user in the db.
     *
     * @param body the user entity to be checked
     * @return {@code ResponseEntity} 
     */
    @Operation(
      summary     = "Register new user",
      description = "Creates a new user. Sends mail for authentication.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User created",
            content = @Content(mediaType = "application/json",
                      schema = @Schema(implementation = UserResponseDTO.class))),
        @ApiResponse(responseCode = "409", description = "Email or username already taken"),
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
          @Parameter(description = "User registration payload", required = true)
          @RequestBody UserRequestDTO body,
          HttpServletResponse response) {
      
      if (userService.emailExists(body.getEmail())) {
        return ResponseEntity
            .status(409) // Conflict
            .body(null);
      }
      if (userService.usernameExists(body.getUsername())) {
        return ResponseEntity
            .status(409) // Conflict
            .body(null);
      }

      UserResponseDTO saved = userService.saveUser(body);
      User user = userService.getUserByUsername(saved.getUsername()).orElse(null);

      if (user == null) {
        return ResponseEntity.status(500).body("Failed to retrieve created user");
      }

      String email = user.getEmail();
      twoFactorCodeService.initiateCode(email);

      return ResponseEntity.ok().build();
    }

    /**
     * Completes the two-factor authentication process by validating the provided code.
     *
     * @param request the two-factor authentication confirmation containing the code and either the login information
     *        or the registration information.
     * @return a response entity indicating the result of the operation
     */
    @Operation(
            summary = "Confirm two-factor authentication",
            description = "Confirm the two-factor authentication process using the provided code."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Two-factor authentication confirmed successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid or expired code.")
    })
    @PostMapping("/confirm-authentication")
    public ResponseEntity<?> confirmAuthentication(
      @RequestBody @Valid ConfirmAuthenticationRequest request,
      HttpServletRequest servletRequest, 
      HttpServletResponse response) {
        twoFactorCodeService.completeAuthentication(request.getTwoFactorCode().getCode(), request.getLogin());
        if (request.getLogin() != null) {

          User loginUser = userService.getUserByUsername(request.getLogin().getUsername()).orElse(null);
          Cookie jwtCookie = generateJwtToken(loginUser.getUsername(), loginUser);
          Cookie refreshCookie = generateRefreshToken(loginUser.getUsername(), loginUser);
          response.addCookie(jwtCookie);
          response.addCookie(refreshCookie);
          return ResponseEntity.ok().body(Map.of("role", loginUser.getRole())); 

        }
        return ResponseEntity.status(500).body("Failed to retrieve created user"); 
    }
  }

