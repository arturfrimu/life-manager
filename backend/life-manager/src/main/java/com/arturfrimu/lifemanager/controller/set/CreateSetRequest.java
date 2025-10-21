package com.arturfrimu.lifemanager.controller.set;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateSetRequest(
        @NotNull(message = "Workout exercise ID is required")
        UUID workoutExerciseId,
        @NotNull(message = "Reps is required")
        Integer reps,
        @NotNull(message = "Weight is required")
        BigDecimal weight
) {}