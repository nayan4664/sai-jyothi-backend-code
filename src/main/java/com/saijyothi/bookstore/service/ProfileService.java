package com.saijyothi.bookstore.service;

import com.saijyothi.bookstore.dto.PasswordChangeRequest;
import com.saijyothi.bookstore.dto.ProfileRequest;
import com.saijyothi.bookstore.dto.ProfileResponse;
import com.saijyothi.bookstore.entity.AppUser;
import com.saijyothi.bookstore.exception.AuthenticationException;
import com.saijyothi.bookstore.exception.UserAlreadyExistsException;
import com.saijyothi.bookstore.exception.UserNotFoundException;
import com.saijyothi.bookstore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long userId) {
        return toResponse(getUser(userId));
    }

    public ProfileResponse updateProfile(Long userId, ProfileRequest request) {
        AppUser user = getUser(userId);
        String normalizedEmail = request.email().trim().toLowerCase();

        if (!user.getEmail().equalsIgnoreCase(normalizedEmail)
                && userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new UserAlreadyExistsException(normalizedEmail);
        }

        user.setName(request.name().trim());
        user.setEmail(normalizedEmail);
        user.setPhone(trimNullable(request.phone()));
        user.setAddress(trimNullable(request.address()));
        user.setCity(trimNullable(request.city()));
        user.setState(trimNullable(request.state()));
        user.setPincode(trimNullable(request.pincode()));

        return toResponse(userRepository.save(user));
    }

    public void changePassword(Long userId, PasswordChangeRequest request) {
        AppUser user = getUser(userId);
        String currentPassword = request.currentPassword().trim();

        boolean encodedMatches = passwordEncoder.matches(currentPassword, user.getPassword());
        boolean legacyPlainTextMatches = user.getPassword().equals(currentPassword);

        if (!encodedMatches && !legacyPlainTextMatches) {
            throw new AuthenticationException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword().trim()));
        userRepository.save(user);
    }

    private AppUser getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private ProfileResponse toResponse(AppUser user) {
        return new ProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getCity(),
                user.getState(),
                user.getPincode(),
                user.getRole().name());
    }

    private String trimNullable(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
