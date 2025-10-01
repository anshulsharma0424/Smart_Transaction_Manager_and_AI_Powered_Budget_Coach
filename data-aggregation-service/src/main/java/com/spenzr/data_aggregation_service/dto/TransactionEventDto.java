package com.spenzr.data_aggregation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEventDto {
    private String jwtToken; // <-- ADD THIS FIELD
    private String transactionId;
    private String keycloakId;
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private String categoryId;
}
