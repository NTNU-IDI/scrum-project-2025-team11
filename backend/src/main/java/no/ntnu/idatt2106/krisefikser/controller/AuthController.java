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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.ntnu.idatt2106.krisefikser.dto.LoginRequest;
import no.ntnu.idatt2106.krisefikser.model.User;
import no.ntnu.idatt2106.krisefikser.security.JwtUtil;
import no.ntnu.idatt2106.krisefikser.service.RefreshTokenService;
import no.ntnu.idatt2106.krisefikser.service.UserService;

@RestController
@CrossOrigin(origins = "*")
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
    cookie.setPath("/auth/refresh");
    cookie.setMaxAge((int) jwtUtil.getRefreshExpiration() / 1000);

    response.addCookie(cookie);

    return ResponseEntity.ok(Map.of("token", token));
  }

}

