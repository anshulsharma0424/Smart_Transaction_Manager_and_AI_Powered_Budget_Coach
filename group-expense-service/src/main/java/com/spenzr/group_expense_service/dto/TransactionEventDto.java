package com.spenzr.group_expense_service.dto;

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
    private String jwtToken;
    private String transactionId; // Can be the MongoDB ObjectId string
    private String keycloakId; // The ID of the person who paid
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private String categoryId; // We'll use a special value for group expenses
}

