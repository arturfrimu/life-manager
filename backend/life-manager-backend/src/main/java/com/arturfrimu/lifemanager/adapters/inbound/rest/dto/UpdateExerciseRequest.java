package com.arturfrimu.lifemanager.adapters.inbound.rest.dto;

import com.arturfrimu.lifemanager.domain.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateExerciseRequest(
        @NotBlank(message = "Exercise name is required")
        @Size(max = 128, message = "Exercise name must not exceed 128 characters")
        String name,
        
        @NotNull(message = "Exercise type is required")
        Type type,
        
        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description
) {}

