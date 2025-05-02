package no.ntnu.idatt2106.krisefikser.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;

  public JwtAuthFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
protected void doFilterInternal(
    HttpServletRequest request, 
    HttpServletResponse response, 
    FilterChain chain
) throws ServletException, IOException {

    // Extract JWT token from "accessToken" cookie
    Cookie[] cookies = request.getCookies();
    String token = null;
    if (cookies != null) {
        token = Arrays.stream(cookies)
            .filter(cookie -> "jwtToken".equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);
    }

    // If no token found, proceed without authentication
    if (token == null) {
        chain.doFilter(request, response);
        return;
    }

    // CSRF protection for state-changing requests (POST, PUT, DELETE, etc.)
    if (!request.getMethod().equalsIgnoreCase("GET")) {
        String csrfToken = request.getHeader("X-XSRF-TOKEN");
        String csrfCookie = Arrays.stream(request.getCookies())
            .filter(cookie -> "XSRF-TOKEN".equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);

        if (csrfCookie == null || !csrfCookie.equals(csrfToken)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
    }

    try {
        // Validate JWT token
        if (!jwtUtil.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid/expired token");
            return;
        }

        // Extract username and role from token
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Create authorities (prefix roles with "ROLE_" for Spring Security)
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role)
            );

            // Create authentication token
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(username, null, authorities);
            
            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

    } catch (Exception e) {
        // Handle token validation errors (e.g., tampered token)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token validation failed: " + e.getMessage());
        return;
    }

    // Proceed with the filter chain
    chain.doFilter(request, response);
}

}
