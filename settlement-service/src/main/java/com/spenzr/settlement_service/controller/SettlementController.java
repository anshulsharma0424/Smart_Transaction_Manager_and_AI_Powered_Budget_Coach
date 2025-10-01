package com.spenzr.settlement_service.controller;

import com.spenzr.settlement_service.dto.SettlementDto;
import com.spenzr.settlement_service.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/settlement")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping
    public ResponseEntity<List<SettlementDto>> getSettlement(@PathVariable String eventId) {
        return ResponseEntity.ok(settlementService.calculateSettlement(eventId));
    }
}

