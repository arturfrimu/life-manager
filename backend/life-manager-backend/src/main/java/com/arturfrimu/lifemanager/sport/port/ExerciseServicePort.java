package com.arturfrimu.lifemanager.sport.port;

import com.arturfrimu.lifemanager.sport.domain.Exercise;
import com.arturfrimu.lifemanager.sport.enums.Type;

public interface ExerciseServicePort {
    Exercise createExercise(String name, Type type, String description);

    Exercise getExercise(java.util.UUID id);
}
