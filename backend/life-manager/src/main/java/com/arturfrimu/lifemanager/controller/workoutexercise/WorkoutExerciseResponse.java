package com.arturfrimu.lifemanager.controller.workoutexercise;

import java.time.Instant;
import java.util.UUID;

public record WorkoutExerciseResponse(
        UUID id,
        UUID workoutSessionId,
        UUID exerciseId,
        Integer orderIndex,
        String notes,
        Instant created,
        Instant updated
) {
}