package com.spenzr.suggestion_service.service;

import com.spenzr.suggestion_service.dto.CreateSuggestionRequest;
import com.spenzr.suggestion_service.dto.SuggestionDto;
import com.spenzr.suggestion_service.entity.Suggestion;
import com.spenzr.suggestion_service.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;

    public SuggestionDto saveSuggestion(CreateSuggestionRequest request) {
        Suggestion suggestion = Suggestion.builder()
                .userId(request.getUserId())
                .suggestionContent(request.getSuggestionContent())
                .relatedTransactionId(request.getRelatedTransactionId())
                .relatedTransactionDescription(request.getRelatedTransactionDescription())
                .isRead(false) // New suggestions are always unread
                .createdAt(Instant.now())
                .build();

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);
        return mapToDto(savedSuggestion);
    }

    public List<SuggestionDto> getSuggestionsForUser(String userId) {
        return suggestionRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteSuggestionsByTransactionId(String transactionId) {
        suggestionRepository.deleteAllByRelatedTransactionId(transactionId);
    }

    private SuggestionDto mapToDto(Suggestion suggestion) {
        return SuggestionDto.builder()
                .id(suggestion.getId())
                .userId(suggestion.getUserId())
                .suggestionContent(suggestion.getSuggestionContent())
                .relatedTransactionId(suggestion.getRelatedTransactionId())
                .relatedTransactionDescription(suggestion.getRelatedTransactionDescription())
                .isRead(suggestion.isRead())
                .createdAt(suggestion.getCreatedAt())
                .build();
    }
}