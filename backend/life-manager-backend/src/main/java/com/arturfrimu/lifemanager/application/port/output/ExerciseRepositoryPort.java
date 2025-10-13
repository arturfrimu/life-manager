package com.arturfrimu.lifemanager.application.port.output;

import com.arturfrimu.lifemanager.domain.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ExerciseRepositoryPort {
    Exercise save(Exercise exercise);

    Optional<Exercise> findById(UUID id);

    boolean existsByName(String name);

    Page<Exercise> findAll(Pageable pageable);

    void deleteById(UUID id);

    Exercise update(Exercise exercise);
}

