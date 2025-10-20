package com.arturfrimu.lifemanager.controller.workout;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SetResponse(
        UUID id,
        Integer setIndex,
        Integer reps,
        BigDecimal weight,
        Integer durationSeconds,
        BigDecimal distanceMeters,
        Boolean completed,
        String notes,
        Instant created,
        Instant updated
) {}