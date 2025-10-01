package com.spenzr.suggestion_service.controller;

import com.spenzr.suggestion_service.dto.CreateSuggestionRequest;
import com.spenzr.suggestion_service.dto.SuggestionDto;
import com.spenzr.suggestion_service.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suggestions")
@RequiredArgsConstructor
public class SuggestionController {

    private final SuggestionService suggestionService;

    /**
     * Endpoint for the frontend to fetch all suggestions for the logged-in user.
     */
    @GetMapping
    public ResponseEntity<List<SuggestionDto>> getMySuggestions(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(suggestionService.getSuggestionsForUser(userId));
    }

    /**
     * Internal endpoint for the Data Aggregation Service to save a new suggestion.
     */
    @PostMapping
    public ResponseEntity<SuggestionDto> saveSuggestion(@RequestBody CreateSuggestionRequest request) {
        SuggestionDto savedSuggestion = suggestionService.saveSuggestion(request);
        return new ResponseEntity<>(savedSuggestion, HttpStatus.CREATED);
    }
}