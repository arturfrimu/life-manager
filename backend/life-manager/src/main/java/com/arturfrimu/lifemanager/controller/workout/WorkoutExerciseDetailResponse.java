package com.arturfrimu.lifemanager.controller.workout;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record WorkoutExerciseDetailResponse(
        UUID id,
        Integer orderIndex,
        String notes,
        ExerciseInfo exercise,
        Instant created,
        Instant updated
) {
    public record ExerciseInfo(
            UUID id,
            String name,
            String type,
            String description,
            String imageUrl,
            List<SetResponse> sets
    ) {
    }
}

