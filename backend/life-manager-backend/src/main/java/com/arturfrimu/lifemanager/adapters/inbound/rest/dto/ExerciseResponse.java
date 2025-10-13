package com.arturfrimu.lifemanager.adapters.inbound.rest.dto;

import com.arturfrimu.lifemanager.domain.model.Type;

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

