package com.spenzr.transaction_service.dto;

import com.spenzr.transaction_service.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/// Data Transfer Object representing a transaction event to be published to Kafka.

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEventDto {
    private String jwtToken;
    private Long transactionId;
    private String keycloakId;
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private String categoryId;
}