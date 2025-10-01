package com.spenzr.suggestion_service.consumer;

import com.spenzr.suggestion_service.dto.TransactionDeletedEvent;
import com.spenzr.suggestion_service.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionEventConsumer {

    private final SuggestionService suggestionService;

    @KafkaListener(topics = "${app.kafka.transaction-events-topic}", groupId = "suggestion-cleanup-group")
    public void consumeTransactionDeletedEvent(TransactionDeletedEvent event) {
        log.info("==> Consumed transaction deleted event for transactionId: {}", event.getTransactionId());
        try {
            suggestionService.deleteSuggestionsByTransactionId(event.getTransactionId());
            log.info("Successfully deleted suggestions related to transactionId: {}", event.getTransactionId());
        } catch (Exception e) {
            log.error("Error processing transaction deleted event for transactionId: {}", event.getTransactionId(), e);
        }
    }
}

