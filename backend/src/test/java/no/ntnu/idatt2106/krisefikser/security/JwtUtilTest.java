package no.ntnu.idatt2106.krisefikser.security;

import io.jsonwebtoken.JwtParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private final String secret = "MySuperSecretKeyForJwt1234567890";
    private final long expiration = 1000 * 60 * 10;
    private final long refreshExpiration = 1000 * 60 * 60 * 24;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretString", secret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", expiration);
        ReflectionTestUtils.setField(jwtUtil, "refreshExpiration", refreshExpiration);
        jwtUtil.init(); // manually call @PostConstruct
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtUtil.generateToken("testuser", "ROLE_USER");

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken("john_doe", "ROLE_ADMIN");

        String username = jwtUtil.extractUsername(token);
        assertEquals("john_doe", username);
    }

    @Test
    void testExtractRole() {
        String token = jwtUtil.generateToken("alice", "ROLE_MODERATOR");

        String role = jwtUtil.extractRole(token);
        assertEquals("ROLE_MODERATOR", role);
    }

    @Test
    void testGenerateRefreshToken() {
        String refreshToken = jwtUtil.generateRefreshToken("testuser");

        assertNotNull(refreshToken);
        assertTrue(jwtUtil.validateToken(refreshToken));
    }

    @Test
    void testInvalidToken() {
        String invalidToken = "invalid.token.value";

        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void testGetExpirationValues() {
        assertEquals(expiration, jwtUtil.getExpiration());
        assertEquals(refreshExpiration, jwtUtil.getRefreshExpiration());
    }

    @Test
    void testJwtParserNotNull() {
        JwtParser parser = jwtUtil.getJwtParser();
        assertNotNull(parser);
    }
}
