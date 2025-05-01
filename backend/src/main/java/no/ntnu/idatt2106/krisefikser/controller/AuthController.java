  package no.ntnu.idatt2106.krisefikser.controller;

  import java.util.Arrays;
  import java.util.Map;

  import org.springframework.http.HttpStatus;
  import org.springframework.http.ResponseEntity;
  import org.springframework.web.bind.annotation.CrossOrigin;
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
  import no.ntnu.idatt2106.krisefikser.dto.LoginRequest;
  import no.ntnu.idatt2106.krisefikser.dto.UserRequestDTO;
  import no.ntnu.idatt2106.krisefikser.dto.UserResponseDTO;
  import no.ntnu.idatt2106.krisefikser.model.User;
  import no.ntnu.idatt2106.krisefikser.security.JwtUtil;
  import no.ntnu.idatt2106.krisefikser.service.RefreshTokenService;
  import no.ntnu.idatt2106.krisefikser.service.UserService;

  @RestController
  @CrossOrigin(
    origins = "http://localhost:5173",
    allowCredentials = "true")
  @RequestMapping("/auth")
  public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(JwtUtil jwtUtil, UserService userService, RefreshTokenService refreshTokenService) {
      this.jwtUtil = jwtUtil;
      this.userService = userService;
      this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
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
      String role = jwtUtil.extractRole(refreshToken); // Optional if refresh token doesn't include role

      String newAccessToken = jwtUtil.generateToken(username, role);

      return ResponseEntity.ok().body(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
      String username = loginRequest.getUsername();
      String password = loginRequest.getPassword();

      User user = userService.getUserByUsername(username).orElse(null);
      if (user == null || !password.equals(user.getPassword())) {
        return ResponseEntity.status(401).body("Invalid credentials");
      }

      String token = jwtUtil.generateToken(username, user.getRole().toString());
      String refreshToken = jwtUtil.generateRefreshToken(username);

      refreshTokenService.createRefreshToken(user, token, jwtUtil.getRefreshExpiration());

      Cookie cookie = new Cookie("refreshToken", refreshToken);
      cookie.setHttpOnly(true);
      cookie.setSecure(true); // important in production
      cookie.setPath("/");
      cookie.setMaxAge((int) jwtUtil.getRefreshExpiration() / 1000);

      response.addCookie(cookie);

      return ResponseEntity.ok(Map.of("token", token));
    }

    /**
     * Saves a new user entity.
     *
     * @param user the user entity to be saved
     * @return {@code ResponseEntity} containing the saved user entity
     */
    @Operation(
      summary     = "Register new user",
      description = "Creates a new user. Authentication *not* required.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created",
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

      // Generate tokens
      String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString());
      String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

      refreshTokenService.createRefreshToken(user, token, jwtUtil.getRefreshExpiration());

      Cookie cookie = new Cookie("refreshToken", refreshToken);
      cookie.setHttpOnly(true);
      cookie.setSecure(true);
      cookie.setPath("/auth/refresh");
      cookie.setMaxAge((int) jwtUtil.getRefreshExpiration() / 1000);
      response.addCookie(cookie);

      // Return both the saved user and access token
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(Map.of("user", saved, "token", token));
    }
  }

