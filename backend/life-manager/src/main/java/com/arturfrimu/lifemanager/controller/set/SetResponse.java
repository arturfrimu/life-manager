package com.arturfrimu.lifemanager.controller.set;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SetResponse(
        UUID id,
        UUID workoutExerciseId,
        Integer setIndex,
        Integer reps,
        BigDecimal weight,
        Boolean completed,
        String notes,
        Instant created,
        Instant updated
) {}

