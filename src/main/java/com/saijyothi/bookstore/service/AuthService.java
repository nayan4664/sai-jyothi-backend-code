package com.saijyothi.bookstore.service;

import com.saijyothi.bookstore.dto.AuthRequest;
import com.saijyothi.bookstore.dto.AuthResponse;
import com.saijyothi.bookstore.dto.ForgotPasswordRequest;
import com.saijyothi.bookstore.dto.RegisterRequest;
import com.saijyothi.bookstore.dto.UserResponse;
import com.saijyothi.bookstore.entity.AppUser;
import com.saijyothi.bookstore.entity.UserRole;
import com.saijyothi.bookstore.exception.AuthenticationException;
import com.saijyothi.bookstore.exception.UserAlreadyExistsException;
import com.saijyothi.bookstore.repository.UserRepository;
import com.saijyothi.bookstore.security.JwtService;
import com.saijyothi.bookstore.security.UserPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new UserAlreadyExistsException(normalizedEmail);
        }

        AppUser user = new AppUser();
        user.setName(request.name().trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(request.password().trim()));
        user.setRole(UserRole.ROLE_USER);

        AppUser savedUser = userRepository.save(user);
        return buildAuthResponse(savedUser);
    }

    public AuthResponse login(AuthRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        AppUser user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        String rawPassword = request.password().trim();
        boolean encodedMatches = passwordEncoder.matches(rawPassword, user.getPassword());
        boolean legacyPlainTextMatches = user.getPassword().equals(rawPassword);

        if (!encodedMatches && !legacyPlainTextMatches) {
            throw new AuthenticationException("Invalid email or password");
        }

        if (legacyPlainTextMatches && !encodedMatches) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            user = userRepository.save(user);
        }

        return buildAuthResponse(user);
    }

    public void resetPassword(ForgotPasswordRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        AppUser user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new AuthenticationException("No account found for this email"));

        user.setPassword(passwordEncoder.encode(request.newPassword().trim()));
        userRepository.save(user);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private AuthResponse buildAuthResponse(AppUser user) {
        UserPrincipal userPrincipal = new UserPrincipal(user);
        String token = jwtService.generateToken(userPrincipal);
        return new AuthResponse(token, toResponse(user));
    }

    private UserResponse toResponse(AppUser user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }
}
