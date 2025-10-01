package com.spenzr.user_service.service;

import com.spenzr.user_service.dto.UserDto;
import com.spenzr.user_service.entity.User;
import com.spenzr.user_service.exception.ResourceNotFoundException;
import com.spenzr.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    /**
     * Finds a user by their Keycloak ID. If the user doesn't exist,
     * it creates a new user profile in the local database using claims from the JWT.
     * This is the "sync on first login" mechanism.

     * @param jwt --> The decoded JWT token from the authenticated user.
     * @return --> UserDto of the found or newly created user.
     */

    @Transactional
    public UserDto findOrCreateUser(Jwt jwt) {
        String keycloakId = jwt.getSubject();

        return userRepository.findByKeycloakId(keycloakId)
                .map(this::mapToUserDto)
                .orElseGet(() -> {
                    log.info("Creating new user profile for Keycloak ID: {}", keycloakId);
                    User newUser = User.builder()
                            .keycloakId(keycloakId)
                            .email(jwt.getClaimAsString("email"))
                            .firstName(jwt.getClaimAsString("given_name"))
                            .lastName(jwt.getClaimAsString("family_name"))
                            .build();
                    User savedUser = userRepository.save(newUser);
                    return mapToUserDto(savedUser);
                });
    }

    // ADD THIS NEW METHOD
    @Transactional(readOnly = true)
    public UserDto findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .keycloakId(user.getKeycloakId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
