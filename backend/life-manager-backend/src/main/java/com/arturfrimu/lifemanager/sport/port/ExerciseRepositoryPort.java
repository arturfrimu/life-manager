package com.arturfrimu.lifemanager.sport.port;

import com.arturfrimu.lifemanager.sport.domain.Exercise;

import java.util.Optional;
import java.util.UUID;

public interface ExerciseRepositoryPort {
    Exercise save(Exercise exercise);

    Optional<Exercise> findById(UUID id);

    boolean existsByName(String name);
}
