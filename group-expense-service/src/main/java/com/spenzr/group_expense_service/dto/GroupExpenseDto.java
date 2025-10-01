package com.spenzr.group_expense_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class GroupExpenseDto {
    private String id;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;

    @NotBlank(message = "Payer user ID is required")
    private String paidByUserId;

    private String paidByUserName; // This will be set by the service

    // Splits can be optional on creation (for equal split), but validated if present
    @Valid
    private List<SplitDto> splits;
}