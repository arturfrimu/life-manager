package com.arturfrimu.lifemanager.controller.workout;

import java.time.Instant;
import java.util.UUID;

public record WorkoutSessionResponse(
        UUID id,
        UUID userId,
        String name,
        String notes,
        Instant startedAt,
        Instant completedAt,
        Instant created,
        Instant updated
) {
}