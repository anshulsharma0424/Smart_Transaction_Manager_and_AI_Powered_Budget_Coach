package com.spenzr.group_expense_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SplitDto {
    @NotBlank(message = "User ID for split is required")
    private String userId;
    private String userName; // This is for display, not input
    @NotNull(message = "Share amount is required")
    @Positive(message = "Share amount must be positive")
    private BigDecimal shareAmount;
}
