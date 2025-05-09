package no.ntnu.idatt2106.krisefikser.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility component for working with JSON Web Tokens (JWT) in the application.
 * <p>
 * Upon initialization, it creates a signing key and parser based on the configured secret.
 * Provides methods to generate access and refresh tokens with custom expirations,
 * extract username and role claims from tokens, and validate tokens for authenticity and expiration.
 * <p>
 * Configuration properties:
 * <ul>
 *   <li><code>jwt.secret</code> - the secret key used for signing tokens</li>
 *   <li><code>jwt.expiration</code> - expiration duration for access tokens (in milliseconds)</li>
 *   <li><code>jwt.refreshExpiration</code> - expiration duration for refresh tokens (in milliseconds)</li>
 * </ul>
 */
@Component
public class JwtUtil {
  private SecretKey key;
  private JwtParser jwtParser;

  // Inject secret from properties
  @Value("${jwt.secret}")
  private String secretString;

  @Value("${jwt.expiration}")
  private long expiration;

  @Value("${jwt.refreshExpiration}")
  private long refreshExpiration;

  @PostConstruct
  public void init() {
    byte[] secretBytes = secretString.getBytes();
    this.key = Keys.hmacShaKeyFor(secretBytes);
    this.jwtParser = Jwts.parser().verifyWith(key).build();
  }

  public String generateToken(String username, String role) {
    return Jwts.builder()
        .subject(username)
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(key, Jwts.SIG.HS256)
        .compact();
  }

  public String generateRefreshToken(String username) {
    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
        .signWith(key, Jwts.SIG.HS256)
        .compact();
  }
  public String extractUsername(String token) {
    return jwtParser.parseSignedClaims(token).getPayload().getSubject();
  }

  public String extractRole(String token) {
    return jwtParser.parseSignedClaims(token).getPayload().get("role", String.class);
  }

  public long getRefreshExpiration() {
    return refreshExpiration;
  }

  public long getExpiration() {
    return expiration;
  }

  public JwtParser getJwtParser() {
    return this.jwtParser;
  }

  public boolean validateToken(String token) {
    try {
        jwtParser.parseSignedClaims(token);
        return true;
    } catch (ExpiredJwtException ex) {
        System.err.println("Token expired: " + ex.getMessage());
    } catch (Exception ex) {
        System.err.println("Token validation failed: " + ex.getMessage());
        ex.printStackTrace();
    }
    return false;
}
}
