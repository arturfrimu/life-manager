package com.arturfrimu.lifemanager.domain.model;

import com.google.common.base.Preconditions;

import java.time.Instant;
import java.util.UUID;

public record ImageMetadata(
        UUID id,
        String fileName,
        ContentType contentType,
        Long size,
        Instant created,
        Instant updated
) {
    public static ImageMetadata create(String fileName, String contentType, Long size) {
        Preconditions.checkArgument(fileName != null && !fileName.trim().isEmpty(), "File name cannot be null or empty");
        Preconditions.checkArgument(contentType != null, "ContentType cannot be null");
        return new ImageMetadata(
                UUID.randomUUID(),
                fileName,
                ContentType.valueOf(contentType.toUpperCase()),
                size,
                Instant.now(),
                Instant.now()
        );
    }
}