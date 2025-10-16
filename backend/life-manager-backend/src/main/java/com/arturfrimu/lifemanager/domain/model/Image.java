package com.arturfrimu.lifemanager.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Image(
        UUID id,
        String fileName,
        String objectKey,
        String bucketName,
        String url,
        String contentType,
        Long size,
        Instant created,
        Instant updated
) {}