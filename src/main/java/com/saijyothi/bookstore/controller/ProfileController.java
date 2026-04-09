package com.saijyothi.bookstore.controller;

import com.saijyothi.bookstore.dto.PasswordChangeRequest;
import com.saijyothi.bookstore.dto.ProfileRequest;
import com.saijyothi.bookstore.dto.ProfileResponse;
import com.saijyothi.bookstore.security.UserPrincipal;
import com.saijyothi.bookstore.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ProfileResponse getProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return profileService.getProfile(userPrincipal.getId());
    }

    @PutMapping
    public ProfileResponse updateProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody ProfileRequest request) {
        return profileService.updateProfile(userPrincipal.getId(), request);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody PasswordChangeRequest request) {
        profileService.changePassword(userPrincipal.getId(), request);
    }
}
