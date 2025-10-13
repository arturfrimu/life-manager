package com.arturfrimu.lifemanager.common.exception.service;

import com.arturfrimu.lifemanager.common.config.MinioProperties;
import com.arturfrimu.lifemanager.common.exception.domain.ErrorEvent;
import com.arturfrimu.lifemanager.common.storage.MinioStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MinioErrorEventStorage {

    ObjectMapper objectMapper;
    MinioStorageService minioStorageService;
    MinioProperties minioProperties;
    FileErrorStorage fileErrorStorage;

    public String generateFileName(String eventType) {
        var uuid = UUID.randomUUID();
        var timestamp = Instant.now().toEpochMilli();
        return "%s_%s_%d.json".formatted(eventType, uuid, timestamp);
    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void saveEventWithRetry(ErrorEvent errorEvent) throws Exception {
        var objectName = generateFileName(errorEvent.eventType());
        var jsonData = objectMapper.writeValueAsBytes(errorEvent);

        minioStorageService.uploadFile(
                minioProperties.getBucketName(),
                objectName,
                new ByteArrayInputStream(jsonData),
                jsonData.length,
                "application/json"
        );

        log.info("Saved error event to MinIO as {}", objectName);
    }

    @Recover
    public void recoverFromMinioFailure(Exception e, ErrorEvent errorEvent) throws Exception {
        log.warn("Failed to save error event to MinIO after retries, falling back to file storage: {}", e.getMessage());
        fileErrorStorage.saveErrorToFile(errorEvent);
        log.info("Successfully saved error event to file storage as fallback");
    }
}