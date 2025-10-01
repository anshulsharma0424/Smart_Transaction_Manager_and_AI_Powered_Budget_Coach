package com.spenzr.suggestion_service.dto;

import lombok.Data;

@Data
public class TransactionDeletedEvent {
    // We only care about the transactionId
    private String transactionId;
}
