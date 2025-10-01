package com.spenzr.transaction_service.service;

import com.spenzr.transaction_service.dto.TransactionDeletedEvent;
import com.spenzr.transaction_service.dto.TransactionDto;
import com.spenzr.transaction_service.dto.TransactionEventDto;
import com.spenzr.transaction_service.entity.Transaction;
import com.spenzr.transaction_service.entity.TransactionType;
import com.spenzr.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    // Inject KafkaTemplate for sending messages
    // private final KafkaTemplate <String, TransactionEventDto> kafkaTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate; // Use Object for multiple event types

    // Inject the topic name from application.yml
    @Value("${app.kafka.transactions-topic}")
    private String transactionsTopic;

    // Inject the new topic name
    @Value("${app.kafka.transaction-events-topic}")
    private String transactionEventsTopic;

    public TransactionDto createTransaction(TransactionDto transactionDto, String keycloakId, Jwt jwt) {
        Transaction transaction = mapToEntity(transactionDto);
        transaction.setKeycloakId(keycloakId);
        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("Transaction {} saved for user {}", savedTransaction.getId(), keycloakId);

        // --- KAFKA PRODUCER LOGIC ---
        // We only publish an event if the transaction is an EXPENSE
        if (savedTransaction.getType() == TransactionType.EXPENSE) {
            TransactionEventDto eventDto = mapToEventDto(savedTransaction, jwt);
            log.info("Publishing expense event to topic '{}' for user {}", transactionsTopic, keycloakId);
            // The keycloakId is used as the Kafka message key to ensure all transactions
            // for the same user go to the same partition, preserving order.
            kafkaTemplate.send(transactionsTopic, eventDto.getKeycloakId(), eventDto);
        }
        // --- END OF KAFKA LOGIC ---

        return mapToDto(savedTransaction);
    }

    public List<TransactionDto> getTransactionsForUser(String keycloakId) {
        return transactionRepository.findByKeycloakIdOrderByTransactionDateDesc(keycloakId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TransactionDto updateTransaction(Long id, TransactionDto transactionDto, String keycloakId) {
        Transaction existingTransaction = transactionRepository.findByIdAndKeycloakId(id, keycloakId)
                .orElseThrow(() -> new RuntimeException("Transaction not found or permission denied."));

        existingTransaction.setType(transactionDto.getType());
        existingTransaction.setAmount(transactionDto.getAmount());
        existingTransaction.setDescription(transactionDto.getDescription());
        existingTransaction.setTransactionDate(transactionDto.getTransactionDate());
        existingTransaction.setCategoryId(transactionDto.getCategoryId());

        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return mapToDto(updatedTransaction);
    }

    public void deleteTransaction(Long id, String keycloakId) {
        Transaction transaction = transactionRepository.findByIdAndKeycloakId(id, keycloakId)
                .orElseThrow(() -> new RuntimeException("Transaction not found or permission denied."));
        transactionRepository.delete(transaction);

        log.info("Deleted transaction with id: {}", id);

        // --- KAFKA PRODUCER LOGIC for deletion ---
        TransactionDeletedEvent event = TransactionDeletedEvent.builder().transactionId(id).build();
        kafkaTemplate.send(transactionEventsTopic, event);
        log.info("Published transaction deleted event for id: {}", id);
    }

    // --- Helper Mappers ---

    private TransactionDto mapToDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .categoryId(transaction.getCategoryId())
                .build();
    }

    private Transaction mapToEntity(TransactionDto transactionDto) {
        return Transaction.builder()
                .id(transactionDto.getId())
                .type(transactionDto.getType())
                .amount(transactionDto.getAmount())
                .description(transactionDto.getDescription())
                .transactionDate(transactionDto.getTransactionDate())
                .categoryId(transactionDto.getCategoryId())
                .build();
    }

    // New mapper for the event DTO
    private TransactionEventDto mapToEventDto(Transaction transaction, Jwt jwt) {
        return TransactionEventDto.builder()
                .transactionId(transaction.getId())
                .keycloakId(transaction.getKeycloakId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .categoryId(transaction.getCategoryId())
                .jwtToken(jwt.getTokenValue())
                .build();
    }
}
