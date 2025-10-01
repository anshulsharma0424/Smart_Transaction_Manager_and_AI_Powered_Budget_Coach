package com.spenzr.transaction_service.controller;

import com.spenzr.transaction_service.dto.TransactionDto;
import com.spenzr.transaction_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionDto transactionDto, @AuthenticationPrincipal Jwt jwt) { // <-- Jwt is already here
        String keycloakId = jwt.getSubject();
        // Pass the full jwt object to the service
        TransactionDto createdTransaction = transactionService.createTransaction(transactionDto, keycloakId, jwt); // <-- PASS JWT
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllUserTransactions(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        return ResponseEntity.ok(transactionService.getTransactionsForUser(keycloakId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDto transactionDto, @AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDto, keycloakId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();
        transactionService.deleteTransaction(id, keycloakId);
        return ResponseEntity.noContent().build();
    }
}
