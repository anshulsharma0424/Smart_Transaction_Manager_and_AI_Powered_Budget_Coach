package com.spenzr.reporting_service.client;

import com.spenzr.reporting_service.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name = "transaction-service")
public interface TransactionClient {

    // This signature matches the endpoint in TransactionController
    @GetMapping("/transactions")
    List<TransactionDto> getAllUserTransactions();
}
