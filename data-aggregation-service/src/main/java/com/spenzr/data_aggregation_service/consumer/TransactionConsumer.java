package com.spenzr.data_aggregation_service.consumer;

import com.spenzr.data_aggregation_service.client.*;
import com.spenzr.data_aggregation_service.dto.TransactionEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionConsumer {

    private final AiInferenceClient aiInferenceClient;
    private final SuggestionClient suggestionClient;

    @KafkaListener(topics = "${app.kafka.transactions-topic}", groupId = "ai-coach-group")
    public void consume(TransactionEventDto transactionEvent) {
        log.info("==> Consumed transaction event for user '{}'", transactionEvent.getKeycloakId());

        try {
            // Step 1: Build the request for the AI service
            SuggestionRequest suggestionRequest = SuggestionRequest.builder()
                    .description(transactionEvent.getDescription())
                    .amount(transactionEvent.getAmount())
                    .categoryId(transactionEvent.getCategoryId())
                    .build();

            // Step 2: Call the AI Inference Service
            log.info("Calling AI Inference Service for user '{}'...", transactionEvent.getKeycloakId());
            SuggestionResponse aiResponse = aiInferenceClient.generateSuggestion(
                    "Bearer " + transactionEvent.getJwtToken(),
                    suggestionRequest
            );
            log.info("==> AI Suggestion received: '{}'", aiResponse.getSuggestion());

            // Step 3: Build the request to save the suggestion
            CreateSuggestionRequest saveRequest = CreateSuggestionRequest.builder()
                    .userId(transactionEvent.getKeycloakId())
                    .suggestionContent(aiResponse.getSuggestion())
                    .relatedTransactionId(transactionEvent.getTransactionId())
                    .relatedTransactionDescription(transactionEvent.getDescription())
                    .build();

            // Step 4: Call the Suggestion Service to save the result
            suggestionClient.saveSuggestion("Bearer " + transactionEvent.getJwtToken(), saveRequest);
            log.info("==> Suggestion successfully saved for user '{}'", transactionEvent.getKeycloakId());

        } catch (Exception e) {
            log.error("Failed to process AI suggestion pipeline for user {}", transactionEvent.getKeycloakId(), e);
        }
    }
}