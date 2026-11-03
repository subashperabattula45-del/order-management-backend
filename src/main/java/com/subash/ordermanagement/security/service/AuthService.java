package com.subash.ordermanagement.security.service;

import com.subash.ordermanagement.exception.InvalidCredentialsException;
import com.subash.ordermanagement.exception.UserNotFoundException;
import com.subash.ordermanagement.security.dto.LoginRequest;
import com.subash.ordermanagement.security.dto.LoginResponse;
import com.subash.ordermanagement.user.entity.User;
import com.subash.ordermanagement.user.repository.UserRepository;

import java.time.Instant;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("user not found with this email" + request.getEmail()));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getEmail())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("userId", user.getId())
                .claim("role", user.getRole().name())
                .build();

        String token = jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();

        return new LoginResponse(token);
    }
}