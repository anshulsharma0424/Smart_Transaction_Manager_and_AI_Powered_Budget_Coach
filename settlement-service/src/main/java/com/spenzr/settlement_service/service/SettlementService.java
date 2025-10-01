package com.spenzr.settlement_service.service;

import com.spenzr.settlement_service.client.GroupExpenseClient;
import com.spenzr.settlement_service.client.GroupExpenseDto;
import com.spenzr.settlement_service.dto.SettlementDto;
import com.spenzr.settlement_service.dto.UserTransactionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final GroupExpenseClient groupExpenseClient;

    public List<SettlementDto> calculateSettlement(String eventId) {
        List<GroupExpenseDto> expenses = groupExpenseClient.getExpensesForEvent(eventId);
        if (expenses.isEmpty()) {
            return Collections.emptyList();
        }

        // Step 1: Calculate the net balance for each user
        Map<String, BigDecimal> balances = calculateNetBalances(expenses);

        // Step 2: Create a map of User ID -> User Name for easy lookup
        Map<String, String> userNameMap = createUserNameMap(expenses);

        // Step 3: Simplify the debts using the balances and the name map
        return simplifyDebts(balances, userNameMap);
    }

    private Map<String, String> createUserNameMap(List<GroupExpenseDto> expenses) {
        return expenses.stream()
                .flatMap(expense -> expense.getSplits().stream())
                .collect(Collectors.toMap(
                        com.spenzr.settlement_service.client.SplitDto::getUserId,
                        com.spenzr.settlement_service.client.SplitDto::getUserName,
                        (existing, replacement) -> existing // In case of duplicates, keep the existing one
                ));
    }

    private Map<String, BigDecimal> calculateNetBalances(List<GroupExpenseDto> expenses) {
        Map<String, BigDecimal> balances = new HashMap<>();
        for (GroupExpenseDto expense : expenses) {
            String payerId = expense.getPaidByUserId();
            balances.put(payerId, balances.getOrDefault(payerId, BigDecimal.ZERO).add(expense.getAmount()));
            expense.getSplits().forEach(split -> {
                balances.put(split.getUserId(), balances.getOrDefault(split.getUserId(), BigDecimal.ZERO).subtract(split.getShareAmount()));
            });
        }
        return balances;
    }

    private List<SettlementDto> simplifyDebts(Map<String, BigDecimal> balances, Map<String, String> userNameMap) {
        PriorityQueue<Map.Entry<String, BigDecimal>> creditors = new PriorityQueue<>((a, b) -> b.getValue().compareTo(a.getValue()));
        PriorityQueue<Map.Entry<String, BigDecimal>> debtors = new PriorityQueue<>(Comparator.comparing(Map.Entry::getValue));

        for (Map.Entry<String, BigDecimal> entry : balances.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(entry);
            } else if (entry.getValue().compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(entry);
            }
        }

        List<SettlementDto> settlements = new ArrayList<>();

        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            Map.Entry<String, BigDecimal> creditorEntry = creditors.poll();
            Map.Entry<String, BigDecimal> debtorEntry = debtors.poll();

            String creditorId = creditorEntry.getKey();
            BigDecimal credit = creditorEntry.getValue();
            String debtorId = debtorEntry.getKey();
            BigDecimal debt = debtorEntry.getValue().abs();

            BigDecimal payment = credit.min(debt);

            settlements.add(SettlementDto.builder()
                    .from(new UserTransactionInfo(debtorId, userNameMap.getOrDefault(debtorId, "Unknown")))
                    .to(new UserTransactionInfo(creditorId, userNameMap.getOrDefault(creditorId, "Unknown")))
                    .amount(payment.setScale(2, RoundingMode.HALF_UP))
                    .build());

            BigDecimal remainingCredit = credit.subtract(payment);
            BigDecimal remainingDebt = debt.subtract(payment).negate();

            if (remainingCredit.compareTo(BigDecimal.ZERO) > 0) {
                creditorEntry.setValue(remainingCredit);
                creditors.add(creditorEntry);
            }
            if (remainingDebt.compareTo(BigDecimal.ZERO) < 0) {
                debtorEntry.setValue(remainingDebt);
                debtors.add(debtorEntry);
            }
        }
        return settlements;
    }
}
