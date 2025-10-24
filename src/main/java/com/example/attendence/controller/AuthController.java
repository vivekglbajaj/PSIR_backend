package com.example.attendence.controller;

// AuthController.java

import com.example.attendence.Dto.AuthRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // NOTE: In a real app, inject Spring Security's AuthenticationManager here
    // and use a UserDetailsService to load user data from a database.

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest authRequest) {

        // --- BASIC, HARDCODED CHECK (For quick implementation) ---
        // REPLACE THIS WITH REAL DATABASE/SECURITY LOGIC!
        if ("teacher".equals(authRequest.getUsername()) &&
                "password123".equals(authRequest.getPassword())) {

            // Success: Return an arbitrary token or success message.
            // In a real application, you'd generate a JWT here.
            return ResponseEntity.ok(Map.of("message", "Login successful!", "token", "fake-jwt-token"));
        } else {
            // Failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }
    }
}
