package com.arturfrimu.lifemanager.error.service;

import com.arturfrimu.lifemanager.config.ErrorStorageProperties;
import com.arturfrimu.lifemanager.error.domain.ErrorEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileErrorStorage {

    @NonFinal
    @Value("${spring.application.name:life-manager}")
    private String APP_NAME;

    ObjectMapper objectMapper;
    ErrorStorageProperties errorStorageProperties;

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 2,
            backoff = @Backoff(delay = 500)
    )
    public void saveErrorToFile(ErrorEvent errorEvent) throws Exception {
        Preconditions.checkArgument(Objects.nonNull(errorEvent), "ErrorEvent cannot be null");

        var basePath = Paths.get(errorStorageProperties.getFilePath());
        var datePath = createDateBasedPath(basePath, errorEvent.eventType());
        var fileName = generateFileName(errorEvent.eventType());
        var filePath = datePath.resolve(fileName);

        Files.createDirectories(datePath);

        var jsonContent = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(errorEvent);

        Files.writeString(filePath, jsonContent);

        log.info("Saved error event to file: {}", filePath);
    }

    @Recover
    public void recoverFromFileStorageFailure(Exception e, ErrorEvent errorEvent) {
        log.error("""
                \n
                ╔════════════════════════════════════════════════════════════════════════════════╗
                ║ CRITICAL ERROR: Failed to save error event to both MinIO and File Storage      ║
                ╠════════════════════════════════════════════════════════════════════════════════╣
                ║ Event Type: {} ║
                ║ Event ID: {} ║
                ║ Service: {} ║
                ║ Timestamp: {} ║
                ║ Message: {} ║
                ╠════════════════════════════════════════════════════════════════════════════════╣
                ║ Error Details:                                                                 ║
                ║ {}
                ╠════════════════════════════════════════════════════════════════════════════════╣
                ║ Full Event JSON:                                                               ║
                ║ {}
                ╚════════════════════════════════════════════════════════════════════════════════╝
                """,
                errorEvent.eventType(),
                errorEvent.id(),
                errorEvent.service(),
                errorEvent.timestamp(),
                errorEvent.message(),
                formatException(e),
                formatEventJson(errorEvent)
        );
    }

    private String formatException(Exception e) {
        return "%-74s".formatted(e.getClass().getSimpleName() + ": " + e.getMessage());
    }

    private String formatEventJson(ErrorEvent errorEvent) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(errorEvent)
                    .replaceAll("\n", "\n║ ");
        } catch (Exception e) {
            return "Failed to serialize event: " + e.getMessage();
        }
    }

    private Path createDateBasedPath(Path basePath, String eventType) {
        var now = LocalDate.now();
        return basePath
                .resolve(APP_NAME)
                .resolve(String.valueOf(now.getYear()))
                .resolve(String.format("%02d", now.getMonthValue()))
                .resolve(String.format("%02d", now.getDayOfMonth()))
                .resolve(eventType);
    }

    private String generateFileName(String eventType) {
        var uuid = UUID.randomUUID();
        var timestamp = Instant.now().toEpochMilli();
        return "%s_%s_%d.json".formatted(eventType, uuid, timestamp);
    }
}

