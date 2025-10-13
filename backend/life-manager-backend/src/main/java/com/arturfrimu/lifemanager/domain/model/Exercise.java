package com.arturfrimu.lifemanager.domain.model;

import com.google.common.base.Preconditions;

import java.time.Instant;
import java.util.UUID;

public record Exercise(
        UUID id,
        String name,
        Type type,
        String description,
        Instant created,
        Instant updated
) {
    public static Exercise create(String name, Type type, String description) {
        Preconditions.checkArgument(name != null && !name.trim().isEmpty(), "Name cannot be null or empty");
        Preconditions.checkArgument(type != null, "Type cannot be null");

        return new Exercise(
                UUID.randomUUID(),
                name.trim(),
                type,
                description,
                null,
                null
        );
    }
}

