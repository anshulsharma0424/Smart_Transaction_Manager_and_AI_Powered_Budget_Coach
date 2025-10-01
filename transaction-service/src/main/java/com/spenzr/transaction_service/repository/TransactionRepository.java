package com.spenzr.transaction_service.repository;

import com.spenzr.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByKeycloakIdOrderByTransactionDateDesc(String keycloakId);
    Optional<Transaction> findByIdAndKeycloakId(Long id, String keycloakId);
}