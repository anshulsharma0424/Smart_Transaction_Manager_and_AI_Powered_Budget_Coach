package com.spenzr.ai_inference_service.controller;

import com.spenzr.ai_inference_service.dto.SuggestionRequest;
import com.spenzr.ai_inference_service.dto.SuggestionResponse;
import com.spenzr.ai_inference_service.service.AiInferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inference")
@RequiredArgsConstructor
public class InferenceController {

    private final AiInferenceService aiInferenceService;

    @PostMapping("/generate-suggestion")
    public ResponseEntity<SuggestionResponse> generateSuggestion(@RequestBody SuggestionRequest request) {
        String suggestion = aiInferenceService.generateSuggestion(request);
        return ResponseEntity.ok(new SuggestionResponse(suggestion));
    }
}

