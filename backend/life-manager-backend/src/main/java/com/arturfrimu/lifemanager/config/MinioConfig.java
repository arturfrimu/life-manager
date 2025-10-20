package com.arturfrimu.lifemanager.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MinioConfig {

    MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        try {
            var client = MinioClient.builder()
                    .endpoint(minioProperties.getEndpoint())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                    .build();

            if (minioProperties.isAutoCreateBucket()) {
                var bucketExists = client.bucketExists(
                        BucketExistsArgs.builder()
                                .bucket(minioProperties.getBucket())
                                .build()
                );

                if (!bucketExists) {
                    client.makeBucket(
                            MakeBucketArgs.builder()
                                    .bucket(minioProperties.getBucket())
                                    .build()
                    );
                    log.info("Created MinIO bucket: {}", minioProperties.getBucket());
                } else {
                    log.info("MinIO bucket already exists: {}", minioProperties.getBucket());
                }
            }

            log.info("MinIO client configured successfully");
            return client;
        } catch (Exception e) {
            log.error("Failed to configure MinIO client", e);
            throw new RuntimeException("Failed to configure MinIO client", e);
        }
    }
}

