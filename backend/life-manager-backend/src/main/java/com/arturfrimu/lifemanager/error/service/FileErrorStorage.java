package com.arturfrimu.lifemanager.error.service;

import com.arturfrimu.lifemanager.config.ErrorStorageProperties;
import com.arturfrimu.lifemanager.error.domain.ErrorEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileErrorStorage {

    ObjectMapper objectMapper;
    ErrorStorageProperties errorStorageProperties;

    public void saveErrorToFile(ErrorEvent errorEvent) {
        Preconditions.checkArgument(Objects.nonNull(errorEvent), "ErrorEvent cannot be null");

        try {
            var basePath = Paths.get(errorStorageProperties.getFilePath());
            var datePath = createDateBasedPath(basePath, errorEvent.eventType());
            var fileName = generateFileName(errorEvent.eventType());
            var filePath = datePath.resolve(fileName);

            Files.createDirectories(datePath);

            var jsonContent = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(errorEvent);

            Files.writeString(filePath, jsonContent);

            log.info("Saved error event to file: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to save error event to file", e);
            throw new RuntimeException("Failed to save error event to file", e);
        }
    }

    private Path createDateBasedPath(Path basePath, String eventType) {
        var now = LocalDate.now();
        return basePath
                .resolve("life-manager")
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

