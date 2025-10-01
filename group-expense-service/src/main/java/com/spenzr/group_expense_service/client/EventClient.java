package com.spenzr.group_expense_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service")
public interface EventClient {
    @GetMapping("/events/{id}")
    EventDto getEventById(@PathVariable("id") String id);
}
