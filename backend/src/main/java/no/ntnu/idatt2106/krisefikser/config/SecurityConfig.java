package no.ntnu.idatt2106.krisefikser.config;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import no.ntnu.idatt2106.krisefikser.repository.UserRepository;
import no.ntnu.idatt2106.krisefikser.security.JwtAuthFilter;

/**
 * Security configuration class for the application.
 * This class configures Spring Security settings, including authentication and authorization.
 */
@Profile("!test")
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserRepository userRepository;

    // Inject via constructor
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserRepository userRepository) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userRepository = userRepository;
    }

    /**
     * Configures the security filter chain for the application.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Disable sessions
            )
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOriginPatterns(List.of("http://localhost:5173"));
                config.setAllowedMethods(List.of("*"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                return config;
            }))
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            )
            .exceptionHandling(exceptions -> exceptions
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        // Check if the user is authenticated
                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                            // Not authenticated → Send 401
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        } else {
                            // Authenticated but lacks permissions → Send 403
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                        }
                    })
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/h2-console/**"
                ).permitAll()
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            //.httpBasic(Customizer.withDefaults()); // optional depending on your needs

        return http.build();
    }

    /**
     * Configures the UserDetailsService bean for user authentication.
     *
     * @return the configured UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            no.ntnu.idatt2106.krisefikser.model.User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            // Spring Security expects ROLE_ prefix for authorities
            String role = "ROLE_" + user.getRole().name().toUpperCase();

            return User.withUsername(user.getUsername())
                .password(user.getPassword()) // must be hashed
                .authorities(role)            // e.g., ROLE_ADMIN, ROLE_SUPER_ADMIN
                .build();
        };
    }

    /**
     * Configures the PasswordEncoder bean for password hashing.
     *
     * @return the configured PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
