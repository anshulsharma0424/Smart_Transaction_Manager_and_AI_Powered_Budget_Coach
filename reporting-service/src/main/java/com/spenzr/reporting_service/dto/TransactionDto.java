package com.spenzr.reporting_service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

// This DTO must match the structure of the one in TransactionService
@Data
public class TransactionDto {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private String categoryId;
}
