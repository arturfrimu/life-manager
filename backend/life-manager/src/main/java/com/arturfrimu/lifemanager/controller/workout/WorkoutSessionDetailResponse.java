package com.arturfrimu.lifemanager.controller.workout;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record WorkoutSessionDetailResponse(
        UUID id,
        UUID userId,
        String name,
        String notes,
        Instant startedAt,
        Instant completedAt,
        List<WorkoutExerciseDetailResponse> workoutExercises,
        Instant created,
        Instant updated
) {
}

