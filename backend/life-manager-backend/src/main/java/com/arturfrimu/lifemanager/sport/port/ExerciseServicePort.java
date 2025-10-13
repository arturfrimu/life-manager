package com.arturfrimu.lifemanager.sport.port;

import com.arturfrimu.lifemanager.sport.domain.Exercise;
import com.arturfrimu.lifemanager.sport.enums.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ExerciseServicePort {
    Exercise createExercise(String name, Type type, String description);

    Exercise getExercise(UUID id);

    Page<Exercise> findAllExercises(Pageable pageable);
}
