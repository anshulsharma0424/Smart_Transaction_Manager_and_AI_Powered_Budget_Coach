package com.spenzr.reporting_service.controller;

import com.spenzr.reporting_service.dto.ReportSummaryDto;
import com.spenzr.reporting_service.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportingService reportingService;

    @GetMapping("/summary")
    public ResponseEntity<ReportSummaryDto> getSummaryReport() {
        return ResponseEntity.ok(reportingService.generateSummary());
    }
}
