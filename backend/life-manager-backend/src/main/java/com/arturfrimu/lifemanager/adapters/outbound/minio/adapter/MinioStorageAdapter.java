package com.arturfrimu.lifemanager.adapters.outbound.minio.adapter;

import com.arturfrimu.lifemanager.config.MinioProperties;
import com.arturfrimu.lifemanager.domain.port.output.FileStoragePort;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MinioStorageAdapter implements FileStoragePort {

    MinioClient minioClient;
    MinioProperties minioProperties;

    @Override
    public void putObject(FileMetadata fileMetadata) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(fileMetadata.objectKey())
                    .contentType(fileMetadata.contentType())
                    .stream(fileMetadata.stream(), fileMetadata.size(), -1)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listObjectKeys(String prefix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String presignGet(String objectKey, Duration ttl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

