package com.spenzr.user_service.controller;

import com.spenzr.user_service.dto.UserDto;
import com.spenzr.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Endpoint to get the current authenticated user's profile.
     * If the user's profile doesn't exist locally, it will be created.

     * @param jwt The JWT token injected by Spring Security.
     * @return The user's profile data.
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        UserDto userDto = userService.findOrCreateUser(jwt);
        return ResponseEntity.ok(userDto);
    }

    // ADD THIS NEW ENDPOINT for server-to-server communication
    @GetMapping("/by-email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }
}
