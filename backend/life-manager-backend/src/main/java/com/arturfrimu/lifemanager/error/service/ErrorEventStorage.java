package com.arturfrimu.lifemanager.error.service;

import com.arturfrimu.lifemanager.config.MinioProperties;
import com.arturfrimu.lifemanager.error.domain.ErrorEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    MinioProperties minioProperties;

    public String generateFileName(String eventType) {
        var uuid = UUID.randomUUID();
        var timestamp = Instant.now().toEpochMilli();
        return "%s_%s_%d.json".formatted(eventType, uuid, timestamp);
    }

    public void saveEventWithRetry(ErrorEvent errorEvent) {
        var eventType = errorEvent.eventType();
        var objectName = generateFileName(eventType);
        var maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
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
                break;
            } catch (MinioException e) {
                log.error("Attempt {} failed to save error event: {}", attempt, e.getMessage());
                if (attempt == maxRetries) {
                    log.error("Failed to save error event after {} attempts", maxRetries, e);
                    throw new RuntimeException("Failed to save error event to MinIO", e);
                }

                try {
                    Thread.sleep(1000L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                log.error("Failed to save error event", e);
                throw new RuntimeException("Failed to save error event to MinIO", e);
            }
        }
    }

    public ErrorEvent readEvent(String objectName) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(objectName)
                        .build())) {

            var event = objectMapper.readValue(stream, ErrorEvent.class);
            log.info("Read error event from MinIO: {}", objectName);
            return event;
        } catch (Exception e) {
            log.error("Failed to read error event from MinIO: {}", objectName, e);
            throw new RuntimeException("Failed to read error event from MinIO", e);
        }
    }
}