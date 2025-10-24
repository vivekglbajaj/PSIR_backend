package com.example.attendence.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // --- 1. Password Encoder (Always use BCrypt) ---
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- 2. User Detail Service (In-Memory User) ---
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        // Plain password: "password123"
        UserDetails teacherUser = User.builder()
                .username("teacher")
                .password(encoder.encode("password123"))
                .roles("TEACHER")
                .build();

        return new InMemoryUserDetailsManager(teacherUser);
    }

    // --- 3. Authentication Manager ---
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // --- 4. CORS Configuration (Allow frontend access) ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Assuming your frontend is running on default live server ports. Adjust if needed.
        configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:5500", "http://127.0.0.1:5500"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        // ALLOW CREDENTIALS IS VITAL for session cookies to be sent with requests
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    // --- 5. Security Filter Chain (Authorization Rules) ---
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Enable CORS using the bean defined above
                .cors(Customizer.withDefaults())

                // Disable CSRF for stateless APIs
                .csrf(AbstractHttpConfigurer::disable)

                // --- KEY FIX: Re-enabling Basic/Session Auth ---
                // This allows the server to establish and use a session cookie
                // after the successful /auth/login POST.
                .httpBasic(Customizer.withDefaults())

                // Configure Authorization Rules
                .authorizeHttpRequests(authorize -> authorize
                        // Allow anyone to access the login endpoint
                        .requestMatchers("/auth/login").permitAll()

                        // All other requests to /api/** require authentication
                        // The user will be authenticated via the session cookie after login
                        .requestMatchers("/api/**").authenticated()

                        // Allow access to static frontend files and all other paths
                        .anyRequest().permitAll()
                )

                // --- KEY FIX: REMOVED SessionCreationPolicy.STATELESS ---
                // By commenting out or removing the line below, Spring defaults to
                // SessionCreationPolicy.IF_REQUIRED, which fixes the 403 error.
                // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                .sessionManagement(Customizer.withDefaults()); // Uses IF_REQUIRED


        return http.build();
    }
}
