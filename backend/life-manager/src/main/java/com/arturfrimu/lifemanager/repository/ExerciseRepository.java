package com.arturfrimu.lifemanager.repository;

import com.arturfrimu.lifemanager.entity.Exercise;
import com.arturfrimu.lifemanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    List<Exercise> findByCreatedByUser(User user);
    List<Exercise> findByCreatedByUserIsNull();
    List<Exercise> findByCreatedByUserOrCreatedByUserIsNull(User user);
}

