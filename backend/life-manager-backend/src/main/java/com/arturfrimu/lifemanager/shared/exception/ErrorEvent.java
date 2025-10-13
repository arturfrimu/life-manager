package com.arturfrimu.lifemanager.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Preconditions;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorEvent(
        String id,
        String eventType,
        String service,
        String timestamp,
        String message,
        Map<String, Object> details,
        Integer schemaVersion
) {
    public static ErrorEvent create(String eventType, String service, String message, Map<String, Object> details) {
        Preconditions.checkArgument(Objects.nonNull(eventType), "Event type cannot be null");
        Preconditions.checkArgument(Objects.nonNull(message), "Message cannot be null");

        return new ErrorEvent(
                UUID.randomUUID().toString(),
                eventType,
                service,
                Instant.now().toString(),
                message,
                details,
                1
        );
    }
}

