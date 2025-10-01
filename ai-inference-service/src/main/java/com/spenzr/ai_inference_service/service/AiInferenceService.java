package com.spenzr.ai_inference_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spenzr.ai_inference_service.dto.SuggestionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiInferenceService {

    private final GeminiHttpService geminiHttpService;
    private final ObjectMapper objectMapper; // Inject the Spring-managed ObjectMapper

    // This annotation tells Spring to inject the template file from the classpath
    @Value("classpath:prompt-template.st")
    private Resource promptTemplateResource;

    public String generateSuggestion(SuggestionRequest request) {
        String prompt = createPromptFromFile(request);
        String rawJsonResponse = geminiHttpService.generateContent(prompt);

        return parseResponse(rawJsonResponse);
    }

    private String parseResponse(String rawJsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(rawJsonResponse);
            // This navigates the nested JSON to get the actual text content
            String suggestion = rootNode
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
            if (suggestion.isEmpty()) {
                log.warn("AI response was empty or malformed. Full response: {}", rawJsonResponse);
                return "I'm having trouble thinking of a suggestion right now.";
            }
            return suggestion;
        } catch (Exception e) {
            log.error("Failed to parse AI response: {}", rawJsonResponse, e);
            return "Sorry, I couldn't generate a tip right now. Please try again later.";
        }
    }

    // This method now reads the template file and replaces the placeholders
    private String createPromptFromFile(SuggestionRequest request) {
        try {
            String template = promptTemplateResource.getContentAsString(StandardCharsets.UTF_8);
            return template
                    .replace("{description}", request.getDescription())
                    .replace("{amount}", request.getAmount().toString())
                    .replace("{category}", request.getCategoryId());
        } catch (IOException e) {
            log.error("Could not read prompt template file", e);
            // Fallback to a basic prompt if the file is missing for any reason
            return "Give a financial tip for an expense of " + request.getAmount() + " INR on " + request.getDescription();
        }
    }
}
