package com.spenzr.group_expense_service.client;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class EventDto {
    private String id;
    private String name;
    private LocalDate eventDate;
    private String createdBy; // <-- ADD THIS LINE
    private Set<ParticipantDto> participants;
}
