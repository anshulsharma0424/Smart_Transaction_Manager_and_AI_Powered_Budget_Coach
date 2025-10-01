package com.spenzr.event_service.service;

import com.spenzr.event_service.client.UserClient;
import com.spenzr.event_service.client.UserDto;
import com.spenzr.event_service.dto.EventDto;
import com.spenzr.event_service.dto.ParticipantDto;
import com.spenzr.event_service.entity.Event;
import com.spenzr.event_service.entity.Participant;
import com.spenzr.event_service.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserClient userClient; // <-- INJECT THE FEIGN CLIENT

    public EventDto createEvent(EventDto eventDto, Jwt currentUser) {
        // The creator is automatically the first participant
        Participant creatorAsParticipant = Participant.builder()
                .userId(currentUser.getSubject())
                .name(currentUser.getClaimAsString("name"))
                .build();

        Event event = Event.builder()
                .name(eventDto.getName())
                .description(eventDto.getDescription())
                .eventDate(eventDto.getEventDate())
                .createdBy(currentUser.getSubject())
                .createdAt(Instant.now())
                .participants(Set.of(creatorAsParticipant))
                .build();

        Event savedEvent = eventRepository.save(event);
        return mapToDto(savedEvent);
    }

    public List<EventDto> getEventsForUser(String userId) {
        return eventRepository.findByParticipantsUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteEvent(String eventId, String userId) {
        Event event = eventRepository.findByIdAndCreatedBy(eventId, userId)
                .orElseThrow(() -> new RuntimeException("Event not found or you are not the creator."));
        eventRepository.delete(event);
    }

    // ADD THESE NEW METHODS
    public EventDto addParticipant(String eventId, String userEmail, String currentUserId) {
        Event event = eventRepository.findByIdAndCreatedBy(eventId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Event not found or you are not the creator."));

        // Call the User Service to find the user to add
        UserDto userToAdd = userClient.findUserByEmail(userEmail);

        Participant newParticipant = Participant.builder()
                .userId(userToAdd.getKeycloakId())
                .name(userToAdd.getFirstName() + " " + userToAdd.getLastName())
                .build();

        event.getParticipants().add(newParticipant);
        eventRepository.save(event);
        return mapToDto(event);
    }

    public void removeParticipant(String eventId, String participantUserId, String currentUserId) {
        Event event = eventRepository.findByIdAndCreatedBy(eventId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Event not found or you are not the creator."));

        // Cannot remove the creator of the event
        if (participantUserId.equals(currentUserId)) {
            throw new RuntimeException("Cannot remove the event creator.");
        }

        event.getParticipants().removeIf(p -> p.getUserId().equals(participantUserId));
        eventRepository.save(event);
    }

    // ADD THIS NEW METHOD
    public EventDto getEventById(String eventId, String userId) {
        Event event = eventRepository.findByIdAndParticipantsUserId(eventId, userId)
                .orElseThrow(() -> new RuntimeException("Event not found or you are not a participant."));
        return mapToDto(event);
    }


    // --- Helper Mappers ---

    private EventDto mapToDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .createdBy(event.getCreatedBy()) // <-- ADD THIS LINE
                .participants(event.getParticipants().stream().map(this::mapParticipantToDto).collect(Collectors.toSet()))
                .build();
    }

    private ParticipantDto mapParticipantToDto(Participant participant) {
        return ParticipantDto.builder()
                .userId(participant.getUserId())
                .name(participant.getName())
                .build();
    }
}
