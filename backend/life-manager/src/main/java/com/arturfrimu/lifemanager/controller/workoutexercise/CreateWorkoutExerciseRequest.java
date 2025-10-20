package com.arturfrimu.lifemanager.controller.workoutexercise;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateWorkoutExerciseRequest(
        @NotNull(message = "Workout session ID is required")
        UUID workoutSessionId,

        @NotNull(message = "Exercise ID is required")
        UUID exerciseId,

        String notes
) {}