package com.spenzr.group_expense_service.controller;

import com.spenzr.group_expense_service.dto.GroupExpenseDto;
import com.spenzr.group_expense_service.service.GroupExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/expenses")
@RequiredArgsConstructor
public class GroupExpenseController {

    private final GroupExpenseService groupExpenseService;

    @PostMapping
    public ResponseEntity<GroupExpenseDto> createExpense(@PathVariable String eventId,
                                                         @Valid @RequestBody GroupExpenseDto expenseDto,
                                                         @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        GroupExpenseDto createdExpense = groupExpenseService.createExpense(eventId, expenseDto, userId, jwt);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GroupExpenseDto>> getExpensesForEvent(@PathVariable String eventId,
                                                                     @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(groupExpenseService.getExpensesForEvent(eventId, userId));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String eventId,
                                              @PathVariable String expenseId,
                                              @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        groupExpenseService.deleteExpense(eventId, expenseId, userId);
        return ResponseEntity.noContent().build();
    }
}

