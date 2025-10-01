package com.spenzr.group_expense_service.repository;

import com.spenzr.group_expense_service.entity.GroupExpense;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface GroupExpenseRepository extends MongoRepository<GroupExpense, String> {
    List<GroupExpense> findByEventId(String eventId);
}
