package com.arturfrimu.lifemanager.controller.exercise;

import java.time.Instant;
import java.util.UUID;

public record ExerciseResponse(
        UUID id,
        String name,
        String type,
        String description,
        String imageUrl,
        UUID createdByUserId,
        Instant created,
        Instant updated
) {
}

