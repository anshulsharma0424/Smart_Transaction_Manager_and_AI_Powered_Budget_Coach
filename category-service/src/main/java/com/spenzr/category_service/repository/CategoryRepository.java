package com.spenzr.category_service.repository;

import com.spenzr.category_service.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    // Finds categories specific to a user OR system-wide categories (where userId is null)
    List<Category> findByUserIdOrUserIdIsNull(String userId);

    // Finds a specific category by its ID and UserId (to verify ownership)
    Optional<Category> findByIdAndUserId(String id, String userId);
}
