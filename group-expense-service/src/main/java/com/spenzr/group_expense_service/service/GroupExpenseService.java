package com.spenzr.group_expense_service.service;

import com.spenzr.group_expense_service.client.EventClient;
import com.spenzr.group_expense_service.client.EventDto;
import com.spenzr.group_expense_service.dto.GroupExpenseDto;
import com.spenzr.group_expense_service.dto.SplitDto;
import com.spenzr.group_expense_service.dto.TransactionEventDto;
import com.spenzr.group_expense_service.dto.TransactionType;
import com.spenzr.group_expense_service.entity.GroupExpense;
import com.spenzr.group_expense_service.entity.Split;
import com.spenzr.group_expense_service.repository.GroupExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupExpenseService {

    private final GroupExpenseRepository groupExpenseRepository;
    private final EventClient eventClient;

    // Inject KafkaTemplate
    private final KafkaTemplate<String, TransactionEventDto> kafkaTemplate;

    // Inject the topic name from application.yml
    @Value("${app.kafka.transactions-topic}")
    private String transactionsTopic;

    public GroupExpenseDto createExpense(String eventId, GroupExpenseDto expenseDto, String currentUserId, Jwt jwt) {
        EventDto event = eventClient.getEventById(eventId);
        validateUserIsParticipant(event, currentUserId);

        String payerName = event.getParticipants().stream()
                .filter(p -> p.getUserId().equals(expenseDto.getPaidByUserId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Payer is not a participant in this event."))
                .getName();

        List<Split> splits;
        // If splits are not provided, we calculate them equally
        if (expenseDto.getSplits() == null || expenseDto.getSplits().isEmpty()) {
            splits = calculateEqualSplits(expenseDto.getAmount(), event);
        } else {
            // Here you would add logic to validate custom splits (e.g., ensure they sum up to the total amount)
            splits = expenseDto.getSplits().stream().map(this::mapSplitToEntity).collect(Collectors.toList());
        }

        GroupExpense groupExpense = mapToEntity(expenseDto);
        groupExpense.setEventId(eventId);
        groupExpense.setPaidByUserName(payerName);
        groupExpense.setSplits(splits);
        groupExpense.setCreatedAt(Instant.now());

        GroupExpense savedExpense = groupExpenseRepository.save(groupExpense);

        log.info("Group expense {} saved for event {}", savedExpense.getId(), eventId);

        // --- KAFKA PRODUCER LOGIC ---
        TransactionEventDto eventDto = mapToEventDto(savedExpense, jwt);
        log.info("Publishing group expense event to topic '{}' from user {}", transactionsTopic, eventDto.getKeycloakId());
        kafkaTemplate.send(transactionsTopic, eventDto.getKeycloakId(), eventDto);
        // --- END OF KAFKA LOGIC ---


        return mapToDto(savedExpense);
    }

    public List<GroupExpenseDto> getExpensesForEvent(String eventId, String currentUserId) {
        validateUserIsParticipant(eventClient.getEventById(eventId), currentUserId);
        return groupExpenseRepository.findByEventId(eventId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteExpense(String eventId, String expenseId, String currentUserId) {
        GroupExpense expense = groupExpenseRepository.findById(expenseId)
                .filter(e -> e.getEventId().equals(eventId))
                .orElseThrow(() -> new RuntimeException("Expense not found in this event."));

        // Allow deletion only by the person who paid or the event creator
        EventDto event = eventClient.getEventById(eventId);
        if (!Objects.equals(expense.getPaidByUserId(), currentUserId) && !Objects.equals(event.getCreatedBy(), currentUserId)) {
            throw new SecurityException("You do not have permission to delete this expense.");
        }

        groupExpenseRepository.delete(expense);
    }

    private List<Split> calculateEqualSplits(BigDecimal totalAmount, EventDto event) {
        int numberOfParticipants = event.getParticipants().size();
        if (numberOfParticipants == 0) throw new IllegalStateException("Event has no participants.");
        BigDecimal share = totalAmount.divide(new BigDecimal(numberOfParticipants), 2, RoundingMode.HALF_UP);
        return event.getParticipants().stream()
                .map(p -> new Split(p.getUserId(), p.getName(), share))
                .collect(Collectors.toList());
    }

    private void validateUserIsParticipant(EventDto event, String userId) {
        event.getParticipants().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new SecurityException("User is not a participant of this event."));
    }

    // --- Mappers ---
    private GroupExpenseDto mapToDto(GroupExpense expense) {
        return GroupExpenseDto.builder()
                .id(expense.getId())
                .description(expense.getDescription())
                .amount(expense.getAmount())
                .expenseDate(expense.getExpenseDate())
                .paidByUserId(expense.getPaidByUserId())
                .paidByUserName(expense.getPaidByUserName())
                .splits(expense.getSplits().stream().map(this::mapSplitToDto).collect(Collectors.toList()))
                .build();
    }

    private GroupExpense mapToEntity(GroupExpenseDto dto) {
        return GroupExpense.builder()
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .expenseDate(dto.getExpenseDate())
                .paidByUserId(dto.getPaidByUserId())
                .build();
    }

    private SplitDto mapSplitToDto(Split split) {
        return new SplitDto(split.getUserId(), split.getUserName(), split.getShareAmount());
    }

    private Split mapSplitToEntity(SplitDto dto) {
        return new Split(dto.getUserId(), dto.getUserName(), dto.getShareAmount());
    }

    private TransactionEventDto mapToEventDto(GroupExpense expense, Jwt jwt) {
        return TransactionEventDto.builder()
                .transactionId(expense.getId())
                .keycloakId(expense.getPaidByUserId())
                .type(TransactionType.EXPENSE)
                .amount(expense.getAmount())
                .description(String.format("Group Expense: %s (Event ID: %s)", expense.getDescription(), expense.getEventId()))
                .transactionDate(expense.getExpenseDate())
                .categoryId("GROUP_EXPENSE") // Use a special identifier for category
                .jwtToken(jwt.getTokenValue())
                .build();
    }
}
