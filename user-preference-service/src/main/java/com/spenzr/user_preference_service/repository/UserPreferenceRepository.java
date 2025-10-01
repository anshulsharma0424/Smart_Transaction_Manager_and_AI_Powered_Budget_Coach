package com.spenzr.user_preference_service.repository;

import com.spenzr.user_preference_service.entity.UserPreference;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    Optional<UserPreference> findByKeycloakId(String keycloakId);
}
