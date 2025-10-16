package com.arturfrimu.lifemanager.adapters.inbound.rest.dto;

import java.time.Instant;
import java.util.UUID;

public record ImageResponse(
        UUID id,
        String fileName,
        String url,
        String contentType,
        Long size,
        Instant created
) {}