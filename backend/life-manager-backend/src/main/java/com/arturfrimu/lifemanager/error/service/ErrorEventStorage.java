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
    FileErrorStorage fileErrorStorage;
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
        var minioSaveSuccessful = false;

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
                minioSaveSuccessful = true;
                break;
            } catch (MinioException e) {
                log.error("Attempt {} failed to save error event to MinIO: {}", attempt, e.getMessage());
                if (attempt == maxRetries) {
                    log.warn("Failed to save error event to MinIO after {} attempts, falling back to file storage", maxRetries);
                }

                try {
                    Thread.sleep(1000L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                log.error("Failed to save error event to MinIO: {}", e.getMessage());
                if (attempt == maxRetries) {
                    log.warn("Failed to save error event to MinIO, falling back to file storage");
                }
            }
        }

        if (!minioSaveSuccessful) {
            try {
                fileErrorStorage.saveErrorToFile(errorEvent);
                log.info("Successfully saved error event to file storage as fallback");
            } catch (Exception e) {
                log.error("Failed to save error event to both MinIO and file storage", e);
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