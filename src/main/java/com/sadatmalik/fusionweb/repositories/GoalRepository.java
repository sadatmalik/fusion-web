package com.sadatmalik.fusionweb.repositories;

import com.sadatmalik.fusionweb.model.Goal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends CrudRepository<Goal, Long> {
}
