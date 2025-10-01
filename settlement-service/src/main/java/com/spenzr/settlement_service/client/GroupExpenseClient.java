package com.spenzr.settlement_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "group-expense-service")
public interface GroupExpenseClient {
    @GetMapping("/events/{eventId}/expenses")
    List<GroupExpenseDto> getExpensesForEvent(@PathVariable("eventId") String eventId);
}