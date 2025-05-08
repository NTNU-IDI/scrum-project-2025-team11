package no.ntnu.idatt2106.krisefikser.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_NoCookies_ProceedsWithoutAuthentication() throws Exception {
        when(request.getCookies()).thenReturn(null);

        jwtAuthFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_ValidJwtCookie_SetsAuthentication() throws Exception {
        // Arrange
        Cookie jwtCookie = new Cookie("jwtToken", "valid.token");
        JwtParser jwtParser = mock(JwtParser.class);
        Jws<?> jws = mock(Jws.class);
        
        when(request.getCookies()).thenReturn(new Cookie[]{jwtCookie});
        when(jwtUtil.getJwtParser()).thenReturn(jwtParser);
        when(jwtParser.parseSignedClaims("valid.token")).thenReturn((Jws<Claims>) jws);
        when(jwtUtil.extractUsername("valid.token")).thenReturn("testuser");
        when(jwtUtil.extractRole("valid.token")).thenReturn("ADMIN");

        // Act
        jwtAuthFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("testuser", authentication.getName());
        assertEquals(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")),
                    authentication.getAuthorities());
    }

    @Test
    void doFilterInternal_ExpiredJwt_ReturnsUnauthorized() throws Exception {
        // Arrange
        Cookie jwtCookie = new Cookie("jwtToken", "expired.token");
        JwtParser jwtParser = mock(JwtParser.class);
        
        when(request.getCookies()).thenReturn(new Cookie[]{jwtCookie});
        when(jwtUtil.getJwtParser()).thenReturn(jwtParser);
        when(jwtParser.parseSignedClaims("expired.token")).thenThrow(new ExpiredJwtException(null, null, "Expired"));

        // Act
        jwtAuthFilter.doFilter(request, response, filterChain);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidJwt_ReturnsUnauthorized() throws Exception {
        // Arrange
        Cookie jwtCookie = new Cookie("jwtToken", "invalid.token");
        JwtParser jwtParser = mock(JwtParser.class);
        
        when(request.getCookies()).thenReturn(new Cookie[]{jwtCookie});
        when(jwtUtil.getJwtParser()).thenReturn(jwtParser);
        when(jwtParser.parseSignedClaims("invalid.token")).thenThrow(new JwtException("Invalid"));

        // Act
        jwtAuthFilter.doFilter(request, response, filterChain);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_OtherCookies_IgnoresNonJwtCookies() throws Exception {
        // Arrange
        Cookie otherCookie = new Cookie("otherCookie", "value");
        when(request.getCookies()).thenReturn(new Cookie[]{otherCookie});

        // Act
        jwtAuthFilter.doFilter(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_MalformedJwt_ReturnsUnauthorized() throws Exception {
        // Arrange
        Cookie jwtCookie = new Cookie("jwtToken", "malformed");
        JwtParser jwtParser = mock(JwtParser.class);
        
        when(request.getCookies()).thenReturn(new Cookie[]{jwtCookie});
        when(jwtUtil.getJwtParser()).thenReturn(jwtParser);
        when(jwtParser.parseSignedClaims("malformed")).thenThrow(new IllegalArgumentException("Bad JWT"));

        // Act
        jwtAuthFilter.doFilter(request, response, filterChain);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        verify(filterChain, never()).doFilter(request, response);
    }
}