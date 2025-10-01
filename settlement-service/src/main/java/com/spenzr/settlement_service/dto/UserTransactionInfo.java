package com.spenzr.settlement_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTransactionInfo {
    private String userId;
    private String name;
}
