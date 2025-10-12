package com.arturfrimu.lifemanager.sport.dto;

import com.arturfrimu.lifemanager.sport.enums.Type;

import java.time.Instant;
import java.util.UUID;

public record ExerciseResponse(
        UUID id,
        String name,
        Type type,
        String description,
        Instant created,
        Instant updated
) {
}
