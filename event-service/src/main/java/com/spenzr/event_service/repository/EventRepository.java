package com.spenzr.event_service.repository;

import com.spenzr.event_service.entity.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    // Find all events where the user is listed as a participant
    List<Event> findByParticipantsUserId(String userId);

    // Find a specific event by its ID, but only if the user is a participant
    Optional<Event> findByIdAndParticipantsUserId(String id, String userId);

    // Find a specific event by its ID, but only if the user is the creator
    Optional<Event> findByIdAndCreatedBy(String id, String createdBy);
}
