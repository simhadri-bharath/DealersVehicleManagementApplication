package com.bharath.task.controller;

import com.bharath.task.dto.AuthRequest;
import com.bharath.task.dto.AuthResponse;
import com.bharath.task.dto.DealerRegistrationRequest;
import com.bharath.task.entity.Dealer;
import com.bharath.task.security.JwtUtil;
import com.bharath.task.service.DealerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final DealerService dealerService;

    @PostMapping("/register")
    public ResponseEntity<Dealer> registerDealer( @Valid @RequestBody DealerRegistrationRequest request) {
        return ResponseEntity.ok(dealerService.registerDealer(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken( @Valid @RequestBody AuthRequest authRequest) throws Exception {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        // If authentication is successful, generate a JWT
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        // Return the JWT in the response
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}