package no.ntnu.idatt2106.krisefikser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Profile;

import no.ntnu.idatt2106.krisefikser.repository.UserRepository;

@Profile("!test") // or any non-test profile
@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**") // allow POSTs to H2 console
            .disable()
            )
            .headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin()) // allow H2 Console in frames
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/login",
                    "/auth/refresh",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/h2-console/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

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
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}