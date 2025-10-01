package com.spenzr.user_preference_service.controller;

import com.spenzr.user_preference_service.dto.UserPreferenceDto;
import com.spenzr.user_preference_service.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    @GetMapping("/me")
    public ResponseEntity<UserPreferenceDto> getMyPreference(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        return ResponseEntity.ok(userPreferenceService.getPreference(keycloakId));
    }

    @PutMapping("/me")
    public ResponseEntity<UserPreferenceDto> updateMyPreference(@AuthenticationPrincipal Jwt jwt, @RequestBody UserPreferenceDto preferenceDto) {
        String keycloakId = jwt.getSubject();
        return ResponseEntity.ok(userPreferenceService.updatePreference(keycloakId, preferenceDto));
    }
}
