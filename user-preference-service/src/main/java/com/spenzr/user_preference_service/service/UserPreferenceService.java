package com.spenzr.user_preference_service.service;

import com.spenzr.user_preference_service.dto.UserPreferenceDto;
import com.spenzr.user_preference_service.entity.Currency;
import com.spenzr.user_preference_service.entity.UserPreference;
import com.spenzr.user_preference_service.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreferenceDto getPreference(String keycloakId) {
        UserPreference preference = userPreferenceRepository.findByKeycloakId(keycloakId)
                .orElse(UserPreference.builder().currency(Currency.INR).build()); // Default to INR

        return mapToDto(preference);
    }

    @Transactional
    public UserPreferenceDto updatePreference(String keycloakId, UserPreferenceDto preferenceDto) {
        UserPreference preference = userPreferenceRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> UserPreference.builder().keycloakId(keycloakId).build());

        preference.setCurrency(preferenceDto.getCurrency());
        UserPreference savedPreference = userPreferenceRepository.save(preference);
        return mapToDto(savedPreference);
    }

    private UserPreferenceDto mapToDto(UserPreference userPreference) {
        return UserPreferenceDto.builder()
                .currency(userPreference.getCurrency())
                .build();
    }
}
