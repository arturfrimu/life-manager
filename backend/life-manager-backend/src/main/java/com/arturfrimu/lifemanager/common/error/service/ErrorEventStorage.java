package com.arturfrimu.lifemanager.common.error.service;

import com.arturfrimu.lifemanager.common.config.MinioProperties;
import com.arturfrimu.lifemanager.common.error.domain.ErrorEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorEventStorage {

    MinioClient minioClient;
    ObjectMapper objectMapper;
    FileErrorStorage fileErrorStorage;
    MinioProperties minioProperties;

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

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(objectName)
                        .stream(new ByteArrayInputStream(jsonData), jsonData.length, -1)
                        .contentType("application/json")
                        .build()
        );

        log.info("Saved error event to MinIO as {}", objectName);
    }

    @Recover
    public void recoverFromMinioFailure(Exception e, ErrorEvent errorEvent) throws Exception {
        log.warn("Failed to save error event to MinIO after retries, falling back to file storage: {}", e.getMessage());
        fileErrorStorage.saveErrorToFile(errorEvent);
        log.info("Successfully saved error event to file storage as fallback");
    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public ErrorEvent readEvent(String objectName) throws Exception {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(objectName)
                        .build())) {

            var event = objectMapper.readValue(stream, ErrorEvent.class);
            log.info("Read error event from MinIO: {}", objectName);
            return event;
        }
    }

    @Recover
    public ErrorEvent recoverFromReadFailure(Exception e, String objectName) {
        log.error("Failed to read error event from MinIO after retries: {}", objectName, e);
        throw new RuntimeException("Failed to read error event from MinIO: %s".formatted(objectName), e);
    }
}