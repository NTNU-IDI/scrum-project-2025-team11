package no.ntnu.idatt2106.krisefikser.controller;

import java.util.Arrays;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import no.ntnu.idatt2106.krisefikser.dto.AddressRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.ConfirmAuthenticationRequest;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.HouseholdResponseDTO;
import no.ntnu.idatt2106.krisefikser.dto.LoginRequest;
import no.ntnu.idatt2106.krisefikser.dto.UserRequestDTO;
import no.ntnu.idatt2106.krisefikser.dto.UserResponseDTO;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.model.User.Role;
import no.ntnu.idatt2106.krisefikser.security.JwtUtil;
import no.ntnu.idatt2106.krisefikser.service.AddressService;
import no.ntnu.idatt2106.krisefikser.service.HouseholdService;
import no.ntnu.idatt2106.krisefikser.service.RefreshTokenService;
import no.ntnu.idatt2106.krisefikser.service.TwoFactorCodeService;
import no.ntnu.idatt2106.krisefikser.service.UserService;

/**
 * Controller class for handling authentication-related operations.
 * This class provides endpoints for user registration, login, JWT token generation,
 * and two-factor authentication confirmation.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
  
  private final JwtUtil jwtUtil;
  private final UserService userService;
  private final RefreshTokenService refreshTokenService;
  private final PasswordEncoder passwordEncoder;
  private final TwoFactorCodeService twoFactorCodeService;
  private final AddressService addressService;
  private final HouseholdService householdService;
  
  /**
  * Endpoint to refresh the JWT token using the refresh token stored in cookies.
  * If the refresh token is valid, a new JWT token is generated and returned in a cookie.
  * The old refresh token is revoked.
  *
  * @param request the HTTP request containing the refresh token in cookies
  * @param response the HTTP response to set the new JWT token cookie
  * @return a response entity with the user's role if successful, or an error message if not
  */
  @Operation(
  summary = "Refresh JWT token",
  description = "Refreshes the JWT token by generating a new one if old was a valid token")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Token successfully refreshed"),
    @ApiResponse(responseCode = "403", description = "Could not refresh the token, check credentials."),
    @ApiResponse(responseCode = "498", description = "Missing refresh token, please log in again")
  })
  @PostMapping("/refresh")
  public ResponseEntity<?> refreshJwtToken(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return ResponseEntity.status(498).body("Missing refresh token");
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
  * @param response the HTTP response to set the JWT token cookie 
  *
  * @return a response entity indicating the result of the operation
  */
  @Operation(
  summary = "Checks login credentials",
  description = "Checks login credentials and sends a mail to the registered mail address")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Login successful"),
    @ApiResponse(responseCode = "400", description = "Invalid credentials")
  })
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    String username = loginRequest.getUsername();
    String password = loginRequest.getPassword();
    
    User user = userService.getUserByUsername(username).orElse(null);
    if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials");
    }
    
    twoFactorCodeService.initiateCode(username);
    
    return ResponseEntity.ok().build();
  }
  
  /**
   * Generates a refresh token and a JWT token for the user.
   * The refresh token is stored in a cookie with HttpOnly and Secure flags.
   * The JWT token is also stored in a cookie with HttpOnly and Secure flags.
   * 
   * @param username the username of the user
   * @param user the user object containing user details
   * @return a cookie containing the refresh token
   */
  private Cookie generateRefreshToken(String username, User user) {
    String refreshToken = jwtUtil.generateRefreshToken(username);
    
    refreshTokenService.createRefreshToken(user, refreshToken, jwtUtil.getRefreshExpiration());
    
    Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
    refreshCookie.setHttpOnly(true);
    refreshCookie.setSecure(false); // important in production
    refreshCookie.setPath("/auth/refresh");
    refreshCookie.setMaxAge((int) jwtUtil.getRefreshExpiration() / 1000);
    return refreshCookie;
  }
  
  private Cookie generateJwtToken(String username, User user) {
    String token = jwtUtil.generateToken(username, user.getRole().toString());
    Cookie jwtCookie = new Cookie("jwtToken", token);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setSecure(false);
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
    
    UserResponseDTO saved = userService.saveUser(body, Role.normal);
    User user = userService.getUserByUsername(saved.getUsername()).orElse(null);
    
    if (user == null) {
      return ResponseEntity.status(500).body("Failed to retrieve created user");
    }
    twoFactorCodeService.initiateCode(user.getUsername());
    
    return ResponseEntity.ok().build();
  }
  
  /**
  * Completes the two-factor authentication process by validating the provided code.
  *
  * @param request the two-factor authentication confirmation containing the code and the login information
  * @return a response entity indicating the result of the operation and cookies such as jwt and refresh token
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
  
  /**
  * Endpoint to register a admin user as a super admin
  * 
  * @param body registration information such as name, username, password, email. Household id is not needed
  * @return statuscode 200 if the admin user was successfully created
  * @throws Exception
  */
  @Operation(
  summary = "Registers an admin user when logged in as a super admin",
  description = "A logged in superadmin can create another admin by sending registration information"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Admin user created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid registration information"),
    @ApiResponse(responseCode = "409", description = "Either the email or username already exist")
  })
  @PreAuthorize("hasRole('super_admin')")
  @PostMapping("/register-admin")
  public ResponseEntity<?> registerAdmin(@RequestBody UserRequestDTO body) throws Exception {
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
    AddressRequestDTO address = new AddressRequestDTO();
    address.setCity("Trondheim");
    address.setPostalCode("7035");
    address.setLatitude(73.4305);
    address.setLongitude(18.5555);
    address.setStreet("Heiabakken 123");
    addressService.save(address);
    
    HouseholdRequestDTO household = new HouseholdRequestDTO();
    household.setName("Admin hus");
    household.setMemberCount(1);
    household.setAddress(address);
    HouseholdResponseDTO savedHousehold = householdService.save(household);
    
    body.setHouseholdId(savedHousehold.getId());
    userService.saveUser(body, Role.admin);
    
    twoFactorCodeService.registerAdmin(body.getFirstName(), body.getUsername(), body.getPassword(), body.getEmail());
    return ResponseEntity.ok().build();
  }
}

