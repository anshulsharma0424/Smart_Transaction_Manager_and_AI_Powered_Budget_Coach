package com.spenzr.reporting_service.service;

import com.spenzr.reporting_service.client.TransactionClient;
import com.spenzr.reporting_service.dto.ReportSummaryDto;
import com.spenzr.reporting_service.dto.TransactionDto;
import com.spenzr.reporting_service.dto.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final TransactionClient transactionClient;

    public ReportSummaryDto generateSummary() {
        // Feign client calls the transaction-service to get all transactions
        List<TransactionDto> transactions = transactionClient.getAllUserTransactions();

        BigDecimal totalIncome = calculateTotal(transactions, TransactionType.INCOME);
        BigDecimal totalExpense = calculateTotal(transactions, TransactionType.EXPENSE);
        BigDecimal netAmount = totalIncome.subtract(totalExpense);

        return ReportSummaryDto.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netAmount(netAmount)
                .build();
    }

    private BigDecimal calculateTotal(List<TransactionDto> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .map(TransactionDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
